package chapter09;

import chapter05.TCPThreadServer;
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
import org.omg.PortableInterceptor.HOLDING;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Author:蔡诚杰
 * Data:2020/11/1
 * description:
 */
public class PortScannerFX extends Application {
    private TextField tfip = new TextField();
    private TextField tfstart = new TextField();
    private TextField tfend= new TextField();
    private TextArea taResult = new TextArea();
    private Button btnScan = new Button("扫描");
    private Button btnScanFast = new Button("快速扫描");
    private Button btnScanThread = new Button("多线程扫描");
    private Button btnexit = new Button("退出");
    private Button btnshut = new Button("停止扫描");
    private int portstart;
    private int portend;
    private String host;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    Thread thread;
    Thread thread1;
    static AtomicInteger portCount = new AtomicInteger(0);
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainpane = new BorderPane();
        HBox tophbox = new HBox();
        tophbox.setSpacing(10);
        tophbox.setPadding(new Insets(10,20,10,20));
        tophbox.setAlignment(Pos.CENTER);
        tophbox.getChildren().addAll(new Label("目标主机ip:"),tfip,new Label("起始端口号"),tfstart,new Label("结束端口号"),tfend);

        HBox tophbox1 = new HBox();
        tophbox1.setSpacing(10);
        tophbox1.setPadding(new Insets(10,20,10,20));
        tophbox1.setAlignment(Pos.CENTER);
        tophbox1.getChildren().addAll(btnScan,btnScanFast,btnScanThread,btnshut,btnexit);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(new Label("端口扫描结果"),taResult);
        //设置文本框宽度
        VBox.setVgrow(taResult, Priority.ALWAYS);
        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(10,20,10,20));
        vBox1.setSpacing(10);
        vBox1.getChildren().addAll(tophbox,tophbox1);
        tfip.setPrefWidth(150);
        mainpane.setCenter(vBox);
        mainpane.setBottom(vBox1);
        Parent root;
        Scene scene = new Scene(mainpane,800,500);
        primaryStage.setScene(scene);
        primaryStage.show();

        btnScan.setOnAction(event -> {
            thread.interrupt();
            String host = tfip.getText();
            thread = new Thread(()->{
                int portstart = Integer.parseInt(tfstart.getText());
                int portend = Integer.parseInt(tfend.getText());
                while (portstart <= portend) {
                    try {
                        Socket socket = new Socket(host,portstart);
                        socket.close();
                        String msg = "端口 " + portstart + " is Open! \r\n";
                        taResult.appendText(msg);
                    } catch(IOException e){
//                   e.printStackTrace();
                        String msg = "端口 " + portstart + " is Closed! \r\n";
                        taResult.appendText(msg);
                    }
                    portstart++;
                }
            });
            thread.start();
        });

        btnScanFast.setOnAction(event -> {
            thread1.interrupt();
            host = tfip.getText();
            thread1 = new Thread(()->{
                 portstart = Integer.parseInt(tfstart.getText());
                 portend = Integer.parseInt(tfend.getText());
                while (portstart <= portend) {
                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(host, portstart), 200);
                        socket.close();
                        String msg = "端口 " + portstart + " is Open! \r\n";
                        taResult.appendText(msg);
                    } catch(IOException e){
//                   e.printStackTrace();
                        String msg = "端口 " + portstart + " is Closed! \r\n";
                        taResult.appendText(msg);
                    }
                    portstart++;
                }
            });
            thread1.start();
        });

        btnScanThread.setOnAction(event -> {
            executorService.shutdown();
            host = tfip.getText();
            portstart = Integer.parseInt(tfstart.getText());
            portend = Integer.parseInt(tfend.getText());
            for (int i = 0; i < 10; i++) {
                executorService.execute(new ScanHandler(i));
            }
        });
        btnexit.setOnAction(event -> {
            System.exit(0);
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

    class ScanHandler implements Runnable{
        private int totalThreadNum;
        private int threadNo;

        public ScanHandler(int threadNo){

            this.totalThreadNum =10;
            this.threadNo = threadNo;
        }

        public ScanHandler(int totalThreadNum, int threadNo) {
            this.totalThreadNum = totalThreadNum;
            this.threadNo = threadNo;
        }



        @Override
        public void run() {
            for (int port = portstart + threadNo;port<=portend;port=port+totalThreadNum){
                try{
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(host,port),200);
                    socket.close();
                    String msg = "端口 " + port + "is open \n";
                    Platform.runLater(()->{
                        taResult.appendText(msg);
                    });
                }catch (IOException e){
                    String msg = "端口 " + port + " is closed \n";
                    Platform.runLater(()->{
                        taResult.appendText(msg);
                    });
                    e.printStackTrace();
                }
                portCount.incrementAndGet();
            }
            if(portCount.get()==(portend-portstart+1)){
                portCount.incrementAndGet();
                Platform.runLater(()->{
                    taResult.appendText("-----多线程扫描结束-------");
                });
            }
        }
    }




//    public static void main(String[] args) {
//        System.out.println(StringIPAddOne("192.168.1.255"));
//        System.out.println(ipToInt("192.168.1.255"));
//        System.out.println(longToIp(-1062731264));
//    }
}
