package visualPart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application
{

    @Override
    public void start(Stage stage) throws Exception
    {
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("RegistrationAuthorisation.fxml"));
        ControllerRegistrationAuthorisation controllerRegistrationAuthorisation = new ControllerRegistrationAuthorisation();
        controllerRegistrationAuthorisation.initConnection();
        stage.setScene(new Scene(mp));
        stage.show();
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:///D:/studying/course%203/first%20half/courseWorkJava/courseJava/client/src/visualPart/pictures/globeBlackBlack.jpg"));
    }
}
