package visualPart.moderatorSettings.FAQ;

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
import javafx.scene.control.TextArea;
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

public class ControllerFAQ implements Initializable
{
    protected static Socket sock = null;
    protected static ObjectOutputStream oos = null;
    protected static ObjectInputStream ois = null;

    protected String login, question;

    @FXML
    TableView<Question> questionsTableView;
    @FXML
    TableColumn<Question, String> loginTableColumn;
    @FXML
    TableColumn<Question, String> questionTableColumn;
    @FXML
    TextArea questionTextArea, answerTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            loginTableColumn.setCellValueFactory(new PropertyValueFactory<Question, String>("login"));
            questionTableColumn.setCellValueFactory(new PropertyValueFactory<Question, String>("question"));
            ObservableList<Question> listOfQuestions = FXCollections.observableArrayList();
            String message, loginAc, questionAc;
            while (true)
            {
                message = (String) ois.readObject();
                if (message.equals("true"))
                {
                    loginAc = (String) ois.readObject();
                    questionAc = (String) ois.readObject();
                    listOfQuestions.add(new Question(loginAc, questionAc));
                }
                else
                    break;
            }
            questionsTableView.setItems(listOfQuestions);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }

    }

    @FXML
    public void clickToAnswer(ActionEvent event) throws IOException {
        if (!answerTextArea.getText().isEmpty())
        {
            oos.writeObject("answer");
            oos.writeObject(login);
            oos.writeObject(question);
            oos.writeObject(answerTextArea.getText().toString());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Answered");
            alert.setContentText("U have answered the question");
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
    public void getSelectedQuestion()
    {
        int whatSelected = questionsTableView.getSelectionModel().getSelectedIndex();
        questionTextArea.setText(questionTableColumn.getCellData(whatSelected).toString());
        login = loginTableColumn.getCellData(whatSelected).toString();
        question = questionTableColumn.getCellData(whatSelected).toString();
    }

    @FXML
    public void clickToBack(ActionEvent event) throws IOException
    {
        Pane mp = (Pane) FXMLLoader.load(Main.class.getResource("moderatorSettings/ModeratorSettings.fxml"));
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
