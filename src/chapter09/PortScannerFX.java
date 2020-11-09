package chapter09;

import chapter05.TCPThreadServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private TextField tfip = new TextField("192.168.0.1");
    private TextField tfstart = new TextField("1");
    private TextField tfend= new TextField("200");
    private TextArea taResult = new TextArea();
    private Button btnScan = new Button("扫描");
    private Button btnStop = new Button("停止扫描");
    private Button btnScanFast = new Button("快速扫描");
    private Button btnScanThread = new Button("多线程扫描");
    private Button btnexit = new Button("退出");
    private int portstart;
    private int portend;
    private String host;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    Thread thread;
    Thread thread1;
    static AtomicInteger portCount = new AtomicInteger(0);
    private ThreadGroup threadGroup = new ThreadGroup("scanThread");
    private ProgressBar progressBar = new ProgressBar(0);
    private Label progressLabel = new Label("0%");

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
        tophbox1.getChildren().addAll(btnScan,btnScanFast,btnScanThread,btnStop,btnexit);

        HBox progressbox = new HBox();
        progressbox.setAlignment(Pos.CENTER);
        progressbox.setSpacing(10);
        progressbox.getChildren().addAll(progressLabel,progressBar);
        progressBar.setPrefWidth(600);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(new Label("端口扫描结果"),taResult);
        //设置文本框宽度
        VBox.setVgrow(taResult, Priority.ALWAYS);
        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(10,20,10,20));
        vBox1.setSpacing(10);
        vBox1.getChildren().addAll(tophbox,progressbox,tophbox1);
        tfip.setPrefWidth(150);
        mainpane.setCenter(vBox);
        mainpane.setBottom(vBox1);

        Scene scene = new Scene(mainpane,800,500);
        primaryStage.setScene(scene);
        primaryStage.show();

        btnScan.setOnAction(event -> {
            progressBar.setProgress(0);
            progressLabel.setText("0%");
            String host = tfip.getText();
            thread = new Thread(()->{
                int portstart = Integer.parseInt(tfstart.getText());
                int portend = Integer.parseInt(tfend.getText());
                int totalport = portend-portstart+1;
                int startp=portstart;
                while (portstart <= portend) {
                    System.out.println("scaning port"+portstart);
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
                    double progress = (double)(portstart-startp)/totalport;
                    Platform.runLater(()->{
                        progressBar.setProgress(progress);
                        progressLabel.setText((int)(progress*100)+"%");
                    });
                    portstart++;
                }
            });
            thread.start();
        });

        btnScanFast.setOnAction(event -> {
            progressBar.setProgress(0);
            progressLabel.setText("0%");
            host = tfip.getText();
            thread1 = new Thread(()->{
                 portstart = Integer.parseInt(tfstart.getText());
                 portend = Integer.parseInt(tfend.getText());
                int totalport = portend-portstart+1;
                 int startp=portstart;
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
                    double progress = (double)(portstart-startp)/totalport;
                    Platform.runLater(()->{
                        progressBar.setProgress(progress);
                        progressLabel.setText((int)(progress*100)+"%");
                    });
                    portstart++;
                }
            });
            thread1.start();
        });

        btnScanThread.setOnAction(event -> {
            host = tfip.getText();
            this.portstart = Integer.parseInt(tfstart.getText());
            this.portend = Integer.parseInt(tfend.getText());


            int thread = 4;
            portCount.set(0);
            for (int i = 0; i < thread; i++) {
                ScanHandler scanHandler = new ScanHandler(i, thread);
                new Thread(threadGroup, scanHandler, "MultiThread" + i).start();
            }
        });
        btnStop.setOnAction(event -> {
            btnStop.setDisable(true);
            btnScan.setDisable(false);
            btnScanFast.setDisable(false);
            btnScanThread.setDisable(false);
            try {
                threadGroup.list();
                threadGroup.interrupt();
            } catch (Exception e) {
            }

        });
        btnexit.setOnAction(event -> {
            try {
                threadGroup.interrupt();
            } catch (Exception e) {
            }
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
                double progress = (double) portCount.get() / (portend - portstart + 1);
                Platform.runLater(() -> {
                    progressBar.setProgress(progress);
                    progressLabel.setText((int)(progress * 100) + "%");
                });
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
