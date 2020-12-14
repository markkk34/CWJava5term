package visualPart.userSettings.changeTariffPlan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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

public class ControllerChangeTariffPlan implements Initializable
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;
    protected static boolean once = false;

    @FXML
    ComboBox tariffPlanComboBox;
    @FXML
    TextField tariffPlanTextField, costTextField, speedTextField, newTariffPlanTextField, newCostTextField, newSpeedTextField;
    @FXML
    BarChart<?, ?> differanceBarChart;
    @FXML
    NumberAxis yNumberAxis;
    @FXML
    CategoryAxis xCategoryAxis;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try {
            String message;
            while (true)
            {
                message = (String) ois.readObject();
                if (message.equals("true"))
                {
                    tariffPlanComboBox.getItems().add((String) ois.readObject());
                }
                else {
                    break;
                }
            }

            tariffPlanTextField.setText((String) ois.readObject());
            Double cost = (Double) ois.readObject();
            costTextField.setText(cost.toString());
            speedTextField.setText((String) ois.readObject());
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
    public void clickToChooseTariffPlan(ActionEvent event) throws IOException, ClassNotFoundException
    {
        oos.writeObject("choose");
        String whatTariff = tariffPlanComboBox.getValue().toString();
        oos.writeObject(whatTariff);

        newTariffPlanTextField.setText(whatTariff);
        Double cost = (Double) ois.readObject();
        newCostTextField.setText(cost.toString());
        newSpeedTextField.setText((String)ois.readObject());
        System.out.println("we passed shh");

        if (once)
        {
            differanceBarChart.getData().clear();
        }

        XYChart.Series differanceInPayment = new XYChart.Series();
        differanceInPayment.getData().add(new XYChart.Data<>("old cost", Double.parseDouble(costTextField.getText())));
        differanceInPayment.getData().add(new XYChart.Data<>("new cost", Double.parseDouble(newCostTextField.getText())));
        differanceBarChart.getData().addAll(differanceInPayment);
        //}
        once = true;
    }

    @FXML
    public void clickToSave(ActionEvent event) throws IOException {
        oos.writeObject("save");
        oos.writeObject(tariffPlanComboBox.getValue().toString());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("changin'");
        alert.setContentText("changed successfully");
        alert.showAndWait();

        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("userSettings/Settings.fxml"));
        Stage settingsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        settingsStage.setScene(new Scene(mp));
    }

    public void initSock(Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
    {
        sock = socket;
        this.oos = oos;
        this.ois = ois;
    }
}
