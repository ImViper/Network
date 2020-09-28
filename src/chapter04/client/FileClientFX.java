package chapter04.client;

import chapter01.TextFileIO;
import chapter03.TCPClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class FileClientFX extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");
    private Button btnConnect = new Button("连接");
    private Button btnDownload = new Button("下载");
    //待发送信息的文本框
    private TextField tfSend = new TextField();
    private TextField tfip = new TextField("127.0.0.1");
    private TextField tfport = new TextField("8008");
    //显示信息的文本区域
    private TextArea taDisplay = new TextArea();
    private TextFileIO textFileIO = new TextFileIO();
    private TCPClient tcpClient;
    Thread readThread;
    private String ip;
    private String port;

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
        hBox.getChildren().addAll(btnSend,  btnDownload,btnExit);
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
                String firstMsg = tcpClient.receive();
                taDisplay.appendText(firstMsg+"\n");
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

        taDisplay.selectionProperty().addListener((observable,oldValue,newValue)->{
            if(!taDisplay.getSelectedText().equals(""))
                tfSend.setText(taDisplay.getSelectedText());
        });
        btnDownload.setOnAction(event -> {
            if(tfSend.getText().equals(""))
                return;
            if(!isValidFile)
                return;
            String fName = tfSend.getText().trim();
            tfSend.clear();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(fName);
            File saveFile = fileChooser.showSaveDialog(null);
            if(saveFile==null)
                return;
            System.out.println("debug"+fName);
            try{
                new FileDataClient(ip,"2020").getFile(saveFile);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(saveFile.getName()+"下载完毕");
                alert.showAndWait();
                FileDialogClient fileDialogClient=new FileDialogClient(ip,"2021");
                fileDialogClient.send("客户端开启下载");
            }catch (IOException e){
                e.printStackTrace();
            }
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
