package visualPart.registration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import visualPart.Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ControllerRegistration
{
    @FXML
    TextField loginTextField, passwordTextField;
    @FXML
    Button signUpButton;
    @FXML
    Hyperlink signInHyperlink;

    protected static Socket socket = null;
    protected static ObjectInputStream ois = null;// new ObjectInputStream(socket.getInputStream());
    protected static ObjectOutputStream oos = null;

    @FXML
    public void clickToSignUp(ActionEvent event) throws IOException
    {
        if (!loginTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty())
        {
            oos.writeObject("signUp");
            oos.writeObject(loginTextField.getText());
            oos.writeObject(passwordTextField.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("addin'");
            alert.setContentText("added successfully");
            alert.showAndWait();

            Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("RegistrationAuthorisation.fxml"));
            Stage RegistrationAuthorisationStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            RegistrationAuthorisationStage.setScene(new Scene(mp));
            RegistrationAuthorisationStage.show();
            RegistrationAuthorisationStage.setResizable(false);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Empty field");
            alert.setContentText("Check the fields. One or more is empty");
            alert.showAndWait();
        }
    }

    @FXML
    public void clickToSignIn(ActionEvent event) throws IOException
    {
        oos.writeObject("signIn");
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("RegistrationAuthorisation.fxml"));
        Stage RegistrationAuthorisationStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        RegistrationAuthorisationStage.setScene(new Scene(mp));
        RegistrationAuthorisationStage.show();
        RegistrationAuthorisationStage.setResizable(false);
    }

    @FXML
    public void clickToShowPassword()
    {

    }

    public void initSock(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) throws IOException {
        System.out.println("1");
        this.socket = socket;
        System.out.println("2");
        this.oos = oos;
        System.out.println("3");
        this.ois = ois;
        System.out.println("init stuff");
    }

}
