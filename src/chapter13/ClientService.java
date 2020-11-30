package chapter13;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/30
 */
public interface ClientService extends Remote {
    public void showMsgToClient(String msg) throws RemoteException;

}
