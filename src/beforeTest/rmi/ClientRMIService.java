package beforeTest.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ClientRMIService extends Remote {
    public String reportResult(String msg) throws RemoteException;
}
