package PCHWRMServer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage)
    {
        monitor m = new monitor();
        Scene s = new Scene(m);
        primaryStage.setScene(s);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event->m.isConnected=false);
    }
}
