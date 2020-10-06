package chapter05;

import java.net.Socket;
import java.util.Stack;

public class Test {
    public static void main(String[] args) {
        String msg = "发送给1-1，1-2:ssa";
        System.out.println(msg.indexOf("，"));
        Stack stack = new Stack();
        Socket getsocket;
        msg=msg.substring(3);
        while(msg.indexOf("，")>=0){
            int r = msg.indexOf(",");
            String name=msg.substring(0,r);
            msg=msg.substring(r+1);
        }
        int r = msg.indexOf(":");
        String name=msg.substring(0,r);
    }
}
