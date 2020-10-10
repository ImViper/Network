package chapter06;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/10/5
 */
public class UDPClient {
    private int remotePort;
    private InetAddress remoteIP;
    private DatagramSocket socket;
    private static final int MAX_PACKET_SIZE=512;

    public UDPClient(String remoteIP,String remotePort)throws IOException{
        this.remoteIP = InetAddress.getByName(remoteIP);
        this.remotePort = Integer.parseInt(remotePort);

        socket = new DatagramSocket();

    }
    public void send(String msg){
        try {
            byte[] outData = msg.getBytes("utf-8");
            DatagramPacket outPacket = new DatagramPacket(outData,outData.length,remoteIP,remotePort);
            socket.send(outPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String receive(){
        String msg;
        DatagramPacket inPacket = new DatagramPacket(new byte[MAX_PACKET_SIZE],MAX_PACKET_SIZE);

        try {
            socket.receive(inPacket);
            msg=new String(inPacket.getData(),0, inPacket.getLength(),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            msg=null;
        }
        return msg;
    }

    public void close() {
        if (socket != null) {
            socket.close();
        }
    }
}
