package visualPart.userSettings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import visualPart.Main;
import visualPart.userSettings.balance.ControllerBalance;
import visualPart.userSettings.changeTariffPlan.ControllerChangeTariffPlan;
import visualPart.userSettings.connection.ControllerConnection;
import visualPart.userSettings.packetService.ControllerPacketService;
import visualPart.userSettings.personalData.ControllerPersonalData;
import visualPart.userSettings.question.ControllerQuestion;
import visualPart.userSettings.startInfo.ConrtrollerStartInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerSettings// implements Initializable
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;

    @FXML
    Button exitButton, personalDataButton, balanceButton;

   /* @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            String message = (String) ois.readObject();
            if (message.equals("true"))
            {
                //ok
            }
            else
            {


                *//*ConrtrollerStartInfo conrtrollerStartInfo = new ConrtrollerStartInfo();
                conrtrollerStartInfo.initSock(sock, oos, ois);
                Stage newStage = new Stage();
                Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/startInfo/StartInfo.fxml"));
                newStage.setScene(new Scene(mp));
                newStage.show();
                newStage.setResizable(false);*//*
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }

    }*/

    @FXML
    public void clickToExit(ActionEvent event) throws IOException {
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("RegistrationAuthorisation.fxml"));
        Stage registrationAuthoristaion = (Stage)((Node)event.getSource()).getScene().getWindow();
        registrationAuthoristaion.setScene(new Scene(mp));
        oos.writeObject("exit");
        //check. may be we need to send data to controller
    }

    @FXML
    public void clickToOpenPersonalData(ActionEvent event) throws IOException
    {
        oos.writeObject("personalData");
        ControllerPersonalData controllerPersonalData = new ControllerPersonalData();
        controllerPersonalData.initSock(sock, oos, ois);
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/personalData/PersonalData.fxml"));
        Stage personalDataStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        personalDataStage.setScene(new Scene(mp));
    }

    @FXML
    public void clickToShowBalance(ActionEvent event) throws IOException
    {
        oos.writeObject("balance");
        ControllerBalance controllerBalance = new ControllerBalance();
        controllerBalance.initSock(sock, oos, ois);
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/balance/Balance.fxml"));
        Stage personalDataStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        personalDataStage.setScene(new Scene(mp));
    }

    @FXML
    public void clickToShowPacketService(ActionEvent event) throws IOException {
        oos.writeObject("packetService");
        ControllerPacketService controllerPacketService = new ControllerPacketService();
        controllerPacketService.initSock(sock, oos, ois);
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/packetService/PacketService.fxml"));
        Stage packetServiceStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        packetServiceStage.setScene(new Scene(mp));
    }

    @FXML
    public void clickToCheckConnection(ActionEvent event) throws IOException
    {
        oos.writeObject("connection");
        ControllerConnection controllerConnection = new ControllerConnection();
        controllerConnection.initSock(sock, oos, ois);
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/connection/Connection.fxml"));
        Stage connectionStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        connectionStage.setScene(new Scene(mp));
    }

    @FXML
    public void clickToChangeTariffPlan(ActionEvent event) throws IOException
    {
        oos.writeObject("changeTariffPlan");
        ControllerChangeTariffPlan controllerTariffPlan = new ControllerChangeTariffPlan();
        controllerTariffPlan.initSock(sock, oos, ois);
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/changeTariffPlan/ChangeTariffPlan.fxml"));
        Stage tariffPlanStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        tariffPlanStage.setScene(new Scene(mp));
    }

    @FXML
    public void clickToAskQuestion(ActionEvent event) throws IOException
    {
        oos.writeObject("question");
        ControllerQuestion controllerQuestion = new ControllerQuestion();
        controllerQuestion.initSock(sock, oos, ois);
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/question/Question.fxml"));
        Stage questionStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        questionStage.setScene(new Scene(mp));
    }

    public void initSock(Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
    {
        sock = socket;
        this.oos = oos;
        this.ois = ois;
    }


}
