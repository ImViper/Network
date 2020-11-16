package chapter10;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;


/**
 * Author:蔡诚杰
 * Data:2020/11/15
 * description:
 */
public class TestIp {
    public static void main(String[] args) {
//        String keyData ="fuck you";
//        String[] keyList = keyData.split('\s+');
//        for (String key : keyList) {
//            System.out.println(key);
//        }
//    }


        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        for (int i = 0; i < devices.length; i++) {
            System.out.println(i+ ":" + devices[i].name+devices[i].description);
            String mac = "";
            for (byte b : devices[i].mac_address){
                mac = mac + Integer.toHexString(b & 0xff) + ":";
            }
            System.out.println("Mac address:"+mac.substring(0,mac.length()-1));
            for (NetworkInterfaceAddress addr : devices[i].addresses
                 ) {
                System.out.println("address" + addr.address + " " + addr.subnet+" " + addr.broadcast);
            }
        }
    }

}
