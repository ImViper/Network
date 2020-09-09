package LearnJavaFx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class ControlCircle extends Application {
    CirclePane pane = new CirclePane();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        HBox hBox = new HBox();
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);
        borderPane.setBottom(hBox);
        BorderPane.setAlignment(hBox, Pos.CENTER);

        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.CENTER);
        Button btEnlarge = new Button("enlarge");
        Button btShrink = new Button("shrink");
        hBox.getChildren().addAll(btEnlarge,btShrink);
        btEnlarge.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pane.enlarge();
            }
        });
        btShrink.setOnAction(event -> {
            pane.shrink();
        });

        pane.setOnMouseClicked(event -> {
            if(event.getButton()== MouseButton.PRIMARY){
                pane.enlarge();
            }else if(event.getButton() == MouseButton.SECONDARY){
                pane.shrink();
            }
        });

        Scene scene = new Scene(borderPane,300,300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

class CirclePane extends StackPane{
    private Circle circle = new Circle(50);
    public CirclePane(){
        getChildren().add(circle);
        circle.setStroke(Color.BLUE);
        circle.setFill(Color.WHITE);
    }
    public void enlarge(){
        circle.setRadius(circle.getRadius()+2);
    }
    public void shrink(){
        circle.setRadius(circle.getRadius() > 2? circle.getRadius()-2: circle.getRadius());
    }
}
