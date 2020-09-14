package chapter02;

import java.io.*;
import java.net.Socket;

public class TCPClient {
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;

    public TCPClient(String ip,String port) throws IOException {
        socket = new Socket(ip, Integer.parseInt(port));
        OutputStream socketOut = socket.getOutputStream();
        pw = new PrintWriter(
                new OutputStreamWriter(socketOut, "utf-8"), true);
        InputStream socketIn = socket.getInputStream();
        br = new BufferedReader(
                new InputStreamReader(socketIn, "utf-8"));
    }
        public void send(String msg) {
            pw.println(msg);
        }
        public String receive(){
            String msg = null;
            try {
                msg=br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return msg;
        }
        public void close(){
            try {
                if(socket != null){
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public static void main(String[] args) throws IOException {
        TCPClient tcpClient = new TCPClient("127.0.0.1" ,"8008");
        tcpClient.send("hello");//发送一串字符
        //接收服务器返回的字符串并显示
        System.out.println(tcpClient.receive());
    }
}
