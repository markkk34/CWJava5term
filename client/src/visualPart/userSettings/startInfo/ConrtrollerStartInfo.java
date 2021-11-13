package visualPart.userSettings.startInfo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ConrtrollerStartInfo implements Initializable
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    private static ObjectInputStream ois = null;

    @FXML
    ComboBox tariffPlanComboBox;
    @FXML
    TextField nameTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try
        {
            String message;
            while (true)
            {
                message = (String) ois.readObject();
                if (message.equals("true"))
                {
                    tariffPlanComboBox.getItems().add((String)ois.readObject());
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
    public void clickToSend() throws IOException {
        oos.writeObject(nameTextField.getText());
        oos.writeObject(tariffPlanComboBox.getValue().toString());
    }

    public void initSock(Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
    {
        sock = socket;
        this.oos = oos;
        this.ois = ois;
    }
}
