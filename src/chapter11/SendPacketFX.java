package chapter11;/**
 * description:
 * author:蔡诚杰;
 * date; 2020/11/16
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jpcap.JpcapSender;

import java.io.BufferedReader;

public class SendPacketFX extends Application {
    private CheckBox cbSYN = new CheckBox("SYN");
    private CheckBox cbACK = new CheckBox("ACK");
    private CheckBox cbRST = new CheckBox("RST");
    private CheckBox cbFIN = new CheckBox("FIN");
    private TextField tfsrcport = new TextField("8000");
    private TextField tfdstport = new TextField("8008");
    private TextField tfsrchost = new TextField("10.173.40.135");
    private TextField tfdsthost = new TextField("202.116.195.71");
    private TextField tfsrcmac = new TextField("A0-C5-89-88-9D-ED");
    private TextField tfdstmac = new TextField("00-11-5d-9c-94-00");
    private TextField tfmsg = new TextField("20181002946&蔡诚杰");
    private Button btnSend = new Button("发送TCP包");
    private Button btnSelect = new Button("选择网卡");
    private Button btnexit = new Button("退出");
    private NetworkChoiceDialog dialog;
    JpcapSender sender;
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
    hBox.setPadding(new Insets(10, 20, 10, 20));
    HBox hBox1 = new HBox();
    hBox1.setSpacing(10);
    hBox1.getChildren().addAll(new Label("TCP标识位"),cbSYN,cbACK,cbRST,cbFIN);
    hBox1.setPadding(new Insets(10, 20, 10, 20));
    HBox hBox2 = new HBox();
    hBox2.setAlignment(Pos.CENTER);
    hBox2.setSpacing(10);
    hBox2.getChildren().addAll(btnSend,btnSelect,btnexit);
    hBox2.setPadding(new Insets(10, 20, 10, 20));
    VBox vBox = new VBox();
    vBox.setSpacing(10);
    vBox.getChildren().addAll(hBox,hBox1,new Label("源主机地址"),tfsrchost,
            new Label("目的主机地址"),tfdsthost,
            new Label("源mac地址"),tfsrcmac,
            new Label("目的mac地址"),tfdstmac,
            new Label("发送的数据"),tfmsg,
            hBox2);
    vBox.setPadding(new Insets(10, 20, 10, 20));
    vBox.prefWidthProperty();
    borderPane.setCenter(vBox);
    Scene scene=new Scene(borderPane,800,500);
    primaryStage.setScene(scene);
    primaryStage.setTitle("发送自购包");
    dialog = new NetworkChoiceDialog(primaryStage);
    dialog.showAndWait();
    sender = dialog.getSender();
    primaryStage.show();

    btnSend.setOnAction(event -> {
        try{
            int srcport = Integer.parseInt(tfsrcport.getText().trim());
            int dstport = Integer.parseInt(tfdstport.getText().trim());
            String srcHost = tfsrchost.getText().trim();
            String dstHost = tfdsthost.getText().trim();
            String srcMAC = tfsrcmac.getText().trim();
            String dstMAC = tfdstmac.getText().trim();
            String data = tfmsg.getText();
            PacketSender.sendTCPPacket(sender,srcport,dstport,srcHost,dstHost,data,srcMAC,dstMAC,cbSYN.isSelected(),cbACK.isSelected(),cbRST.isSelected(),cbFIN.isSelected());
            new Alert(Alert.AlertType.INFORMATION,"已发送").showAndWait();

        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).showAndWait();
        }
    });
    btnSelect.setOnAction(event -> {
        dialog = new NetworkChoiceDialog(primaryStage);
        dialog.showAndWait();
        sender = dialog.getSender();
    });
    btnexit.setOnAction(event -> {
        System.exit(0);
    });
    }
}
