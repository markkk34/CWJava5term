package visualPart.adminSettings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import visualPart.Main;
import visualPart.adminSettings.adminPersonalData.ControllerAdminPersonalData;
import visualPart.adminSettings.workWithAllTariffPlans.ControllerWorkWithAllTariffPlans;
import visualPart.adminSettings.workWithModerators.ControllerWorkWithModerators;
import visualPart.adminSettings.workWithTariffPlans.ControllerWorkWithTariffPlans;
import visualPart.userSettings.connection.ControllerConnection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ControllerAdminSettings
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;

    @FXML
    public void clickToExit(ActionEvent event) throws IOException
    {
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("RegistrationAuthorisation.fxml"));
        Stage registrationAuthoristaion = (Stage)((Node)event.getSource()).getScene().getWindow();
        registrationAuthoristaion.setScene(new Scene(mp));
        oos.writeObject("exit");
    }

    @FXML
    public void clickToWorkWithTariffPlanOfUsers(ActionEvent event) throws IOException
    {
        oos.writeObject("workTariffPlanOfUsers");
        ControllerWorkWithTariffPlans controllerWorkWithTariffPlans = new ControllerWorkWithTariffPlans();
        controllerWorkWithTariffPlans.initSock(sock, oos, ois);
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("adminSettings/workWithTariffPlans/WorkWithTariffPlans.fxml"));
        Stage connectionStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        connectionStage.setScene(new Scene(mp));
    }

    @FXML
    public void clickToShowPersonalData(ActionEvent event) throws IOException
    {
        oos.writeObject("adminPersonalData");
        ControllerAdminPersonalData controllerAdminPersonalData = new ControllerAdminPersonalData();
        controllerAdminPersonalData.initSock(sock, oos, ois);
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("adminSettings/adminPersonalData/AdminPersonalData.fxml"));
        Stage adminPersonalDataStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        adminPersonalDataStage.setScene(new Scene(mp));
    }

    @FXML
    public void clickToWorkWithTariffPlans(ActionEvent event) throws IOException
    {
        oos.writeObject("tariffPlans");
        ControllerWorkWithAllTariffPlans controllerWorkWithAllTariffPlans = new ControllerWorkWithAllTariffPlans();
        controllerWorkWithAllTariffPlans.initSock(sock, oos, ois);
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("adminSettings/workWithAllTariffPlans/WorkWithAllTariffPlans.fxml"));
        Stage workWithAllTariffPlansStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        workWithAllTariffPlansStage.setScene(new Scene(mp));
    }

    @FXML
    public void clickToAddDeleteModerator(ActionEvent event) throws IOException
    {
        oos.writeObject("addDeleteModerator");
        ControllerWorkWithModerators controllerWorkWithModerators = new ControllerWorkWithModerators();
        controllerWorkWithModerators.initSock(sock, oos, ois);
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("adminSettings/workWithModerators/WorkWithModerators.fxml"));
        Stage workWithModeratorsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        workWithModeratorsStage.setScene(new Scene(mp));
    }

    public void initSock(Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
    {
        sock = socket;
        this.oos = oos;
        this.ois = ois;
    }
}
