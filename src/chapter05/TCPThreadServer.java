package chapter05;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPThreadServer {
    private int port = 8008; //服务器监听端口
    private ServerSocket serverSocket; //定义服务器套接字
    private ExecutorService executorService;

    public TCPThreadServer() throws IOException {
        serverSocket = new ServerSocket(8008);
        executorService = Executors.newCachedThreadPool();
        System.out.println("多用户服务器启动");
    }
    public void service(){
        while(true){
            Socket socket = null;
            try{
                socket = serverSocket.accept(); //监听客户请求, 阻塞语句.
                //接受一个客户请求,从线程池中拿出一个线程专门处理该客户.
                executorService.execute(new Handler(socket));
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    class Handler implements Runnable{
        private Socket socket;
        public Handler(Socket socket) {
            this.socket = socket;
        }


        @Override
        public void run() {
            System.out.println("New connection accepted:"+socket.getInetAddress());
            try{
                BufferedReader br = getReader(socket);
                PrintWriter pw = getWriter(socket);
                pw.println("From 服务器：欢饮使用本服务！");

                String msg = null;
                while((msg = br.readLine())!=null){
                    if(msg.trim().equalsIgnoreCase("bye")){
                        pw.println("From 服务器：服务器已经断开连接，结束服务！");
                        System.out.println("客户端离开");
                        break;
                    }
                    pw.println("From 服务器"+msg);
                }

            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try{
                    if(socket !=null)
                        socket.close();
                }catch (IOException e){
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




    public static void main(String[] args) throws IOException{
        new TCPThreadServer().service();
    }
}