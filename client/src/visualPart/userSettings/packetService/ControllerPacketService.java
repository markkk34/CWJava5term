package visualPart.userSettings.packetService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import visualPart.Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPacketService implements Initializable
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;

    @FXML
    TextField tariffPlanTextField, costTextField, speedTextField;

    @FXML
    public void clickToBack(ActionEvent event) throws IOException
    {
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/Settings.fxml"));
        Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        settingsStage.setScene(new Scene(mp));
        oos.writeObject("back");
    }

    public void initSock(Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
    {
        sock = socket;
        this.oos = oos;
        this.ois = ois;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try {
            tariffPlanTextField.setText((String) ois.readObject());
            Double cost = (Double) ois.readObject();
            costTextField.setText(cost.toString());
            speedTextField.setText((String) ois.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }
}
