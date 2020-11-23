package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/23
 */
public interface RmiMsgService extends Remote {
    public String send(String msg) throws RemoteException;
    public String send(String yourNo,String yourName) throws RemoteException;

}
