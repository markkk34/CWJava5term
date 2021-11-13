package visualPart.adminSettings.workWithAllTariffPlans;

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

public class ControllerWorkWithAllTariffPlans implements Initializable
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;

    protected String oldNameOfTariffPlan;
    //protected ObservableList<Tariff> listOfTariffPlans;

    @FXML
    TableView<Tariff> tariffsTableView;
    @FXML
    TableColumn<Tariff, String> tariffPlanTableColumn;
    @FXML
    TableColumn<Tariff, Double> costTableColumn;
    @FXML
    TableColumn<Tariff, String> speedTableColumn;

    @FXML
    TextField tariffPlanAddingTextField, costAddingTextField, speedAddingTextField, tariffPlanChangingTextField,
    costChangingTextField, speedChangingTextField, tariffPlanDeletingTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            //String message;
            tariffPlanTableColumn.setCellValueFactory(new PropertyValueFactory<Tariff, String>("tariffPlanName"));
            costTableColumn.setCellValueFactory(new PropertyValueFactory<Tariff, Double>("cost"));
            speedTableColumn.setCellValueFactory(new PropertyValueFactory<Tariff, String>("speed"));
            ObservableList<Tariff> listOfTariffPlans = FXCollections.observableArrayList();
            String message, tariffAc, speedAc;
            Double costAc;
            while (true)
            {
                message = (String) ois.readObject();
                if (message.equals("true"))
                {
                    tariffAc=  (String) ois.readObject();
                    costAc = (Double) ois.readObject();
                    speedAc = (String) ois.readObject();
                    listOfTariffPlans.add(new Tariff(tariffAc, costAc,speedAc));
                }
                else
                    break;
            }
            tariffsTableView.setItems(listOfTariffPlans);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
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
    public void clickToAddTariffPlan(ActionEvent event)
    {
        try
        {
            Double cost = Double.parseDouble(costAddingTextField.getText());
            if (!tariffPlanAddingTextField.getText().isEmpty() && !costAddingTextField.getText().isEmpty() &&
            !speedAddingTextField.getText().isEmpty() && (cost > 2))
            {
                oos.writeObject("addNewTariffPlan");
                oos.writeObject(tariffPlanAddingTextField.getText());
                oos.writeObject(cost);
                oos.writeObject(speedAddingTextField.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Adding");
                alert.setContentText("The info was added successfully");
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
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Wrong type of cost");
            alert.setContentText("U entered wrong type of the cost. Check this out again");
            alert.showAndWait();
        }
    }

    @FXML
    public void clickToChangeTariffPlan(ActionEvent event)
    {
        try
        {
            Double cost = Double.parseDouble(costChangingTextField.getText());
            if (!costChangingTextField.getText().isEmpty() && !tariffPlanChangingTextField.getText().isEmpty() &&
                    !speedChangingTextField.getText().isEmpty() && (cost > 2))
            {
                oos.writeObject("changeTariffPlan");
                oos.writeObject(tariffPlanChangingTextField.getText());
                oos.writeObject(cost);
                oos.writeObject(oldNameOfTariffPlan);
                oos.writeObject(speedChangingTextField.getText());
                System.out.println(oldNameOfTariffPlan);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Changing");
                alert.setContentText("The info was changed successfully");
                alert.showAndWait();
                Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("adminSettings/AdminSettings.fxml"));
                Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                settingsStage.setScene(new Scene(mp));
                //oos.writeObject("back");
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
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Wrong type of cost");
            alert.setContentText("U entered wrong type of the cost. Check this out again");
            alert.showAndWait();
        }
    }

    public void clickToDeleteTariff(ActionEvent event) throws IOException
    {
        if (!tariffPlanDeletingTextField.getText().isEmpty())
        {
            oos.writeObject("deleteTariffPlan");
            oos.writeObject(tariffPlanDeletingTextField.getText().toString());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Deleted");
            alert.setContentText("The tariff was deleted successfully");
            alert.showAndWait();
            Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("adminSettings/AdminSettings.fxml"));
            Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            settingsStage.setScene(new Scene(mp));
            //oos.writeObject("back");
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

    @FXML
    public void getSelectedRow()
    {
        int whatSelected = tariffsTableView.getSelectionModel().getSelectedIndex();
        tariffPlanChangingTextField.setText(tariffPlanTableColumn.getCellData(whatSelected).toString());
        costChangingTextField.setText(costTableColumn.getCellData(whatSelected).toString());
        speedChangingTextField.setText(speedTableColumn.getCellData(whatSelected).toString());
        tariffPlanDeletingTextField.setText(tariffPlanTableColumn.getCellData(whatSelected).toString());
        oldNameOfTariffPlan = tariffPlanTableColumn.getCellData(whatSelected).toString();
    }

    public void initSock(Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
    {
        sock = socket;
        this.oos = oos;
        this.ois = ois;
    }
}
