package LearnJavaFx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ShowRectangle extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane pane= new StackPane();
        for (int i = 0;i < 4; i++){
            Rectangle r = new Rectangle(250,60);
            r.widthProperty().bind(pane.widthProperty().divide(3));
            r.heightProperty().bind(pane.heightProperty().divide(7));
            r.setRotate(i*180/4);
            r.setStroke(Color.color(Math.random(),Math.random(),Math.random()));
            r.setFill(Color.WHITE);
            pane.getChildren().add(r);
        }
        Scene scene = new Scene(pane,500,500);
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }
}
