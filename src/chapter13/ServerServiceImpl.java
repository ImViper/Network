package chapter13;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/30
 */
public class ServerServiceImpl extends UnicastRemoteObject implements ServerService {
    private static ConcurrentHashMap<String,ClientService> onlineGroup = new ConcurrentHashMap<>();
    protected ServerServiceImpl() throws RemoteException
    {
    }
    @Override
    public String addClientToOnlineGroup(String client, ClientService clientService) throws RemoteException {
        if(client == null)
            return "From 服务器，学号姓名信息为空";

        if(client.split("-").length !=2)
            return "From 服务器，学号姓名格式不正确";
        boolean isLogin = false;
        isLogin=onlineGroup.contains(clientService);
        if(!isLogin){
            onlineGroup.put(client.trim(),clientService);
            isLogin = true;
            sendPublicMsgToServer(client,"加入到群聊！");
            return "From 服务器:" + client.trim() + "加入到群聊！";
        }
        else{
            return "From 服务器: 不能反复登录";
        }

    }

    @Override
    public String removeClientFromOnlineGroup(String client, ClientService clientService) throws RemoteException {
        if(onlineGroup.contains(clientService)){
            onlineGroup.remove(clientService);
        }
        sendPublicMsgToServer(client, "退出群聊！");
        return "From 服务器：" + client.trim() + " 退出群聊！";
    }

    @Override
    public void sendPublicMsgToServer(String client, String msg) throws RemoteException {
        if(msg != null){
            for(String onlineUser : onlineGroup.keySet())
            {
                ClientService clientService = onlineGroup.get(onlineUser);
                if(clientService != null){

                }
            }
        }
    }
}
