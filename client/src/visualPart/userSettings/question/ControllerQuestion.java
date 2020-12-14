package visualPart.userSettings.question;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
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

public class ControllerQuestion implements Initializable
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;

    @FXML
    ComboBox questionComboBox;
    @FXML
    TextArea answerTextArea;
    @FXML
    TextField questionTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            String message;
            while (true)
            {
                message = (String) ois.readObject();
                if (message.equals("true"))
                {
                    questionComboBox.getItems().add((String)ois.readObject());
                }
                else
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    @FXML
    public void clickToBack(ActionEvent event) throws IOException
    {
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/Settings.fxml"));
        Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        settingsStage.setScene(new Scene(mp));
        oos.writeObject("back");
    }

    @FXML
    public void clickToShowAnswer(ActionEvent event) throws IOException, ClassNotFoundException {
        oos.writeObject("showAnswer");
        oos.writeObject(questionComboBox.getValue().toString());
        answerTextArea.setText((String)ois.readObject());
    }

    @FXML
    public void clickToAskQuestion(ActionEvent event) throws IOException {
        oos.writeObject("askQuestion");
        oos.writeObject(questionTextField.getText());
        questionComboBox.getItems().add(questionTextField.getText());
        questionTextField.setText("");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("question");
        alert.setContentText("question is sent");
        alert.showAndWait();
    }

    public void initSock(Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
    {
        sock = socket;
        this.oos = oos;
        this.ois = ois;
    }
}
