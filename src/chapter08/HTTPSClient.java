//package chapter02;
//
//import java.io.*;
//import java.net.Socket;
//
//public class TCPClient {
//    private Socket socket;
//    private PrintWriter pw;
//    private BufferedReader br;
//
//    public TCPClient(String ip,String port) throws IOException {
//        socket = new Socket(ip, Integer.parseInt(port));
//        OutputStream socketOut = socket.getOutputStream();
//        pw = new PrintWriter(
//                new OutputStreamWriter(socketOut, "utf-8"), true);
//        InputStream socketIn = socket.getInputStream();
//        br = new BufferedReader(
//                new InputStreamReader(socketIn, "utf-8"));
//    }
//        public void send(String msg) {
//            pw.println(msg);
//        }
//        public String receive(){
//            String msg = null;
//            try {
//                msg=br.readLine();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return msg;
//        }
//        public void close(){
//            try {
//                if(socket != null){
//                    socket.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    public static void main(String[] args) throws IOException {
//        TCPClient tcpClient = new TCPClient("127.0.0.1" ,"8008");
//        tcpClient.send("hello");//发送一串字符
//        //接收服务器返回的字符串并显示
//        System.out.println(tcpClient.receive());
//    }
//}

package chapter08;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;

public class HTTPSClient {
    private SSLSocket socket; //定义套接字
    private SSLSocketFactory factory;
    //定义字符输入流和输出流
    private PrintWriter pw;
    private BufferedReader br;

    public HTTPSClient(String ip, String port) throws IOException {
        //主动向服务器发起连接，实现TCP的三次握手过程
        //如果不成功，则抛出错误信息，其错误信息交由调用者处理
        factory=(SSLSocketFactory)SSLSocketFactory.getDefault();
        socket=(SSLSocket)factory.createSocket(ip,Integer.parseInt(port));

        //得到网络输出字节流地址，并封装成网络输出字符流
        OutputStream socketOut = socket.getOutputStream();
        pw = new PrintWriter( // 设置最后一个参数为true，表示自动flush数据
                new OutputStreamWriter(//设置utf-8编码
                        socketOut, "utf-8"), true);

        //得到网络输入字节流地址，并封装成网络输入字符流
        InputStream socketIn = socket.getInputStream();
        br = new BufferedReader(
                new InputStreamReader(socketIn, "utf-8"));
    }

    public void send(String msg) {
        //输出字符流，由Socket调用系统底层函数，经网卡发送字节流
        pw.println(msg);
    }

    public String receive() {
        String msg = null;
        try {
            //从网络输入字符流中读信息，每次只能接受一行信息
            //如果不够一行（无行结束符），则该语句阻塞，
            // 直到条件满足，程序才往下运行
            msg = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
    public void close() {
        try {
            if (socket != null) {
                //关闭socket连接及相关的输入输出流,实现四次握手断开
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //本机模块内测试与运行，需先运行TCPServer
    public static void main(String[] args) throws IOException {
        HTTPSClient tcpClient = new HTTPSClient("127.0.0.1" ,"8008");
        tcpClient.send("hello");//发送一串字符
        //接收服务器返回的字符串并显示
        System.out.println(tcpClient.receive());
    }

}