package visualPart.userSettings.balance;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import visualPart.Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ControllerBalance implements Initializable
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;

    @FXML
    VBox transactionsVBox;
    @FXML
    TextField sumOfPaymentTextField, currentBalanceTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try {
            String message;
            Double curBalance;
            curBalance = (Double) ois.readObject();
            currentBalanceTextField.setText(curBalance.toString());
            while (true) {
                message = (String) ois.readObject();

                if (message.equals("true"))
                {
                    message = (String) ois.readObject();
                    Label label = new Label(message);
                    label.setPrefHeight(200);
                    transactionsVBox.getChildren().add(label);
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
    public void clickToPay(ActionEvent event) throws IOException
    {
        try
        {
            Double balance = Double.parseDouble(sumOfPaymentTextField.getText());
            if (balance > 0)
            {
                oos.writeObject("pay");
                oos.writeObject(sumOfPaymentTextField.getText());
                Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/Settings.fxml"));
                Stage settingsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                settingsStage.setScene(new Scene(mp));
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Mistake");
                alert.setHeaderText("Minus");
                alert.setContentText("U can not decrease ur own balance, nerd. Check this out again");
                alert.showAndWait();
            }
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Wrong type");
            alert.setContentText("U entered wrong type. Check this out again");
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
