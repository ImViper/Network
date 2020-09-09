package LearnJavaFx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CentetPositionDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new FlowPane();
        pane.setStyle("-fx-background-color: aqua");
        Button buttonOK = new Button("OK");
        Button buttonCancel = new Button("cancle");
        pane.getChildren().addAll(buttonOK,buttonCancel);
        buttonOK.setLayoutX(10);
        buttonCancel.setLayoutX(60);
        Scene scene = new Scene(pane,300,300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
