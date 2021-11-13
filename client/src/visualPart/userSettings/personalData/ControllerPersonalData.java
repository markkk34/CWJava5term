package visualPart.userSettings.personalData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

public class ControllerPersonalData implements Initializable
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;

    @FXML
    TextField nameTextField, surnameTextField, ageTextField, addressTextField, loginTextField, passwordTextField;
    @FXML
    Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try {
            System.out.println("gigi");
            nameTextField.setText(ois.readObject().toString());
            surnameTextField.setText(ois.readObject().toString());
            ageTextField.setText(ois.readObject().toString());
            addressTextField.setText(ois.readObject().toString());
            loginTextField.setText(ois.readObject().toString());
            System.out.println("kk");
            passwordTextField.setText(ois.readObject().toString());
            System.out.println("fafa");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("so bav");
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    public void initSock(Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
    {
        this.sock = socket;
        this.oos = oos;
        this.ois = ois;
    }

    @FXML
    public void clickToBack(ActionEvent event) throws IOException
    {
        oos.writeObject("back");
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/Settings.fxml"));
        Stage registrationAuthoristaion = (Stage)((Node)event.getSource()).getScene().getWindow();
        registrationAuthoristaion.setScene(new Scene(mp));
    }

    @FXML
    public void clickToSave(ActionEvent event)
    {
        try
        {
            Double age = Double.parseDouble(ageTextField.getText());
            if (!nameTextField.getText().isEmpty() && !surnameTextField.getText().isEmpty() && !ageTextField.getText().isEmpty()
                    && !addressTextField.getText().isEmpty() && !loginTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty())
            {
                oos.writeObject("save");
                oos.writeObject(nameTextField.getText());
                oos.writeObject(surnameTextField.getText());
                oos.writeObject(age);
                oos.writeObject(addressTextField.getText());
                oos.writeObject(loginTextField.getText());
                oos.writeObject(passwordTextField.getText());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("change'");
                alert.setContentText("changed successfully");
                alert.showAndWait();

                Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/Settings.fxml"));
                Stage registrationAuthoristaion = (Stage)((Node)event.getSource()).getScene().getWindow();
                registrationAuthoristaion.setScene(new Scene(mp));
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Mistake");
                alert.setHeaderText("One or more field is empty");
                alert.setContentText("U entered wrong data. Check this out again");
                alert.showAndWait();
            }
        }
        catch (Exception exception)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Field is empty or u entered age wrongly");
            alert.setContentText("U entered wrong data. Check this out again");
            alert.showAndWait();
        }
    }
}
