package chapter13;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Author:蔡诚杰
 * Data:2020/11/30
 * description:
 */
public class ServerServicesServer {
    public static void main(String[] args) {
        int port = 8008;

        try {
            System.setProperty("java.rmi.server.hostname","127.0.0.1");
            Registry registry = LocateRegistry.createRegistry(port);
            ServerService serverService = new ServerServiceImpl();
            registry.rebind("ServerService",serverService);
            System.out.println("服务器发布了ServerService远程服务");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
