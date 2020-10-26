package chapter08;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Author:蔡诚杰
 * Data:2020/10/24
 * description:
 */
public class URLClientFX extends Application {
    private Button btnClose = new Button("退出");
    private Button btnClear = new Button("清空");
    private Button btnHttp = new Button("发送");
    private TextField tfUrl = new TextField();
    private TextArea taDisplay = new TextArea();
    private HTTPClient httpClient;
    Thread readThread;
    BufferedReader br;
    @Override
    public void start(Stage primaryStage){
        BorderPane mainpane = new BorderPane();
        taDisplay.setWrapText(true);
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(new Label("网页信息显示区"),taDisplay,new Label("输入URL地址"),tfUrl);
        VBox.setVgrow(taDisplay, Priority.ALWAYS);
        HBox buttomHbox = new HBox();
        buttomHbox.setPadding(new Insets(10,20,10,20));
        buttomHbox.setSpacing(10);
        buttomHbox.setAlignment(Pos.CENTER_RIGHT);
        buttomHbox.getChildren().addAll(btnHttp,btnClear,btnClose);
        mainpane.setCenter(vBox);
        mainpane.setBottom(buttomHbox);
        Scene scene = new Scene(mainpane,800,400);
        primaryStage.setScene(scene);
        primaryStage.show();


        btnClear.setOnAction(event -> {
            taDisplay.clear();
        });
        btnHttp.setOnAction(event -> {

            taDisplay.clear();
            try {
                String ip = tfUrl.getText();
                if (!isURL(ip)) {
                    taDisplay.appendText("当前url不合法");
                } else {
                    URL url = new URL(ip);
                    System.out.println("连接成功！");
                    InputStream in = url.openStream();
                    br = new BufferedReader(new InputStreamReader(in, "utf-8"));
//                readThread=new Thread(){
//                    String msg =null;
//                    while((msg=br.readLine())!=null){
//                        taDisplay.appendText(msg.toString());
//                    }
//                };
                    readThread = new Thread(() -> {
                        String msg;
                        //不知道服务器有多少回传信息，就持续不断接收
                        //由于在另外一个线程，不会阻塞主线程的正常运行
                        try {
                            while ((msg = br.readLine()) != null) {
                                //lambda表达式不能直接访问外部非final类型局部变量
                                //所以这里使用了一个临时变量
                                String msgTemp = msg;
                                Platform.runLater(() -> {
                                    taDisplay.appendText(msgTemp + "\r\n");
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //跳出了循环，说明服务器已关闭，读取为null，提示对话关闭
                        Platform.runLater(() -> {
                            taDisplay.appendText("对话已关闭！\n");
                        });
                    });
                    readThread.start();

                    }
                }catch(IOException e) {
                e.printStackTrace();
            }
        });


    }
    public static boolean isURL(String str){
        //转换为小写
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"  //https、http、ftp、rtsp、mms
                + "(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 例如：199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,5})?" // 端口号最大为65535,5位数
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return  str.matches(regex);
    }

}
//dddedc838f88c450fc0001db1ec8eb85