package chapter12.client;

import chapter12.server.RmiStudentServer;
import javafx.application.Application;
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
import rmi.HelloService;
import rmi.RmiMsgService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/17
 */
public class RmiStudentClientFX extends Application {
    private TextArea taDisplay = new TextArea();
    private TextField tfMessage = new TextField("20181002946&我的RMI服务器已经启动");
    private TextField tfNO = new TextField();
    private TextField tfName = new TextField();
    Button btnSendMsg = new Button("发送信息");
    Button btnSendNoAndName = new Button("发送学号和姓名");
    //客户端也有一份相同的远程接口
    private RmiMsgService rmiMsgService;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10,20,10,20));
        hBox.getChildren().addAll(new Label("输入信息："), tfMessage,btnSendMsg,new Label("学号"),tfNO,new Label("姓名"),tfName,btnSendNoAndName);


        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.getChildren().addAll(new Label("信息显示区："),taDisplay,hBox);
        borderPane.setCenter(vBox);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(()->{rmiInit();}).start();
        btnSendMsg.setOnAction(event -> {
            try{
                String msg = tfMessage.getText();
                taDisplay.appendText(rmiMsgService.send(msg)+"\n");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        btnSendNoAndName.setOnAction(event -> {
            try {
                taDisplay.appendText(rmiMsgService.send(tfNO.getText(), tfName.getText()) + "\n");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });


    }

    public void rmiInit(){
        try{
            Registry registry = LocateRegistry.getRegistry("202.116.195.71",1099);
            System.out.println("RMI远程服务别名列表：");
            for (String name: registry.list()
                 ) {
                System.out.println(name);
            }
            rmiMsgService  = (RmiMsgService)registry.lookup("RmiMsgService");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
