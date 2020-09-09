package LearnJavaFx;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TimeLineDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane pane = new StackPane();
        Text text = new Text(20,50,"Orifrnanubf us fyb");
        text.setFont(Font.font(35));
        text.setFill(Color.RED);
        pane.getChildren().add(text);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),event -> {
            if(text.isVisible())
                text.setVisible(false);
            else
                text.setVisible(true);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        pane.setOnMouseClicked(event -> {
            if(timeline.getStatus()== Animation.Status.RUNNING)
                timeline.pause();
            else
                timeline.play();
        });
        Scene scene = new Scene(pane,500,500);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
