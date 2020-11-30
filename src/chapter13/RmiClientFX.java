package chapter13;

import javafx.application.Application;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/30
 */
public class RmiClientFX extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");
    private Button btnLogin = new Button("登录");
    private TextField tfNO = new TextField();
    private TextField tfName = new TextField();
    private TextField tfMsg  = new TextField();
    private TextArea taDisplay = new TextArea();

    private String client;
    private ServerService serverService;
    private ClientService clientService;
    @Override
    public void start(Stage primaryStage) throws Exception {
      initComponents(primaryStage);

      new Thread(()->{
          initRmi();
      }).start();

      initEvent(primaryStage);

    }




    public void appendMsg(String msg) {
        taDisplay.appendText(msg + "\n");
    }

    private void initComponents(Stage primaryStage){
        BorderPane borderPane = new BorderPane();

        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.CENTER);
        hBox1.setSpacing(10);
        hBox1.setPadding(new Insets(10,20,10,20));
        hBox1.getChildren().addAll(new Label("学号："),tfNO,new Label("姓名:"),
                tfName,btnLogin);


        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10,20,10,20));
        hBox.getChildren().addAll(btnSend,btnExit);

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.getChildren().addAll(hBox1,new Label("信息显示区"),taDisplay,new Label("信息输入区"),tfMsg,hBox);

        borderPane.setCenter(vBox);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initRmi() {
        try{
            String ip = "202.116.195.71";
            int port = 8008;
            Registry registry = LocateRegistry.getRegistry(ip,port );
            for (String name : registry.list()
                 ) {
                System.out.println(name);
            }
            serverService = (ServerService) registry.lookup("ServerService");
            clientService = new ClientServiceImpl(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initEvent(Stage primaryStage) {
        btnLogin.setOnAction(event -> {
            try {
                String NO = tfNO.getText().trim();
                String name = tfName.getText().trim();
                if(!NO.equals("") && !NO.equals("")){
                     client = NO + "-" + name;
                }
                String retStr = serverService.addClientToOnlineGroup(client,clientService);
                taDisplay.appendText(retStr + "\n");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        btnSend.setOnAction(event -> {
            String sendmsg = tfMsg.getText();
            try{
                serverService.sendPublicMsgToServer(client,sendmsg);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        });

        btnExit.setOnAction(event -> {
            exit();
        });

    }
    private void exit(){
        try{
            serverService.removeClientFromOnlineGroup(client,clientService);
        }catch (RemoteException e){
            e.printStackTrace();
        }finally {
            System.exit(0);
        }
    }
}
