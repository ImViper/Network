package LearnJavaFx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FirstDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new StackPane();
//        Image image = new Image("file:image/flag0.gif");
        Image image = new Image(this.getClass().getResourceAsStream("image/flag0.gif"));
        ImageView iv = new ImageView(image);
        iv.fitHeightProperty().bind(pane.heightProperty());
        iv.fitWidthProperty().bind(pane.widthProperty());
        pane.getChildren().add(iv);
        Scene scene = new Scene(pane,200,250);
        primaryStage.setTitle("MyjavaFX");
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }
}
