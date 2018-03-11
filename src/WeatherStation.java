import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.stage.Stage;

public class WeatherStation extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(WeatherStation.class.getResource("Template-new.fxml"));
        Parent layout = fxmlLoader.load();

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("Style.css");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("scroll-pane");

        Separator separator = new Separator();
        separator.getStyleClass().add("separator");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Pogodynka by TT");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}