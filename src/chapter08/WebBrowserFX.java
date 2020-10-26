package chapter08;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.concurrent.Worker.State;
/**
 * description:
 * author:蔡诚杰;
 * date; 2020/10/26
 */
public class WebBrowserFX extends Application {
    private Button btnConnect = new Button("跳转");
    private Button btnFlash= new Button("刷新");
    private Button btnBefore= new Button("前进");
    private Button btnAfter = new Button("后退");
    private Button btnHome = new Button("首页");
    private TextField textField = new TextField();
    private WebView webView=new WebView();
    private WebEngine webEngine;
    private WebHistory webHistory;


    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainpane = new BorderPane();
        textField.setPrefWidth(400);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(btnFlash,btnBefore,btnAfter,btnHome,textField,btnConnect);
        ScrollPane scrollPane = new ScrollPane();
        webEngine = webView.getEngine();
        webHistory=webEngine.getHistory();
        webEngine.load("https://www.gdufs.edu.cn");
        scrollPane.setContent(webView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        mainpane.setCenter(scrollPane);
        mainpane.setTop(hBox);

        Scene scene = new Scene(mainpane,1000,800);
        primaryStage.setScene(scene);
        primaryStage.show();



       btnConnect.setOnAction(event -> {
           String url = "https://"+textField.getText();
           webEngine=webView.getEngine();
           webEngine.load(url);
           System.out.println("连接成功");
       });

       btnHome.setOnAction(event -> {
           webEngine.load("https://www.gdufs.edu.cn");
       });

       btnBefore.setOnAction(event -> {
           try {
               webHistory.go(-1);
           } catch (IndexOutOfBoundsException e) {
               e.printStackTrace();
           }
       });
       btnAfter.setOnAction(event -> {
           try {
               webHistory.go(1);
           } catch (IndexOutOfBoundsException e) {
               e.printStackTrace();
           }
       });

       webEngine.getLoadWorker().stateProperty().addListener(
               (observable,oldState,newState)->{
                   if(newState == State.SUCCEEDED){
                       primaryStage.setTitle(webEngine.getTitle());
                       textField.setText(webEngine.getLocation());
                   }
               });

    }
}
