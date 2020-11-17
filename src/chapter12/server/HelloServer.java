package chapter12.server;

import rmi.HelloService;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/17
 */
public class HelloServer {
    public static void main(String[] args) {
        try{
            Registry registry = LocateRegistry.createRegistry(1099);
            HelloService helloService = new HelloServerImpl("viper的远程服务");

            registry.bind("HelloService",helloService);
            System.out.println("发布了一个HelloService RMI远程服务");

        }catch (RemoteException | AlreadyBoundException e){
            e.printStackTrace();
        }
    }
}
