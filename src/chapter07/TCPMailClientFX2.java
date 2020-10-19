package chapter07;

import chapter01.TextFileIO;
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

public class TCPMailClientFX2 extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");
    private Button btnConnect = new Button("连接");
    //待发送信息的文本框
    private TextField tfSend = new TextField();
    private TextField tfip = new TextField("smtp.qq.com");
    private TextField tfport = new TextField("25");
    private TextField tfip1 = new TextField();
    private TextField tfport1 = new TextField();
    private TextField mtitle = new TextField();
    //显示信息的文本区域
    private TextArea taDisplay = new TextArea();
    private TextFileIO textFileIO = new TextFileIO();
    private TCPClient tcpClient;
    Thread readThread;
    BASE64 base64;

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
        tophbox.getChildren().addAll(new Label("邮件服务器地址："),tfip,new Label("邮件服务器端口："),tfport);
        HBox tophbox1 = new HBox();
        tophbox1.setSpacing(10);
        tophbox1.setPadding(new Insets(10,20,10,20));
        tophbox1.setAlignment(Pos.CENTER);
        tophbox1.getChildren().addAll(new Label("邮件发送者地址："),tfip1,new Label("邮件发送者端口："),tfport1);
        HBox tophbox2 = new HBox();
        tophbox2.setSpacing(10);
        tophbox2.setPadding(new Insets(10,20,10,20));
        tophbox2.setAlignment(Pos.CENTER);
        mtitle.setPrefWidth(470);
        tophbox2.getChildren().addAll(new Label("邮件标题："),mtitle);
        //内容显示区域
        VBox vBox = new VBox();
        vBox.setSpacing(10);//各控件之间的间隔
        //VBox面板中的内容距离四周的留空区域
        vBox.setPadding(new Insets(10, 20, 10, 20));
        vBox.getChildren().addAll(tophbox,tophbox1,tophbox2,new Label("信息显示区："),
                taDisplay);
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
                tcpClient = new TCPClient(ip,port);
                // 多线程不需要这一条了
//                String firstMsg = tcpClient.receive();
//                taDisplay.appendText(firstMsg+"\n");
                btnSend.setDisable(false);
                btnConnect.setDisable(true);
                //多线程方法

                readThread = new Thread(()->{
                    String msg = null;
                    while((msg = tcpClient.receive())!=null){
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
            tcpClient.send(sendMsg);//向服务器发送一串字符
            taDisplay.appendText("客户端发送：" + sendMsg + "\n");
            //注释掉这句话，和线程不冲突，不会卡死。
//            String receiveMsg = tcpClient.receive();//从服务器接收一行字符
//            taDisplay.appendText(receiveMsg + "\n");
            tfSend.clear();
        });

    }

    private void endSystem() {
        if(tcpClient != null){
            //向服务器发送关闭连接的约定信息
            tcpClient.send("bye");
            tcpClient.close();
        }
        System.exit(0);
    }
}