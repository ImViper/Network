package rmi;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;


/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/17
 */
public interface HelloService extends Remote {
    public String echo(String msg)throws RemoteException;
    public Date getTime() throws RemoteException;
}
