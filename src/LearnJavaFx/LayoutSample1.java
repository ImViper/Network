package LearnJavaFx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class LayoutSample1 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane minPane = new BorderPane();
        HBox hbox = addHBox();
        minPane.setTop(hbox);
        minPane.setStyle("-fx-background-color: aliceblue");
        VBox vBox = addVBox();
        minPane.setLeft(vBox);

        FlowPane flowPane = addFlowPane();
        minPane.setRight(flowPane);
        
        
        BorderPane centerPane = new BorderPane();
        minPane.setCenter(centerPane);
        centerPane.setCenter(addGridPane());
        centerPane.setBottom(addAnchorPane());

        Scene scene = new Scene(minPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private AnchorPane addAnchorPane() {
        AnchorPane anchorPane =  new AnchorPane();
        Button buttonOK = new Button("OK");
        Button buttoncancle = new Button("cancle");
        buttonOK.setPrefWidth(80);
        buttoncancle.setPrefWidth(80);
        AnchorPane.setRightAnchor(buttoncancle,20.0);
        AnchorPane.setBottomAnchor(buttoncancle,10.0);
        AnchorPane.setRightAnchor(buttonOK,110.0);
        AnchorPane.setBottomAnchor(buttonOK,10.0);
        anchorPane.getChildren().addAll(buttonOK,buttoncancle);

        return anchorPane;
    }

    private GridPane addGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(8,10,8,10));
        gridPane.setStyle("-fx-background-color: white");

        gridPane.add(new ImageView(new Image(this.getClass().getResourceAsStream("graphics/house.png"))),0,0,1,2);
        Label label1 = new Label("sale:");
        label1.setFont(Font.font(null,FontWeight.BOLD,20));
        gridPane.add(label1,1,0);
        Label label2 = new Label("Current Year");
        label2.setFont(Font.font(null,FontWeight.BOLD,20));
        gridPane.add(label2,2,0);
        Label label3 = new Label("Goods and services");
        gridPane.add(label3,1,1,2,1);
        Label label4 = new Label("Goods \n 80^");
        gridPane.add(label4,0,2);
        GridPane.setValignment(label4, VPos.BOTTOM);
        gridPane.add(new ImageView(new Image(this.getClass().getResourceAsStream("graphics/piechart.png"))),1,2,2,1);
        Label label5 = new Label("Services \n  20&");
        gridPane.add(label5,3,2);
        GridPane.setValignment(label1,VPos.BOTTOM);

        return  gridPane;
    }

    private FlowPane addFlowPane() {
        FlowPane flowPane = new FlowPane();
        flowPane.setPadding(new Insets(8,5,8,5));
        flowPane.setHgap(4);
        flowPane.setVgap(6);
        flowPane.setPrefWrapLength(168);
        flowPane.setStyle("-fx-background-color: #DAE6F4");
        flowPane.setPrefWrapLength(170);
        ImageView[] views = new ImageView[8];
        for(int i=0;i<8;i++){
            views[i] = new ImageView(new Image(this.getClass().getResourceAsStream("graphics/chart_"+(i+1)+".png")));
            flowPane.getChildren().add(views[i]);
        }
        return flowPane;
    }

    private VBox addVBox() {
        VBox vBox = new VBox();
        vBox.setSpacing(6);
        vBox.setPadding(new Insets(8));
        Label lblTitle = new Label("数据:");
        lblTitle.setFont(Font.font(null,FontWeight.BOLD,14));
        vBox.getChildren().add(lblTitle);
        Label[] labels = new Label[]{
                new Label("aaa"),new Label("bbb"),new Label("ccc"),
                new Label("ddd")
        };
        for (Label label : labels){
            label.setFont(Font.font("consolas"));
            VBox.setMargin(label,new Insets(0,0,0,15));
            vBox.getChildren().add(label);

        }
                return vBox;
    }

    private HBox addHBox() {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: white");

        hBox.setSpacing(10);
        hBox.setPadding(new Insets(15,12,15,12));
        Button button1 = new Button("button1");
        Button button2 = new Button("button2");
        button1.setPrefSize(100,30);
        button2.setPrefSize(100,30);
        hBox.getChildren().addAll(button1,button2);

        StackPane stackPane = new StackPane();
//        stackPane.setStyle("-fx-background-color: yellow");
        stackPane.setAlignment(Pos.CENTER_RIGHT);
        Circle circle = new Circle();
        circle.setRadius(15);
        circle.setStroke(Color.BLUE);
        Text helptext = new Text("?");
        helptext.setFont(Font.font(null, FontWeight.BOLD,18));
        helptext.setFill(Color.WHITE);
        StackPane.setMargin(helptext,new Insets(0,10,0,0));
        stackPane.getChildren().addAll(circle,helptext);
        hBox.getChildren().add(stackPane);
        HBox.setHgrow(stackPane, Priority.ALWAYS);

        return hBox;
    }
}
