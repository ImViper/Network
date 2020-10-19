package chapter07;

/**
 * Author:蔡诚杰
 * Data:2020/10/19
 * description:
 */
public class BASE64 {
    public static void main(String[] args) {
        String userName="756627124@qq.com";
        String authCode = "rbrxsegwmwjgbbge";
        System.out.println(encode(userName));
        System.out.println(encode(authCode));
    }

    public static String  encode(String str) {
        return new sun.misc.BASE64Encoder().encode(str.getBytes());
    }
}
