package chapter12.server;

import rmi.HelloService;
import rmi.RmiKitService;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/23
 */
public class RmiStudentServer {
    public static void main(String[] args) {
        try{
            Registry registry = LocateRegistry.createRegistry(1099);
            RmiKitService rmiKitService = new RmiKitServiceImpl();
            registry.bind("RmiKitService",rmiKitService);
            System.out.println("发布了一个RmiKitService RMI远程服务");

        }catch (RemoteException | AlreadyBoundException e){
            e.printStackTrace();
        }
    }
}
