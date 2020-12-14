package visualPart.moderatorSettings.workWithUsersAndTariffs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import visualPart.Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerWorkWithUsersAndTariffs implements Initializable
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;

    @FXML
    TableView<UserTariffPlan> usersAndTariffsTableView;
    @FXML
    TableColumn<UserTariffPlan, String> loginTableColumn;
    @FXML
    TableColumn<UserTariffPlan, String> tariffTableColumn;
    @FXML
    TableColumn<UserTariffPlan, String> passwordTableColumn;
    @FXML
    TextField loginChangingTextField, tariffChangingTextField;
    @FXML
    ComboBox usersComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try
        {
            loginTableColumn.setCellValueFactory(new PropertyValueFactory<UserTariffPlan, String>("login"));
            tariffTableColumn.setCellValueFactory(new PropertyValueFactory<UserTariffPlan, String>("tariffPlan"));
            passwordTableColumn.setCellValueFactory(new PropertyValueFactory<UserTariffPlan, String>("password"));
            ObservableList<UserTariffPlan> listOfUsersAndTariffs = FXCollections.observableArrayList();
            String loginAc, tariffAc, passwordAc, message;
            while (true)
            {
                message = (String) ois.readObject();
                if (message.equals("true"))
                {
                    loginAc = (String) ois.readObject();
                    tariffAc = (String) ois.readObject();
                    passwordAc = (String) ois.readObject();
                    listOfUsersAndTariffs.add(new UserTariffPlan(loginAc, tariffAc, passwordAc));
                }
                else
                    break;
            }
            usersAndTariffsTableView.setItems(listOfUsersAndTariffs);

            String userLoginAc;
            while (true)
            {
                message = (String) ois.readObject();
                if (message.equals("true"))
                {
                    userLoginAc = (String) ois.readObject();
                    usersComboBox.getItems().add(userLoginAc);
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
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("moderatorSettings/ModeratorSettings.fxml"));
        Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        settingsStage.setScene(new Scene(mp));
        oos.writeObject("back");
    }

    @FXML
    public void clickToTurnOffUser(ActionEvent event) throws IOException
    {
        if (!loginChangingTextField.getText().isEmpty())
        {
            oos.writeObject("turnOffUser");
            oos.writeObject(loginChangingTextField.getText().toString());
            //oos.writeObject(tariffChangingTextField.getText().toString());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Turned off");
            alert.setContentText("The user was turned off");
            alert.showAndWait();
            Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("moderatorSettings/ModeratorSettings.fxml"));
            Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            settingsStage.setScene(new Scene(mp));
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Empty");
            alert.setContentText("U did not select anything");
            alert.showAndWait();
        }

    }

    @FXML
    public void clickToDeleteUser(ActionEvent event) throws IOException
    {
        if (!usersComboBox.getValue().toString().isEmpty())
        {
            oos.writeObject("deleteUser");
            oos.writeObject(usersComboBox.getValue().toString());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Deleted");
            alert.setContentText("The user was deleted");
            alert.showAndWait();
            Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("moderatorSettings/ModeratorSettings.fxml"));
            Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            settingsStage.setScene(new Scene(mp));
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Empty");
            alert.setContentText("U did not select anything to delete");
            alert.showAndWait();
        }
    }

    @FXML
    public void getSelectedLoginAndTariff()
    {
        int whatSelected = usersAndTariffsTableView.getSelectionModel().getSelectedIndex();
        loginChangingTextField.setText(loginTableColumn.getCellData(whatSelected).toString());
        //loginDeletingTextField.setText(loginTableColumn.getCellData(whatSelected).toString());
        tariffChangingTextField.setText(tariffTableColumn.getCellData(whatSelected).toString());
    }

    public void initSock(Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
    {
        sock = socket;
        this.oos = oos;
        this.ois = ois;
    }
}
