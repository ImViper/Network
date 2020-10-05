package chapter05;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private int port = 8008; //服务器监听端口
    private ServerSocket serverSocket; //定义服务器套接字
    private ExecutorService executorService;
    private static Set<Socket> members = new CopyOnWriteArraySet<Socket>();
    private static HashMap<String,Socket> info = new HashMap<String, Socket>();

    public ChatServer() throws IOException {
        serverSocket = new ServerSocket(8008);
        executorService = Executors.newCachedThreadPool();
        System.out.println("多用户服务器启动");
    }
    public void service(){
        while(true){
            Socket socket = null;
            try{
                socket = serverSocket.accept(); //监听客户请求, 阻塞语句.
                members.add(socket);
                //接受一个客户请求,从线程池中拿出一个线程专门处理该客户.
                executorService.execute(new Handler(socket));
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void sendToAllMembers(String msg,String hostAddress) throws IOException{
        PrintWriter pw;
        OutputStream out;
        for(Socket tempSocket:members) {
            out = tempSocket.getOutputStream();
            pw = new PrintWriter(new OutputStreamWriter(out,"utf-8"),true);
            pw.println(hostAddress + "发言="+msg);

        }
//        Socket tempSocket;

//        Iterator<Socket> iterator = members.iterator();
//        while (iterator.hasNext()) {//遍历在线客户Set集合
//            tempSocket = iterator.next(); //取出一个客户的socket
//            String hostName = tempSocket.getInetAddress().getHostName();
//            String hostAddress = tempSocket.getInetAddress().getHostAddress();
//            out = tempSocket.getOutputStream();
//            pw = new PrintWriter(
//                    new OutputStreamWriter(out, "utf-8"), true);
//            pw.println(tempSocket.getInetAddress() + " 发言：" + msg );

    }

    class Handler implements Runnable{
        private Socket socket;
        public Handler(Socket socket) {
            this.socket = socket;
        }


        @Override
        public void run() {
            //本地服务器控制台显示客户端连接的用户信息
            System.out.println("New connection accepted： " + socket.getInetAddress());
            try {
                BufferedReader br = getReader(socket);//定义字符串输入流
                PrintWriter pw = getWriter(socket);//定义字符串输出流

                //客户端正常连接成功，则发送服务器欢迎信息，然后等待客户发送信息
                pw.println("From 服务器：欢迎使用本服务！");

                String msg = null;
                //此处程序阻塞，每次从输入流中读入一行字符串
                while ((msg = br.readLine()) != null) {
                    //如果客户发送的消息为"bye"，就结束通信
                    if (msg.trim().equalsIgnoreCase("bye")) {
                        //向输出流中输出一行字符串,远程客户端可以读取该字符串
                        pw.println("From 服务器：服务器已断开连接，结束服务！");
                        System.out.println("客户端离开");
                        break;//跳出循环读取
                    }
                    //向输出流中回传字符串,远程客户端可以读取该字符串
                    sendToAllMembers(msg,socket.getInetAddress().getHostAddress());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(socket != null)
                        socket.close(); //关闭socket连接及相关的输入输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private PrintWriter getWriter(Socket socket) throws IOException {
        //获得输出流缓冲区的地址
        OutputStream socketOut = socket.getOutputStream();
        //网络流写出需要使用flush，这里在PrintWriter构造方法中直接设置为自动flush
        return new PrintWriter(
                new OutputStreamWriter(socketOut, "utf-8"), true);
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        //获得输入流缓冲区的地址
        InputStream socketIn = socket.getInputStream();
        return new BufferedReader(
                new InputStreamReader(socketIn, "utf-8"));
    }


    //单客户版本，即每一次只能与一个客户建立通信连接


    public static void main(String[] args) throws IOException{
        new ChatServer().service();
    }
}