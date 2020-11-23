package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/23
 */
public interface RmiKitService extends Remote {
    public long ipToLong(String ip)throws RemoteException;
    public String LongtoIp(long ipNum) throws RemoteException ;
    public byte[] macStringToBytes(String macStr) throws RemoteException;
    public String bytesToMacString(byte[] macBytes)throws RemoteException;

}
