package visualPart.moderatorSettings.moderatorPersonalData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

public class ControllerModeratorPersonalData implements Initializable
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;

    @FXML
    TextField loginTextField, passwordTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try
        {
            String login = (String) ois.readObject();
            String password = (String) ois.readObject();
            loginTextField.setText(login);
            passwordTextField.setText(password);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    @FXML
    public void clickToBack(ActionEvent event) throws IOException {
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("moderatorSettings/ModeratorSettings.fxml"));
        Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        settingsStage.setScene(new Scene(mp));
        oos.writeObject("back");
    }

    @FXML
    public void clickToSavePersonalData(ActionEvent event) throws IOException {
        if (!loginTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty())
        {
            oos.writeObject("savePersonalDataModer");
            oos.writeObject(loginTextField.getText());
            oos.writeObject(passwordTextField.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("change'");
            alert.setContentText("changed successfully");
            alert.showAndWait();
            Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("moderatorSettings/ModeratorSettings.fxml"));
            Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            settingsStage.setScene(new Scene(mp));

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("One or more field is empty");
            alert.setContentText("U entered wrong data. Check this out again");
            alert.showAndWait();
        }
    }

    public void initSock(Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
    {
        sock = socket;
        this.oos = oos;
        this.ois = ois;
    }

}
