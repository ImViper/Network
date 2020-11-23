package chapter12.server;

import rmi.RmiKitService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/23
 */
public class RmiKitServiceImpl extends UnicastRemoteObject implements RmiKitService {

    protected RmiKitServiceImpl() throws RemoteException {
    }

    @Override
    public long ipToLong(String ip) throws RemoteException {
        String[] s = ip.split("\\.");
        long ip1 = (Long.parseLong(s[0]) << 24)
                + (Long.parseLong(s[1]) << 16) +
                (Long.parseLong(s[2]) << 8)
                + (Long.parseLong(s[3]));
        return ip1;
    }

    @Override
    public String LongtoIp(long ipNum) throws RemoteException {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf(ipNum>>24)).append(".").
                append(String.valueOf((ipNum&0x00ffffff)>>16)).append(".").
                append(String.valueOf((ipNum&0x0000ffff)>>8)).append(".").
                append(String.valueOf(ipNum&0x000000ff));
        return sb.toString();
    }

    @Override
    public byte[] macStringToBytes(String macStr) throws RemoteException {
        byte[] mac =new byte[6];
        if(macStr.contains("-") || macStr.contains(":")){
            String[] temp=macStr.split(":|-");
            for (int i=0;i<6;i++
            ) {
                mac[i] = (byte)Integer.parseInt(temp[i],16);
            }
        }
        return mac;
    }

    @Override
    public String bytesToMacString(byte[] macBytes) throws RemoteException {
        String macStr="";
        for(int i=0;i<macBytes.length;i++){
            macStr+=Integer.toHexString(macBytes[i]&0xFF);
            if(i<(macBytes.length-1)){
                macStr+="-";
            }
        }
        return macStr;
    }
}
