package chapter02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

public class TCPServer {
    private  int port = 8008;
    private ServerSocket serverSocket;
    public TCPServer() throws IOException{
        serverSocket = new ServerSocket(8008);
        System.out.println("服务器的启动监听在" + port + "端口");
    }

    private PrintWriter getWriter (Socket socket) throws  IOException{
        OutputStream socketOut =  socket.getOutputStream();
        return new PrintWriter(new OutputStreamWriter(socketOut,"utf-8"),true);
    }
    private BufferedReader getReader(Socket socket) throws IOException{
        InputStream socketIn = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(socketIn,"utf-8"));
    }

    public void Service(){
        while(true){
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                System.out.println("New connection accepted:"+socket.getInetAddress());
                BufferedReader br = getReader(socket);
                PrintWriter pw = getWriter(socket);
                pw.println("From服务器，欢迎使用本服务");
                String msg = null;
                while ((msg=br.readLine())!=null){
                    if (msg.equals("bye")) {
                        pw.println("From服务器：服务器断开连接，结束服务！");
                        System.out.println("客户端离开");
                        break;
                    }
                    pw.println("From服务器"+msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try{
                    if(socket!=null)
                        socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new TCPServer().Service();
    }

}
