package chapter06;

import chapter01.TextFileIO;
import chapter06.UDPClient;
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

import java.io.IOException;

public class UDPClientFX extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");
    private Button btnConnect = new Button("连接");
    //待发送信息的文本框
    private TextField tfSend = new TextField();
    private TextField tfip = new TextField("127.0.0.1");
    private TextField tfport = new TextField("8008");
    //显示信息的文本区域
    private TextArea taDisplay = new TextArea();
    private TextFileIO textFileIO = new TextFileIO();
    private UDPClient udpClient;
    Thread readThread;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        // 顶部输入
        HBox tophbox = new HBox();
        tophbox.setSpacing(10);
        tophbox.setPadding(new Insets(10,20,10,20));
        tophbox.setAlignment(Pos.CENTER);
        tophbox.getChildren().addAll(new Label("IP地址："),tfip,new Label("端口："),tfport,btnConnect);
        //内容显示区域
        VBox vBox = new VBox();
        vBox.setSpacing(10);//各控件之间的间隔
        //VBox面板中的内容距离四周的留空区域
        vBox.setPadding(new Insets(10, 20, 10, 20));
        vBox.getChildren().addAll(tophbox,new Label("信息显示区："),
                taDisplay, new Label("信息输入区："), tfSend);
        //设置显示信息区的文本区域可以纵向自动扩充范围
        VBox.setVgrow(taDisplay, Priority.ALWAYS);
        mainPane.setCenter(vBox);
        //底部按钮区域
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 20, 10, 20));
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnSend,  btnExit);
        mainPane.setBottom(hBox);
        Scene scene = new Scene(mainPane, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        btnSend.setDisable(true);
        btnConnect.setOnAction(event -> {
            String ip = tfip.getText().trim();
            String port = tfport.getText().trim();
            try {
                udpClient = new UDPClient(ip,port);
//                String firstMsg = udpClient.receive();
//                taDisplay.appendText(firstMsg+"\n");
                btnSend.setDisable(false);
                btnConnect.setDisable(true);
                //多线程方法
                readThread = new Thread(()->{
                    String msg = null;
                    while((msg = udpClient.receive())!=null){
                        String msgTemp = msg;
                        Platform.runLater(()->{
                            taDisplay.appendText(msgTemp+"\n");
                        });
                    }
                    Platform.runLater(()->{
                        taDisplay.appendText("对话已关闭！\n");
                    });
                });
                readThread.start();
            } catch (IOException e) {
                taDisplay.appendText("服务器连接失败"+e.getMessage()+"\n");
                btnSend.setDisable(true);
            }
        });

        btnExit.setOnAction(event -> {
            endSystem();
        });
        primaryStage.setOnCloseRequest(event -> {
            endSystem();
        });
        btnSend.setOnAction(event -> {
            String sendMsg = tfSend.getText();
            if(sendMsg.equals("bye")) {
                btnConnect.setDisable(false);
                btnSend.setDisable(true);
            }
            udpClient.send(sendMsg);//向服务器发送一串字符
            taDisplay.appendText("客户端发送：" + sendMsg + "\n");
            //注释掉这句话，和线程不冲突，不会卡死。
//            String receiveMsg = udpClient.receive();//从服务器接收一行字符
//            taDisplay.appendText(receiveMsg + "\n");
            tfSend.clear();
        });

    }

    private void endSystem() {
        if(udpClient != null){
            //向服务器发送关闭连接的约定信息
            udpClient.send("bye");
            udpClient.close();
        }
        System.exit(0);
    }
}
