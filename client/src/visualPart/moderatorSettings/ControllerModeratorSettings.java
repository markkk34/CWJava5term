package visualPart.moderatorSettings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import visualPart.Main;
import visualPart.moderatorSettings.FAQ.ControllerFAQ;
import visualPart.moderatorSettings.moderatorPersonalData.ControllerModeratorPersonalData;
import visualPart.moderatorSettings.workWithUsersAndTariffs.ControllerWorkWithUsersAndTariffs;
import visualPart.userSettings.personalData.ControllerPersonalData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ControllerModeratorSettings
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;

    @FXML
    public void clickToShowPersonalData(ActionEvent event) throws IOException
    {
        oos.writeObject("personalData");
        ControllerModeratorPersonalData controllerModeratorPersonalData = new ControllerModeratorPersonalData();
        controllerModeratorPersonalData.initSock(sock, oos, ois);
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("moderatorSettings/moderatorPersonalData/ModeratorPersonalData.fxml"));
        Stage personalDataStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        personalDataStage.setScene(new Scene(mp));
    }

    @FXML
    public void clickToWorkWithUsersAndTariffs(ActionEvent event) throws IOException
    {
        oos.writeObject("workWithUsersAndTariffs");
        ControllerWorkWithUsersAndTariffs controllerWorkWithUsersAndTariffs = new ControllerWorkWithUsersAndTariffs();
        controllerWorkWithUsersAndTariffs.initSock(sock, oos, ois);
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("moderatorSettings/workWithUsersAndTariffs/WorkWithUsersAndTariffs.fxml"));
        Stage usersAndTariffStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        usersAndTariffStage.setScene(new Scene(mp));
    }

    @FXML
    public void clickToOpenFAQ(ActionEvent event) throws IOException
    {
        oos.writeObject("FAQ");
        ControllerFAQ controllerFAQ = new ControllerFAQ();
        controllerFAQ.initSock(sock, oos, ois);
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("moderatorSettings/FAQ/FAQ.fxml"));
        Stage FAQStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FAQStage.setScene(new Scene(mp));
    }

    @FXML
    public void clickToExit(ActionEvent event) throws IOException
    {
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("RegistrationAuthorisation.fxml"));
        Stage registrationAuthoristaion = (Stage)((Node)event.getSource()).getScene().getWindow();
        registrationAuthoristaion.setScene(new Scene(mp));
        oos.writeObject("exit");
    }

    public void initSock(Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
    {
        sock = socket;
        this.oos = oos;
        this.ois = ois;
    }
}
