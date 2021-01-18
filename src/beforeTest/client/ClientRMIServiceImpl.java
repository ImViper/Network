package beforeTest.client;

import beforeTest.rmi.ClientRMIService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ClientRMIServiceImpl extends UnicastRemoteObject implements ClientRMIService {
    public ClientRMIServiceImpl() throws RemoteException {
    }

    @Override
    public String reportResult(String msg) throws RemoteException
    {
        return "23";
    }
}
