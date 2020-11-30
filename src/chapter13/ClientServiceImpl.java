package chapter13;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/30
 */
public class ClientServiceImpl extends UnicastRemoteObject implements ClientService {
    private RmiClientFX rmiClientFX;
    public ClientServiceImpl(RmiClientFX rmiClientFX)throws RemoteException{
        this.rmiClientFX = rmiClientFX;
    }

    @Override
    public void showMsgToClient(String msg) throws RemoteException {
        rmiClientFX.appendMsg(msg);
    }
}
