package chapter11;

import jpcap.JpcapSender;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.TCPPacket;

import java.net.InetAddress;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/16
 */
public class PacketSender {
    /**
     * @param sender JpcapSender类型
     * @param srcPort 源端口
     * @param dstPort 目的端口
     * @param srcHost ip地址形式或类似 www.baidu.com的域名形式
     * @param dstHost
     * @param data 填充到tcp包中的数据
     * @param srcMAC 格式为"dc-8b-28-87-b9-82"或"dc:8b:28:87:b9:82"
     * @param dstMAC
     * @param syn 这几个为常用标识位
     * @param ack
     * @param rst
     * @param fin
     */
    public static void sendTCPPacket(JpcapSender sender,int srcPort,int dstPort,String srcHost,String dstHost,String data,String srcMAC,String dstMAC,boolean syn,boolean ack,boolean rst,boolean fin){
        try{
            //构造一个TCP包
            TCPPacket tcp = new TCPPacket(srcPort,dstPort,56,78,false,ack,false,rst,syn,fin,true,true,200,10);
            //设置IPv4报头参数，ip地址可以伪造
            tcp.setIPv4Parameter(0,false,false,false,0,false,false,false,0,1010101,100, IPPacket.IPPROTO_TCP, InetAddress.getByName(srcHost),
                    InetAddress.getByName (dstHost));
            //填充TCP包中的数据
            tcp.data= data.getBytes("utf-8");//字节数组型的填充数据
            //构造相应的MAC帧
//create an Ethernet packet (frame)
            EthernetPacket ether=new EthernetPacket();

//set frame type as IP
            ether.frametype=EthernetPacket.ETHERTYPE_IP;

//set the datalink frame of the tcp packet as ether
            tcp.datalink=ether;

//set source and destination MAC addresses
//MAC地址要转换成十进制，ipconfig /all 查看本机的MAC地址
//源地址是自己机器的MAC地址
            ether.src_mac=convertMacFormat(srcMAC);
//根据实际情况设置目的MAC地址， arp -a 可以查看相关的MAC地址
            ether.dst_mac=convertMacFormat(dstMAC);
            if(ether.src_mac == null || ether.dst_mac==null)
                throw new Exception("MAC地址输入错误");
            sender.sendPacket(tcp);
            System.out.println("发包成功！");
        }catch (Exception e){
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public static byte[] convertMacFormat(String MAC) {
        byte[] mac =new byte[6];
     if(MAC.contains("-") || MAC.contains(":")){
         String[] temp=MAC.split(":|-");
         for (int i=0;i<6;i++
              ) {
             mac[i] = (byte)Integer.parseInt(temp[i],16);
         }
     }
     return mac;
    }
}
