package chapter06;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;

/**
 * Author:蔡诚杰
 * Data:2020/10/10
 * description:
 */
public class UDPServer {
    private DatagramSocket server;
    private DatagramPacket packet;
    byte[] buffer =  new byte[512];
    public UDPServer() throws IOException {
         server = new DatagramSocket(8008);
        packet = new DatagramPacket(buffer,buffer.length);
//
        System.out.println("服务器开始运行");
    }
    public void Runserver() throws IOException {
        while(true){
            System.out.println("等待消息");
            server.receive(packet);
            System.out.println("接收到信息");
            String msg = new String(packet.getData(),packet.getOffset(),packet.getLength(),"utf-8");
            String remsg = "20181002946&蔡诚杰&"+new Date().toString()+"&"+msg;
            byte[] outPutData = remsg.getBytes("utf-8");
            DatagramPacket outputPacket = new DatagramPacket(outPutData,outPutData.length,packet.getAddress(),packet.getPort());
            server.send(outputPacket);
            System.out.println("完成发送");
        }
    }

    public static void main(String[] args) throws IOException {
        UDPServer udpServer = new UDPServer();
        udpServer.Runserver();
    }
}
