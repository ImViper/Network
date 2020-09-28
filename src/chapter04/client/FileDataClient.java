package chapter04.client;

import java.io.*;
import java.net.Socket;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/9/28
 */
public class FileDataClient {
    Socket dataSocket;
    FileDataClient(String ip,String port) throws IOException {
        dataSocket = new Socket(ip, Integer.parseInt(port));
        //得到网络输出字节流地址，并封装成网络输出字符流

    }

    public void getFile(File saveFile)throws IOException{
        if(dataSocket !=null){
            FileOutputStream fileOut = new FileOutputStream(saveFile);
            byte[] buf = new byte[1024];
            InputStream socketIn = dataSocket.getInputStream();
            OutputStream socketOut = dataSocket.getOutputStream();

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socketOut,"utf-8"),true);
            pw.println(saveFile.getName());

            int size=0;
            while((size=socketIn.read(buf))!=-1){
                fileOut.write(buf,0,size);
            }
            fileOut.flush();
            fileOut.close();
            if(dataSocket != null) {
                dataSocket.close();
            }
        }else{
            System.out.println("连接ftp服务器失败");
        }
    }
}
