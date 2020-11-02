package chapter09;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
import java.net.InetAddress;


/**
 * Author:蔡诚杰
 * Data:2020/11/1
 * description:
 */
public class HostScannerFX extends Application {
    private TextField tfcommand = new TextField();
    private TextField tfstart = new TextField();
    private TextField tfend= new TextField();
    private TextArea taResult = new TextArea();
    private Button btnScan = new Button("主机扫描");
    private Button btnExcu = new Button("执行命令");
    private Button btnshut = new Button("停止扫描");
    Thread thread;
    Thread thread1;
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainpane = new BorderPane();
        HBox tophbox = new HBox();
        tophbox.setSpacing(10);
        tophbox.setPadding(new Insets(10,20,10,20));
        tophbox.setAlignment(Pos.CENTER);
        tophbox.getChildren().addAll(new Label("起始地址"),tfstart,new Label("结束地址"),tfend,btnScan,btnshut);

        HBox tophbox1 = new HBox();
        tophbox1.setSpacing(10);
        tophbox1.setPadding(new Insets(10,20,10,20));
        tophbox1.setAlignment(Pos.CENTER);
        tophbox1.getChildren().addAll(new Label("输入命令格式"),tfcommand,btnExcu);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(new Label("扫描结果"),taResult);
        //设置文本框宽度
        VBox.setVgrow(taResult, Priority.ALWAYS);
        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(10,20,10,20));
        vBox1.setSpacing(10);
        vBox1.getChildren().addAll(tophbox,tophbox1);
        tfcommand.setPrefWidth(500);
        mainpane.setCenter(vBox);
        mainpane.setBottom(vBox1);
        Parent root;
        Scene scene = new Scene(mainpane,800,500);
        primaryStage.setScene(scene);
        primaryStage.show();

        btnScan.setOnAction(event -> {
            String ipst = tfstart.getText();
            String ipen = tfend.getText();
            thread = new Thread(()->{
                try{
                    int ipstart = ipToInt(tfstart.getText());
                    int ipend = ipToInt(tfend.getText());
                    int timeout = 100;
                    String host = ipst;
                    while(ipstart<=ipend){
                        InetAddress addr = InetAddress.getByName(host);
                        boolean status = addr.isReachable(timeout);
                        if(status == true){
                            taResult.appendText(host+" reachable \r\n");
                        }else{
                            taResult.appendText(host+" is not reachable \r\n");
                        }
                        host = longToIp(++ipstart);
                    }

                    Platform.runLater(()->{
                        taResult.appendText("结束扫描"+"\n");
                    });

                }catch (IOException e){
                    e.printStackTrace();
                }
            });
            thread.start();
        });

        btnExcu.setOnAction(event -> {
            thread1 = new Thread(()->{
                try {
                    String cmd = tfcommand.getText();
                    Process process = Runtime.getRuntime().exec(cmd);
                    InputStream in = process.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in,"GBK"));
                    String msg;
                    while((msg = br.readLine())!=null){



                        String msgtemp= msg;
                        Platform.runLater(()->{
                            taResult.appendText(msgtemp+"\n");
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread1.start();
        });

    }

    public static int ipToInt(String ip){
        String[] ipArray =  ip.split("\\.");
        int num=0;
        for(int i=0;i<ipArray.length;i++){
            int valueOfSection = Integer.parseInt(ipArray[i]);
            num = (valueOfSection << 8*(3-i)) | num;
        }
        return num;
    }

    public static String longToIp(long ip) {
        StringBuilder result = new StringBuilder(15);

        for (int i = 0; i < 4; i++) {

            result.insert(0,Long.toString(ip & 0xff));

            if (i < 3) {
                result.insert(0,'.');
            }

            ip = ip >> 8;
        }
        return result.toString();
    }



//    public static void main(String[] args) {
//        System.out.println(StringIPAddOne("192.168.1.255"));
//        System.out.println(ipToInt("192.168.1.255"));
//        System.out.println(longToIp(-1062731264));
//    }
}
