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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Author:蔡诚杰
 * Data:2020/10/24
 * description:
 */
public class HTTPClientFX extends Application {
    private Button btnConnect = new Button("连接");
    private Button btnClose = new Button("退出");
    private Button btnClear = new Button("清空");
    private Button btnHttp = new Button("网页请求");
    private TextField tfUrl = new TextField();
    private TextField tfPort = new TextField();
    private TextArea taDisplay = new TextArea();
    private HTTPClient httpClient;
    Thread readThread;

    @Override
    public void start(Stage primaryStage){
        BorderPane mainpane = new BorderPane();
        HBox tophbox = new HBox();
        tophbox.setSpacing(10);
        tophbox.setPadding(new Insets(10,20,10,20));
        tophbox.setAlignment(Pos.CENTER);
        tophbox.getChildren().addAll(new Label("网页地址"),tfUrl,new Label("端口"),tfPort,btnConnect);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(new Label("信息显示区"),taDisplay);
        HBox buttomHbox = new HBox();
        buttomHbox.setPadding(new Insets(10,20,10,20));
        buttomHbox.setSpacing(10);
        buttomHbox.setAlignment(Pos.CENTER_RIGHT);
        buttomHbox.getChildren().addAll(btnHttp,btnClear,btnClose);
        mainpane.setTop(tophbox);
        mainpane.setCenter(vBox);
        mainpane.setBottom(buttomHbox);
        Scene scene = new Scene(mainpane,800,400);
        primaryStage.setScene(scene);
        primaryStage.show();

        btnConnect.setOnAction(event -> {
            String url = tfUrl.getText().trim();
            String port = tfPort.getText().trim();
            try {
                httpClient = new HTTPClient(url,port);
                // 多线程不需要这一条了
//                String firstMsg = tcpClient.receive();
//                taDisplay.appendText(firstMsg+"\n");
//                btnConnect.setDisable(true);
                //多线程方法

                readThread = new Thread(()->{
                    String msg = null;
                    while((msg = httpClient.receive())!=null){
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
            }
        });

        btnHttp.setOnAction(event -> {
            String ip = tfUrl.getText();
            StringBuffer msg = new StringBuffer("GET / HTTP/1.1" + "\r\n"); //
            //  StringBuffer msg = new StringBuffer("GET / connecttest.txt HTTP/1.1 " + "\r\n"); //注意/后面需要有空格
            msg.append("HOST:" + ip + "\r\n");
            msg.append("Accept: */*" + "\r\n");
            msg.append("Accept-Language: zh-cn"  + "\r\n");
            msg.append("Accept-Encoding: deflate" + "\r\n");
            msg.append("User-Agent:Mozilla/4.0(compatible;MSIE6.0;Windows XP)" + "\r\n");
            msg.append("Connection:Keep-Alive" + "\r\n");
            //msg.append
            httpClient.send(msg.toString());
        });

    }


}
