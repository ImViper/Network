package chapter11;/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/16
 */

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SendPacketFX extends Application {
    private CheckBox cbSYN = new CheckBox("SYN");
    private CheckBox cbACK = new CheckBox("ACK");
    private CheckBox cbRST = new CheckBox("RST");
    private CheckBox cbFIN = new CheckBox("FIN");
    private TextField tfsrcport = new TextField();
    private TextField tfdstport = new TextField();
    private TextField tfsrchost = new TextField();
    private TextField tfdsthost = new TextField();
    private TextField tfsrcmac = new TextField();
    private TextField tfdstmac = new TextField();
    private TextField tfmsg = new TextField();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    BorderPane borderPane = new BorderPane();
    HBox hBox = new HBox();
    hBox.setAlignment(Pos.CENTER);
    hBox.setSpacing(10);
    hBox.getChildren().addAll(new Label("源端口"),tfsrcport,new Label("目的端口"),tfdstport);
    HBox hBox1 = new HBox();
    hBox1.setAlignment(Pos.CENTER);
    hBox1.setSpacing(10);
    hBox1.getChildren().addAll(cbSYN,cbACK,cbRST,cbFIN);
        VBox
    }
}
