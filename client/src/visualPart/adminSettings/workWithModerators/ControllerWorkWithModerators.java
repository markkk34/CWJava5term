package visualPart.adminSettings.workWithModerators;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

public class ControllerWorkWithModerators implements Initializable
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;

    @FXML
    TableView<Moder> modersTableView;
    @FXML
    TableColumn<Moder, String> loginTableColumn;
    @FXML
    TableColumn<Moder, String> passwordTableColumn;
    @FXML
    TextField loginAddingTextField, passwordAddingTextField, loginDeletingTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            loginTableColumn.setCellValueFactory(new PropertyValueFactory<Moder, String>("login"));
            passwordTableColumn.setCellValueFactory(new PropertyValueFactory<Moder, String>("password"));
            //ObservableList<Moder> listOfModers = FXCollections.observableArrayList();
            ObservableList<Moder> listOfModerators = FXCollections.observableArrayList();
            String message;
            String whatLogin, whatPassword;
            while (true)
            {
                message = (String) ois.readObject();
                if (message.equals("true"))
                {
                    whatLogin = (String) ois.readObject();
                    whatPassword = (String) ois.readObject();
                    listOfModerators.add(new Moder(whatLogin, whatPassword));
                }
                else
                    break;
            }
            modersTableView.setItems(listOfModerators);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    @FXML
    public void clickToAddModer(ActionEvent event) throws IOException
    {
        if (!loginAddingTextField.getText().isEmpty() && !passwordAddingTextField.getText().isEmpty())
        {
            oos.writeObject("addModer");
            oos.writeObject(loginAddingTextField.getText());
            oos.writeObject(passwordAddingTextField.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Added");
            alert.setContentText("The moderator was added successfully");
            alert.showAndWait();
            Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("adminSettings/AdminSettings.fxml"));
            Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            settingsStage.setScene(new Scene(mp));
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Empty field");
            alert.setContentText("U did not enter smth smwhr. Check this out again");
            alert.showAndWait();
        }
    }

    public void clickToDeleteModer(ActionEvent event) throws IOException
    {
        if (!loginDeletingTextField.getText().isEmpty())
        {
            oos.writeObject("deleteModer");
            oos.writeObject(loginDeletingTextField.getText().toString());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Deleted");
            alert.setContentText("The moderator was deleted successfully");
            alert.showAndWait();
            Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("adminSettings/AdminSettings.fxml"));
            Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            settingsStage.setScene(new Scene(mp));
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Empty field");
            alert.setContentText("U did not choose whom to delete. Check this out again");
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

    @FXML
    public void getSelectedLoginInRow()
    {
        int whatSelected = modersTableView.getSelectionModel().getSelectedIndex();
        loginDeletingTextField.setText(loginTableColumn.getCellData(whatSelected).toString());
    }

    public void initSock(Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
    {
        sock = socket;
        this.oos = oos;
        this.ois = ois;
    }
}
