package chapter13;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/30
 */
public interface ServerService extends Remote {
    public String addClientToOnlineGroup(String client,ClientService clientService)throws RemoteException;
    public String removeClientFromOnlineGroup(String client,ClientService clientService) throws RemoteException;
    public void sendPublicMsgToServer(String client,String msg) throws RemoteException;

}
