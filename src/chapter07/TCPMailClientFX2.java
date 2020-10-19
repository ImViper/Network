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
    private TextField tfSmtpAddr = new TextField("smtp.qq.com");
    private TextField tfSmtpPort = new TextField("25");
    private TextField tfSenderAddr = new TextField();
    private TextField tfRecieverAddr = new TextField();
    private TextField mtitle = new TextField();
    //显示信息的文本区域
    private TextArea taMainText = new TextArea();
    private TextArea taSerMsg = new TextArea();
    private TextFileIO textFileIO = new TextFileIO();
    private TCPMailClient tcpMailClient;
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
        tophbox.getChildren().addAll(new Label("邮件服务器地址："), tfSmtpAddr,new Label("邮件服务器端口："), tfSmtpPort);
        HBox tophbox1 = new HBox();
        tophbox1.setSpacing(10);
        tophbox1.setPadding(new Insets(10,20,10,20));
        tophbox1.setAlignment(Pos.CENTER);
        tophbox1.getChildren().addAll(new Label("邮件发送者地址："), tfSenderAddr,new Label("邮件发送者端口："), tfRecieverAddr);
        HBox tophbox2 = new HBox();
        tophbox2.setSpacing(10);
        tophbox2.setPadding(new Insets(10,20,10,20));
        tophbox2.setAlignment(Pos.CENTER);
        mtitle.setPrefWidth(470);
        tophbox2.getChildren().addAll(new Label("邮件标题："),mtitle);
        //内容显示区域
        VBox InVbox = new VBox();
        InVbox.setSpacing(10);
        InVbox.setPadding(new Insets(10, 20, 10, 20));
        InVbox.getChildren().addAll(new Label("邮件正文："),taMainText);
        InVbox.prefHeightProperty().bind(primaryStage.heightProperty());

        VBox InVbox1 = new VBox();
        InVbox1.setSpacing(10);
        InVbox1.setPadding(new Insets(10, 20, 10, 20));
        InVbox1.getChildren().addAll(new Label("服务器反馈信息："),taSerMsg);
        taMainText.setWrapText(true);
        taSerMsg.setWrapText(true);
        taMainText.heightProperty();
        VBox.setVgrow(taMainText, Priority.ALWAYS);
        VBox.setVgrow(taSerMsg, Priority.ALWAYS);
        HBox mainhbox=new HBox();
        mainhbox.setSpacing(10);
        mainhbox.setPadding(new Insets(10,20,10,20));
        mainhbox.setAlignment(Pos.CENTER);
        mainhbox.getChildren().addAll(InVbox,InVbox1);
//        HBox.setHgrow(InVbox,Priority.ALWAYS);
//        HBox.setHgrow(InVbox1,Priority.ALWAYS);


        VBox vBox = new VBox();
        vBox.setSpacing(10);//各控件之间的间隔
        //VBox面板中的内容距离四周的留空区域
        vBox.setPadding(new Insets(10, 20, 10, 20));
        vBox.getChildren().addAll(tophbox,tophbox1,tophbox2,mainhbox);
        //设置显示信息区的文本区域可以纵向自动扩充范围
        mainPane.setCenter(vBox);
        //底部按钮区域
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 20, 10, 20));
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnSend,  btnExit);
        mainPane.setBottom(hBox);
        Scene scene = new Scene(mainPane, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();


//        btnSend.setDisable(true);
        btnConnect.setOnAction(event -> {
            String ip = tfSmtpAddr.getText().trim();
            String port = tfSmtpPort.getText().trim();
            try {
                tcpMailClient = new TCPMailClient(ip,port);
                // 多线程不需要这一条了
//                String firstMsg = tcpClient.receive();
//                taDisplay.appendText(firstMsg+"\n");
                btnSend.setDisable(false);
                btnConnect.setDisable(true);
                //多线程方法

                readThread = new Thread(()->{
                    String msg = null;
                    while((msg = tcpMailClient.receive())!=null){
                        String msgTemp = msg;
                        Platform.runLater(()->{
                            taSerMsg.appendText(msgTemp+"\n");
                        });
                    }
                    Platform.runLater(()->{
                        taSerMsg.appendText("对话已关闭！\n");
                    });
                });
                readThread.start();
            } catch (IOException e) {
                taSerMsg.appendText("服务器连接失败"+e.getMessage()+"\n");
                btnSend.setDisable(true);
            }
        });
        btnConnect.fire();
        btnExit.setOnAction(event -> {
            endSystem();
        });
        primaryStage.setOnCloseRequest(event -> {
            endSystem();
        });
        btnSend.setOnAction(event -> {
            String smtpAddr = tfSmtpAddr.getText().trim();
            String smtpPort = tfSmtpPort.getText().trim();

            try {
                tcpMailClient = new TCPMailClient(smtpAddr,smtpPort);
                tcpMailClient.send("AUTH LOGIN");
//            base64 = new BASE64();
                String userName="756627124@qq.com";
                String authCode = "rbrxsegwmwjgbbge";
                String msg = BASE64.encode(userName);
                tcpMailClient.send(msg);
                System.out.println("userName send");
                msg = BASE64.encode(authCode);
                tcpMailClient.send(msg);

                msg = "MAIL FROM:<"+ tfSenderAddr.getText().trim()+">";
                tcpMailClient.send(msg);
                msg = "RCPT TO:<"+ tfRecieverAddr.getText().trim()+">";
                tcpMailClient.send(msg);
                msg = "DATA";
                tcpMailClient.send(msg);
                msg = "FROM:"+tfRecieverAddr.getText().trim();
                tcpMailClient.send(msg);
                msg = "Subject"+mtitle.getText().trim();
                tcpMailClient.send(msg);
                msg = "To"+tfRecieverAddr.getText().trim();
                tcpMailClient.send(msg);

                msg = taMainText.getText();
                tcpMailClient.send(msg);

                msg = ".";
                tcpMailClient.send(msg);
                msg = "QUIT";
                tcpMailClient.send(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            String sendMsg = tfSend.getText();
//            if(sendMsg.equals("bye")) {
//                btnConnect.setDisable(false);
//                btnSend.setDisable(true);
//            }
//            tcpMailClient.send(sendMsg);//向服务器发送一串字符
//            taDisplay.appendText("客户端发送：" + sendMsg + "\n");
//            //注释掉这句话，和线程不冲突，不会卡死。
////            String receiveMsg = tcpClient.receive();//从服务器接收一行字符
////            taDisplay.appendText(receiveMsg + "\n");
//            tfSend.clear();
        });

    }

    private void endSystem() {
        if(tcpMailClient != null){
            //向服务器发送关闭连接的约定信息
            tcpMailClient.send("bye");
            tcpMailClient.close();
        }
        System.exit(0);
    }
}
