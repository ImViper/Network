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
import rmi.RmiKitService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.ResultSet;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/17
 */
public class HelloClientFX extends Application {
    private TextArea taDisplay = new TextArea();
    private TextField tfMessage = new TextField();
    private Button btnEcho = new Button("调用echo方法");
    private Button btnGetTime = new Button("调用getTime方法");

    private HelloService helloService;
    private RmiKitService rmiKitService;
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
        hBox.getChildren().addAll(new Label("输入信息："), tfMessage, btnEcho, btnGetTime);

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.getChildren().addAll(new Label("信息显示区："),taDisplay,hBox);
        borderPane.setCenter(vBox);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(()->{rmiInit();}).start();
        btnEcho.setOnAction(event -> {
            try{
                String msg = tfMessage.getText();
                taDisplay.appendText(helloService.echo(msg)+"\n");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        btnGetTime.setOnAction(event -> {
            try{
//                String msg = helloService.getTime().toString();
                String msg = rmiKitService.longToIp(2130706433);
                taDisplay.appendText(msg+"\n");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

    }

    public void rmiInit(){
        try{
            Registry registry = LocateRegistry.getRegistry("127.0.0.1",1099);
            System.out.println("RMI远程服务别名列表：");
            for (String name: registry.list()
                 ) {
                System.out.println(name);
            }
//            helloService = (HelloService)registry.lookup("HelloService");
            rmiKitService = (RmiKitService)registry.lookup("RmiKitService");
            System.out.println("cnm");



        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
