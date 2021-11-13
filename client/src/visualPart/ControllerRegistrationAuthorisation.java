package visualPart;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import visualPart.adminSettings.ControllerAdminSettings;
import visualPart.moderatorSettings.ControllerModeratorSettings;
import visualPart.registration.ControllerRegistration;
import visualPart.userSettings.ControllerSettings;

import javax.sound.midi.ControllerEventListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerRegistrationAuthorisation implements Initializable
{
    @FXML
    Button signInButton;
    @FXML
    TextField loginTextField, passwordTextField;
    @FXML
    CheckBox showPasswordCheckBox;
    @FXML
    ComboBox roleComboBox;
    @FXML
    Hyperlink signUpHyperlink;
    @FXML
    PasswordField passwordPasswordField;

    final static String ADMIN_ROLE = "Администратор";
    final static String USER_ROLE = "Пользователь";
    final static String MODERATOR_ROLE = "Модератор";

    protected boolean invisiblePasswordWasChosenLast = true;

    protected Socket sock = null;
    protected static ObjectInputStream ois = null;
    protected static ObjectOutputStream oos = null;

    public void initConnection()
    {
        try
        {
            sock = new Socket(InetAddress.getByName("127.0.0.1"), Integer.parseInt("1024"));
            oos = new ObjectOutputStream(sock.getOutputStream());// выходной поток для записи данных
            ois = new ObjectInputStream(sock.getInputStream()); // входной поток для чтения данных
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void clickToShowPassword()
    {
        if (invisiblePasswordWasChosenLast)
        {
            passwordTextField.setText(passwordPasswordField.getText());
            passwordPasswordField.setVisible(false);
            passwordTextField.setVisible(true);
        }
        else {
            passwordPasswordField.setText(passwordTextField.getText());
            passwordPasswordField.setVisible(true);
            passwordTextField.setVisible(false);
        }
        invisiblePasswordWasChosenLast = !invisiblePasswordWasChosenLast;
    }

    @FXML
    public void clickToSignIn(ActionEvent event) throws IOException, ClassNotFoundException
    {
        if (!loginTextField.getText().isEmpty() && (!passwordTextField.getText().isEmpty() || !passwordPasswordField.getText().isEmpty()) && !roleComboBox.getSelectionModel().isEmpty())
        {
            if (invisiblePasswordWasChosenLast)//for always having value in TF, cause we use only that, not paspas
                passwordTextField.setText(passwordPasswordField.getText());
            oos.writeObject("signIn");
            oos.writeObject(loginTextField.getText());
            oos.writeObject(passwordTextField.getText());
            oos.writeObject(roleComboBox.getValue().toString());

            String message = (String) ois.readObject();
            System.out.println("got");
            if (message.equals("true")) //we check, did we find this user?
            {
                System.out.println("right success");
                switch (roleComboBox.getValue().toString())
                {
                    case USER_ROLE:
                        Pane mpUser = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/Settings.fxml"));
                        Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        settingsStage.setScene(new Scene(mpUser));
                        ControllerSettings controllerSettings = new ControllerSettings();
                        controllerSettings.initSock(sock, oos, ois);
                        break;
                    case ADMIN_ROLE:
                        Pane mpAdmin = (Pane) FXMLLoader.load(Main.class.getResource("adminSettings/AdminSettings.fxml"));
                        Stage adminSettingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        adminSettingsStage.setScene(new Scene(mpAdmin));
                        ControllerAdminSettings controllerAdminSettings = new ControllerAdminSettings();
                        controllerAdminSettings.initSock(sock, oos, ois);
                        break;
                    case MODERATOR_ROLE:
                        Pane mpModerator = (Pane) FXMLLoader.load(Main.class.getResource("moderatorSettings/ModeratorSettings.fxml"));
                        Stage moderatorSettingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        moderatorSettingsStage.setScene(new Scene(mpModerator));
                        ControllerModeratorSettings controllerModeratorSettings = new ControllerModeratorSettings();
                        controllerModeratorSettings.initSock(sock, oos, ois);
                        break;
                    default:
                        System.out.println("mistake in choosing role");
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Mistake");
                alert.setHeaderText("Cannot find user");
                alert.setContentText("U entered wrong password or login. Check this out again");
                alert.showAndWait();
            }
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
    public void clickToSignUp(ActionEvent event) throws IOException
    {
        oos.writeObject("signUp");
        //Stage registrationStage = new Stage();
        System.out.println("sentt");
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("registration/Registration.fxml"));
        System.out.println("found");
        System.out.println("call nextwind");
        Stage registrationStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        registrationStage.setScene(new Scene(mp));
        ControllerRegistration controllerRegistration = new ControllerRegistration();
        System.out.println("load xo");
        controllerRegistration.initSock(sock, oos, ois);
        registrationStage.show();
        registrationStage.setResizable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleComboBox.getItems().add(USER_ROLE);
        roleComboBox.getItems().add(ADMIN_ROLE);
        roleComboBox.getItems().add(MODERATOR_ROLE);
    }
}
