package visualPart.adminSettings.workWithTariffPlans;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import visualPart.Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerWorkWithTariffPlans implements Initializable
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;

    @FXML
    TableView<Users> usersTableView;
    @FXML
    TableColumn<Users, String> loginTableColumn;
    @FXML
    TableColumn<Users, String> nameTableColumn;
    @FXML
    TableColumn<Users, String>tariffTableColumn;
    @FXML
    TableColumn<Users, Double> balanceTableColumn;

    @FXML
    TextField loginTextField, balanceTextField;
    @FXML
    Button plusBalanceButton, minusBalanceButton;

    //protected ObservableList<Users> listOfUsers;
    protected static Double differenceInBalanceForTransactionHistory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try {
            loginTableColumn.setCellValueFactory(new PropertyValueFactory<Users, String>("loginInTable"));
            nameTableColumn.setCellValueFactory(new PropertyValueFactory<Users, String>("nameInTable"));
            tariffTableColumn.setCellValueFactory(new PropertyValueFactory<Users, String>("tariffInTable"));
            balanceTableColumn.setCellValueFactory(new PropertyValueFactory<Users, Double>("balanceInTable"));
            ObservableList<Users> listOfUsers = FXCollections.observableArrayList();
            String message, loginAc, nameAc, tariffAc;
            Double balanceAc;
            while (true) {
                message = (String) ois.readObject();
                if (message.equals("true"))
                {
                    loginAc = (String) ois.readObject();
                    nameAc = (String) ois.readObject();
                    tariffAc = (String) ois.readObject();
                    balanceAc = (Double) ois.readObject();
                    listOfUsers.add(new Users(loginAc, nameAc, tariffAc, balanceAc));
                }
                else
                    break;
            }
            usersTableView.setItems(listOfUsers);



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    @FXML
    public void getSelectedRowLogin(MouseEvent event)
    {
        int whatSelected = usersTableView.getSelectionModel().getSelectedIndex();
        loginTextField.setText(loginTableColumn.getCellData(whatSelected).toString());
        balanceTextField.setText(balanceTableColumn.getCellData(whatSelected).toString());
        differenceInBalanceForTransactionHistory = balanceTableColumn.getCellData(whatSelected);
    }

    @FXML
    public void clickToPlusBalance(ActionEvent event)
    {
        try
        {
            Double newBalance = Double.parseDouble(balanceTextField.getText());
            newBalance++;
            balanceTextField.setText(newBalance.toString());
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Empty");
            alert.setContentText("There is nothing in the Balanca field");
            alert.showAndWait();
        }
    }

    @FXML
    public void clickToMinusBalance(ActionEvent event)
    {
        try
        {
            Double newBalance = Double.parseDouble(balanceTextField.getText());
            newBalance--;
            balanceTextField.setText(newBalance.toString());
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Empty");
            alert.setContentText("There is nothing in the Balanca field");
            alert.showAndWait();
        }
    }

    public void clickToSaveChangesUserTariffBalance(ActionEvent event) throws IOException
    {
        Double balanceStatus;
        try
        {
            balanceStatus = Double.parseDouble(balanceTextField.getText());
            if (!loginTextField.getText().isEmpty())
            {
                oos.writeObject("saveChanges");
                oos.writeObject(balanceStatus);
                oos.writeObject(loginTextField.getText().toString());
                oos.writeObject(balanceStatus - differenceInBalanceForTransactionHistory);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("change'");
                alert.setContentText("changed successfully");
                alert.showAndWait();
                Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("adminSettings/AdminSettings.fxml"));
                Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                settingsStage.setScene(new Scene(mp));
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Mistake");
                alert.setHeaderText("Incorrect input");
                alert.setContentText("U did not choose anyone");
                alert.showAndWait();
            }
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Incorrect input");
            alert.setContentText("Balance field type mistake (value)");
            alert.showAndWait();
        }
    }

    @FXML
    public void clickToBack(ActionEvent event) throws IOException
    {
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("adminSettings/AdminSettings.fxml"));
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
}
