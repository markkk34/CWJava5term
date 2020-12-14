package Communication;

import Database.Configs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Date;

public class ServerThread extends Configs implements Runnable
{
    protected Socket client;
    protected ObjectInputStream ois;
    protected ObjectOutputStream oos;
    protected static int am = 0;

    protected PreparedStatement preparedStatement;
    protected Connection connection;

    protected String login;
    protected String password;
    protected String role;

    protected static int amOfConnectedUsers;


    public ServerThread(Socket client)
    {
        this.client = client;
        //this.amOfConnectedUsers = amount;
        amOfConnectedUsers++;
        System.out.println("amount of connected users: " + amOfConnectedUsers);
    }

    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(client.getInputStream());
            oos = new ObjectOutputStream(client.getOutputStream());

            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
            System.out.println("success");

            while (true) {

                String message = (String) ois.readObject();

                switch (message)
                {
                    case "signIn": signIn();
                        break;
                    case "signUp": signUp(); //we register only users
                        break;
                    default:
                        System.out.println("serverThread mistake");
                }
            }
        }
        catch (Exception exception)
        {
           // exception.printStackTrace();
        }finally {
            try {
                ois.close();//закрытие входного потока
            } catch (IOException e) {
                //e.printStackTrace();
            }
            try {
                oos.close();//закрытие входного потока
            } catch (IOException e) {
                //e.printStackTrace();
            }
            try {
                client.close();//закрытие сокета, выделенного для работы с подключившимся клиентом
            } catch (IOException e) {
                //e.printStackTrace();
            }
            System.out.println("Client disconnected");
            amOfConnectedUsers--;
            System.out.println("amount: " + amOfConnectedUsers);
        }
    }

    public void signIn() throws IOException, ClassNotFoundException
    {
        System.out.println("in func");
        login = (String) ois.readObject();
        password = (String) ois.readObject();
        role = (String) ois.readObject();
        switch (role) //we interpret rus value to eng
        {
            case "Пользователь":
                role = "user";
                break;
            case "Администратор":
                role = "admin";
                break;
            case "Модератор":
                role = "moderator";
                break;
            default:
                System.out.println("while role mistake");
        }

        try
        {
            preparedStatement = connection.prepareStatement("select * from users where login = ? and password = ? and " +
                    "role = ?");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                //System.out.println(resultSet.getString("login") + "alal" + resultSet.getString("password"));
                oos.writeObject("true");
                switch (role)
                {
                    case "admin":
                        actAsAdmin();
                        break;
                    case "user":
                        actAsUser();
                        break;
                    case "moderator":
                        actAsModerator();
                        break;
                    default:
                        System.out.println("mistake in role case");
                }
            }
            else
            {
                oos.writeObject("false");
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void signUp() throws IOException, ClassNotFoundException, SQLException {
        String message =(String) ois.readObject();
        switch (message)
        {
            case "signUp":
                login = (String) ois.readObject();
                password = (String) ois.readObject();
                String command = "insert into users(login, password, role) values (?, ?, 'user')";
                preparedStatement = connection.prepareStatement(command);
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                preparedStatement.executeUpdate();
                break;
            case "signIn":
                break;
            default:
                System.out.println("signIn/Up mistake");
        }
    }

    public void actAsUser() throws IOException, ClassNotFoundException, SQLException //settings in API
    {
        String command = "select personalDataId from personal_data where personalDataId = (select userId from users where login = ?)";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
        {
            //oos.writeObject("true");
        }
        else {
            //oos.writeObject("false");
           /* String commandForStart = "select * from tariff_plans";
            preparedStatement = connection.prepareStatement("");
            resultSet = preparedStatement.executeQuery(commandForStart);
            while (true)
            {
                if (resultSet.next())
                {
                    oos.writeObject("true");
                    oos.writeObject(resultSet.getString("planName"));
                }
                else
                {
                    oos.writeObject("false");
                    break;
                }
            }*/
            //String whatName = (String) ois.readObject();
            //String whatTariffPlan = (String) ois.readObject();
            /*String commandForPersonalData = "insert into personal_data(personalDataId, name) values ((select userId from" +
                    " users where login = ?), ?)";
            String commandForSubscribers = "insert into subscribers(subscriberId, tariffPlanId, name) values " +
                    "((select userId from users where login = ?), (select tariffPlanId from tariff_plans where planName = ?)," +
                    " 0)";*/
            String commandForPersonalData = "insert into personal_data(personalDataId) values ((select userId from" +
                    " users where login = ?))";
            String commandForSubscribers = "insert into subscribers(subscriberId, balance) values " +
                    "((select userId from users where login = ?), 0)";
            preparedStatement = connection.prepareStatement(commandForPersonalData);
            preparedStatement.setString(1, login);
            //preparedStatement.setString(2, whatName);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(commandForSubscribers);
            preparedStatement.setString(1, login);
            //preparedStatement.setString(2, whatTariffPlan);
            preparedStatement.executeUpdate();
        }

        boolean going = true;
        while (going) {
            String message = (String) ois.readObject();
            switch (message) {
                case "exit":
                    going = false;
                    break;
                case "personalData":
                    userPersonalData();
                    break;
                case "balance":
                    userBalance();
                    break;
                case "packetService":
                    userPacketService();
                    break;
                case "connection":
                    userConnection();
                    break;
                case "changeTariffPlan":
                    userChangeTariffPlan();
                    break;
                case "question":
                    userQuestion();
                    break;
                default:
                    System.out.println("mist in choosin' while actinn' user");
            }
        }
    }

    public void userPersonalData() throws IOException, ClassNotFoundException, SQLException
    {
        System.out.println(login);

        String command = "select * from users" +
                " join personal_data on personal_data.personalDataId = users.userID " +
                "join subscribers on subscribers.subscriberId = users.userId " +
                "where login = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            System.out.println("I");
            oos.writeObject(resultSet.getString("name"));
            System.out.println("do");
            oos.writeObject(resultSet.getString("surname"));
            System.out.println("my");
            oos.writeObject(resultSet.getDouble("age"));
            oos.writeObject(resultSet.getString("address"));
            oos.writeObject(resultSet.getString("login"));
            System.out.println("rpe");
            oos.writeObject(resultSet.getString("password"));
            System.out.println("over");
        }
        else
        {
            oos.writeObject("unknown");
            oos.writeObject("unknown");
            oos.writeObject(0);
            oos.writeObject("unknown");
            oos.writeObject(login);
            oos.writeObject(password);
        }

        String message = (String) ois.readObject();
        switch (message)
        {
            case "back":
                break;
            case "save":
                saveUserPersonalData();
                break;
            default:
                System.out.println("mistWhile er data");
        }
    }

    public void saveUserPersonalData() throws IOException, ClassNotFoundException, SQLException {
        String name = (String) ois.readObject();
        String surname = (String) ois.readObject();
        Double age = (Double) ois.readObject();
        String address = (String) ois.readObject();
        String oldLogin = login;
        login = (String) ois.readObject();
        password = (String) ois.readObject();
        String commandUser = "update users set login = ?, password = ? where login = ?";
        String commandPersonalData = "update personal_data set name = ?, surname = ?, age = ?, address = ?" +
                " where personalDataId = (select userId from users where login = ?)";
        //String commandSubscriber = "update subscribers set balance = 5 where subscriberId = (select userId from users where login = ?)";
        preparedStatement = connection.prepareStatement(commandUser);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, oldLogin);
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement(commandPersonalData);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, surname);
        preparedStatement.setDouble(3, age);
        preparedStatement.setString(4, address);
        preparedStatement.setString(5, login);
        preparedStatement.executeUpdate();
    }

    public void userBalance() throws IOException, ClassNotFoundException, SQLException
    {
        String commandForCurBalance = "select * from subscribers where subscriberId = (select userId from users where login = ?)";
        preparedStatement = connection.prepareStatement(commandForCurBalance);
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
            oos.writeObject(resultSet.getDouble("balance"));

        String command = "select * from transactions where subscriberId = (select userId from users where login = ?)";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, login);
        resultSet = preparedStatement.executeQuery();
        while (true)
        {
            if (resultSet.next())
            {
                oos.writeObject("true");
                oos.writeObject(resultSet.getString("transaction"));
            }
            else
            {
                oos.writeObject("false");
                break;
            }
        }

        String message = (String) ois.readObject();
        switch (message)
        {
            case "back":
                break;
            case "pay": userPayBalance();
                break;
            default:
                System.out.println("while balance mistake");
        }
    }

    public void userPayBalance() throws IOException, ClassNotFoundException, SQLException
    {
        String addBalance = (String) ois.readObject();
        String command = "update subscribers set balance = balance + ? where" +
                " subscriberId = (select userId from users where login = ?);";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, addBalance);
        preparedStatement.setString(2, login);
        preparedStatement.executeUpdate();

        command = "insert into transactions(subscriberId, transaction) values ((select userId from users where login = ?), ?)";
        //Date date = new Date();
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, (new Date().toString()) + ": " + addBalance + " $");
        preparedStatement.executeUpdate();
    }

    public void userPacketService() throws IOException, ClassNotFoundException, SQLException
    {
        String command = "select * from tariff_plans where " +
                "tariffPlanId = (select tariffPlanId from subscribers where subscriberId = (select userId from users where " +
                "login = ?))";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
        {
            oos.writeObject(resultSet.getString("planName"));
            oos.writeObject(resultSet.getDouble("cost"));
            oos.writeObject(resultSet.getString("speed"));
        }
        else {
            Double d = 0.0;
            oos.writeObject("unknown");
            oos.writeObject(d);
            oos.writeObject("unknown");
        }

        ois.readObject();//we get back (cause only one button)

    }

    public void userConnection() throws IOException, ClassNotFoundException, SQLException
    {
        String command = "select balance from subscribers where subscriberId = (select userId from users where login = ?)";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
        {
            if (resultSet.getDouble("balance") >= 0.1)
                oos.writeObject("true");
            else
                oos.writeObject("false");
        }
        else
            oos.writeObject("noData");

        ois.readObject();//the same as prev
    }

    public void userChangeTariffPlan() throws IOException, ClassNotFoundException, SQLException
    {
        String command = "select * from tariff_plans";
        preparedStatement = connection.prepareStatement("");
        ResultSet resultSet = preparedStatement.executeQuery(command);

        while (true) //initialisin' the default values
        {
            if (resultSet.next())
            {
                oos.writeObject("true");
                oos.writeObject(resultSet.getString("planName"));
            }
            else
            {
                oos.writeObject("false");
                break;
            }
        }
        resultSet.close();

        String commandForStartValues = "select * from tariff_plans where " +
                "tariffPlanId = (select tariffPlanId from subscribers where subscriberId = (select userId from users where " +
                "login = ?))";
        preparedStatement = connection.prepareStatement(commandForStartValues);
        preparedStatement.setString(1, login);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
        {
            oos.writeObject(resultSet.getString("planName"));
            oos.writeObject(resultSet.getDouble("cost"));
            oos.writeObject(resultSet.getString("speed"));
        }
        else
        {
            oos.writeObject("unknown");
            oos.writeObject(0.0);
            oos.writeObject("unknown");
        }

        boolean going = true;
        while(going)
        {
            String message = (String) ois.readObject();
            switch (message) {
                case "back":
                    going = false;
                    break;
                case "choose":
                    userChooseTariffPlan();
                    break;
                case "save":
                    userSaveNewTariffPlan();
                    going = false;
                    break;
                default:
                    System.out.println("while newtariff plan mistake");
            }
        }
    }

    public void userChooseTariffPlan() throws IOException, ClassNotFoundException, SQLException {
        String chosenPlanName = (String) ois.readObject();
        String command = "select * from tariff_plans where planName = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, chosenPlanName);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next())
        {
            System.out.println("we copw with that");
            oos.writeObject(resultSet.getDouble("cost"));
            oos.writeObject(resultSet.getString("speed"));
            System.out.println("done");
        }
        else
            System.out.println("smth is wrong");
    }

    public void userSaveNewTariffPlan() throws SQLException, IOException, ClassNotFoundException {
        String newPlanName = (String) ois.readObject();
        String command = "update subscribers set tariffPlanId = (select tariffPlanId from tariff_plans where planName = ?) " +
                "where subscriberId = (select userId from users where login = ?)";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, newPlanName);
        preparedStatement.setString(2, login);
        preparedStatement.executeUpdate();
    }

    public void userQuestion() throws IOException, ClassNotFoundException, SQLException {
        String command = "select * from requests where subscriberId = (select userId from users where login = ?)";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (true)
        {
            if (resultSet.next())
            {
                oos.writeObject("true");
                oos.writeObject(resultSet.getString("question"));
                //oos.writeObject(resultSet.getString("answer"));
            }
            else {
                oos.writeObject("false");
                break;
            }
        }


        boolean going = true;
        while (going)
        {
            String message = (String) ois.readObject();
            switch (message)
            {
                case "back":
                    going = false;
                    break;
                case "showAnswer":
                    userShowAnswer();
                    break;
                case "askQuestion":
                    userAskQuestion();
                    break;
                default:
                    System.out.println("mist while ask");
            }

        }
    }

    public void userShowAnswer() throws SQLException, IOException, ClassNotFoundException {
        String whatQuestion = (String) ois.readObject();
        String command = "select * from requests where question = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, whatQuestion);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
        {
            oos.writeObject(resultSet.getString("answer"));
        }
    }

    public void userAskQuestion() throws IOException, ClassNotFoundException, SQLException {
        String whatQuestion = (String) ois.readObject();
        String command = "insert into requests(subscriberId, question) values ((select userId from users where " +
                "login = ?), ?)";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, whatQuestion);
        preparedStatement.executeUpdate();
    }

    public void actAsModerator() throws IOException, ClassNotFoundException, SQLException {
        String message;
        boolean going = true;
        while (going)
        {
            message = (String) ois.readObject();
            switch (message)
            {
                case "exit":
                    going = false;
                    break;
                case "personalData":
                    moderatorPersonalData();
                    break;
                case "workWithUsersAndTariffs":
                    moderatorWorkWithUsersAndTariffPlans();
                    break;
                case "FAQ":
                    moderatorFAQ();
                    break;
                default:
                    System.out.println("mist while moderActing");
            }
        }
    }

    public void moderatorPersonalData() throws IOException, ClassNotFoundException, SQLException
    {
        String message;
        boolean going = true;

        String command = "select * from users where login = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next())
        {
            oos.writeObject(resultSet.getString("login"));
            oos.writeObject(resultSet.getString("password"));
        }

        while (going)
        {
            message = (String) ois.readObject();
            switch (message)
            {
                case "back":
                    going = false;
                    break;
                case "savePersonalDataModer":
                    saveModeratorPersonalData();
                    going = false;
                    break;
                default:
                    System.out.println("mist while personalModerData");
            }
        }
    }

    public void saveModeratorPersonalData() throws IOException, ClassNotFoundException, SQLException
    {
        String newLogin = (String) ois.readObject();
        String newPassword = (String) ois.readObject();
        String command = "update users set login = ?, password = ? where login = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, newLogin);
        preparedStatement.setString(2, newPassword);
        preparedStatement.setString(3, login);
        preparedStatement.executeUpdate();
    }

    public void moderatorWorkWithUsersAndTariffPlans() throws IOException, ClassNotFoundException, SQLException
    {
        String message;
        boolean going = true;

        String commandForUsersAndTariffs = "select * from users " +
                "join subscribers on subscribers.subscriberId = users.userId " +
                "join tariff_plans on tariff_plans.tariffPlanId = subscribers.tariffPlanId " +
                "where role = ?";
        String commandForUsers = "select * from users where role = ?";
        preparedStatement = connection.prepareStatement(commandForUsersAndTariffs);
        preparedStatement.setString(1, "user");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (true)
        {
            if (resultSet.next())
            {
                oos.writeObject("true");
                oos.writeObject(resultSet.getString("login"));
                oos.writeObject(resultSet.getString("planName"));
                oos.writeObject(resultSet.getString("password"));
            }
            else
            {
                oos.writeObject("false");
                break;
            }
        }
        preparedStatement = connection.prepareStatement(commandForUsers);
        preparedStatement.setString(1, "user");
        resultSet = preparedStatement.executeQuery();
        while (true)
        {
            if (resultSet.next())
            {
                oos.writeObject("true");
                oos.writeObject(resultSet.getString("login"));
            }
            else
            {
                oos.writeObject("false");
                break;
            }
        }

        while (going)
        {
            message = (String) ois.readObject();
            switch (message)
            {
                case "back":
                    going = false;
                    break;
                case "turnOffUser":
                    moderatorTurnOffUser();
                    going = false;
                    break;
                case "deleteUser":
                    moderatorDeleteUser();
                    going = false;
                    break;
                default:
                    System.out.println("mist while workWithUsersAndTariffs");
            }
        }
    }

    public void moderatorTurnOffUser() throws IOException, ClassNotFoundException, SQLException
    {
        String loginAc = (String) ois.readObject();
        //String tariffAc = (String) ois.readObject();
        String command = "update subscribers set tariffPlanId = null where " +
                "subscriberId = (select userId from users where login = ?)";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, loginAc);
        preparedStatement.executeUpdate();
    }

    public void moderatorDeleteUser() throws IOException, ClassNotFoundException, SQLException
    {
        String logingAc = (String) ois.readObject();
        String command = "delete from users where login = ? and role = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, logingAc);
        preparedStatement.setString(2, "user");
        preparedStatement.executeUpdate();
    }

    public void moderatorFAQ() throws IOException, ClassNotFoundException, SQLException
    {
        String message;
        boolean going = true;

        String command = "select * from users " +
                "join subscribers on subscribers.subscriberId = users.userId " +
                "join requests on requests.subscriberId = users.userId " +
                "where role = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, "user");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (true)
        {
            if (resultSet.next())
            {
                if (resultSet.getString("answer").equals("not considered yet"))
                {
                    oos.writeObject("true");
                    oos.writeObject(resultSet.getString("login"));
                    oos.writeObject(resultSet.getString("question"));
                }
            }
            else
            {
                oos.writeObject("false");
                break;
            }
        }

        while (going)
        {
            message = (String) ois.readObject();
            switch (message)
            {
                case "back":
                    going = false;
                    break;
                case "answer":
                    moderatorAnswerQuestion();
                    going = false;
                    break;

                default:
                    System.out.println("mist while FAQ");
            }
        }
    }

    public void moderatorAnswerQuestion() throws IOException, ClassNotFoundException, SQLException
    {
        String loginAc = (String) ois.readObject();
        String questionAc = (String) ois.readObject();
        String answerAc = (String) ois.readObject();
        String command = "update requests set answer = ? where " +
                "subscriberId = (select subscriberId from subscribers where subscriberId = " +
                "(select userId from users where login = ?)) and question = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, answerAc);
        preparedStatement.setString(2, loginAc);
        preparedStatement.setString(3, questionAc);
        preparedStatement.executeUpdate();
    }

    public void actAsAdmin() throws IOException, ClassNotFoundException, SQLException {
        String message;
        boolean going = true;
        while (going)
        {
            message = (String) ois.readObject();
            switch (message)
            {
                case "exit":
                    going = false;
                    break;
                case "workTariffPlanOfUsers":
                    adminWorkWithTariffPlan();
                    break;
                case "adminPersonalData":
                    adminPersonalData();
                    break;
                case "tariffPlans":
                    adminWorkWithAllTariffPlansToExactlySmthWithThem();
                    break;
                case "addDeleteModerator":
                    adminAddDeleteModerator();
                    break;
                default:
                    System.out.println("mist while admin setts");
            }
        }
    }

    public void adminWorkWithTariffPlan() throws IOException, ClassNotFoundException, SQLException {
        String message;
        String command = "select * from users " +
                "join personal_data on personal_data.personalDataId = users.userId " +
                "join subscribers on subscribers.subscriberId = users.userId " +
                "join tariff_plans on tariff_plans.tariffPlanId = (select tariffPlanId from subscribers where subscriberId = users.userId)";
        preparedStatement = connection.prepareStatement("");
        ResultSet resultSet = preparedStatement.executeQuery(command);
        while(true)
        {
            if (resultSet.next())
            {
                oos.writeObject("true");
                oos.writeObject(resultSet.getString("login"));
                oos.writeObject(resultSet.getString("name"));
                oos.writeObject(resultSet.getString("planName"));
                oos.writeObject(resultSet.getDouble("balance"));
            }
            else
            {
                oos.writeObject("false");
                break;
            }
        }

        boolean going = true;
        while (going)
        {
            message = (String) ois.readObject();
            switch (message)
            {
                case "back":
                    going = false;
                    break;
                case "saveChanges":
                    saveUserChangesTariffBalance();
                    going = false;
                    break;
                default:
                    System.out.println("mist while tariff stuff");
            }
        }

    }

    public void saveUserChangesTariffBalance() throws IOException, ClassNotFoundException, SQLException {
        Double whatBalance = (Double) ois.readObject();
        String whatLogin = (String) ois.readObject();
        Double differanceForTransactionHistory = (Double) ois.readObject();
        //new Date().toString()) + ":  + " + addBalance + " $"
        String commandForBalance = "update subscribers set balance = ? where subscriberId = (select userId from users where" +
                " login = ?)";
        int idForNotSubqueryAndForEasyLife;
        String commandForTransaction = "insert into transactions(subscriberId, transaction) values " +
                "((select subscriberId from subscribers where subscriberId = (select userId from users where login = ?))," +
                " ?)";

        preparedStatement = connection.prepareStatement(commandForBalance);
        preparedStatement.setDouble(1, whatBalance);
        preparedStatement.setString(2, whatLogin);
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement(commandForTransaction);
        preparedStatement.setString(1, whatLogin);
        preparedStatement.setString(2, new Date().toString() + ":  " + differanceForTransactionHistory.toString() + " $");
        preparedStatement.executeUpdate();
    }

    public void adminPersonalData() throws IOException, ClassNotFoundException, SQLException
    {
        String command = "select * from users where login = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
        {
            oos.writeObject(resultSet.getString("login"));
            oos.writeObject(resultSet.getString("password"));
        }

        String message = (String) ois.readObject();

        switch (message)
        {
            case "back":
                break;
            case "saveAdmin":
                saveAdminPersonalData();
                break;
            default:
                System.out.println("mist while adm pers data");
        }
    }

    public void saveAdminPersonalData() throws IOException, ClassNotFoundException, SQLException
    {
        String newLogin = (String) ois.readObject();
        String newPassword = (String) ois.readObject();

        String command  = "update users set login = ?, password = ? where login = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, newLogin);
        preparedStatement.setString(2, newPassword);
        preparedStatement.setString(3, login);
        preparedStatement.executeUpdate();
    }

    public void adminWorkWithAllTariffPlansToExactlySmthWithThem() throws IOException, ClassNotFoundException, SQLException {
        String message;
        String command = "select * from tariff_plans";
        preparedStatement = connection.prepareStatement("");
        ResultSet resultSet = preparedStatement.executeQuery(command);
        while (true)
        {
            if (resultSet.next())
            {
                oos.writeObject("true");
                oos.writeObject(resultSet.getString("planName"));
                oos.writeObject(resultSet.getDouble("cost"));
                oos.writeObject(resultSet.getString("speed"));
            }
            else
            {
                oos.writeObject("false");
                break;
            }
        }

        boolean going = true;

        while (going)
        {
            message = (String) ois.readObject();
            switch (message)
            {
                case "back":
                    going = false;
                    break;
                case "addNewTariffPlan":
                    addNewTariffPlan();
                    going = false;
                    break;
                case "changeTariffPlan":
                    adminChangeTariffPlan();
                    going = false;
                    break;
                case "deleteTariffPlan":
                    adminDeleteTariffPlan();
                    going = false;
                    break;
                default:
                    System.out.println("mist while tarifPlansWorkinAdmin");
            }
        }

    }

    public void addNewTariffPlan() throws IOException, ClassNotFoundException, SQLException
    {
        String tariffPlan = (String) ois.readObject();
        Double cost = (Double) ois.readObject();
        String speed = (String) ois.readObject();
        String command = "insert into tariff_plans(planName, cost, speed) values (?, ?, ?)";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, tariffPlan);
        preparedStatement.setDouble(2, cost);
        preparedStatement.setString(3, speed);
        preparedStatement.executeUpdate();
    }

    public void adminChangeTariffPlan() throws IOException, ClassNotFoundException, SQLException
    {
        String tariffPlan = (String) ois.readObject();
        Double cost = (Double) ois.readObject();
        String oldNameOfTariffPlan = (String) ois.readObject();
        String speed = (String) ois.readObject();
        String command = "update tariff_plans set planName = ?, cost = ?, speed = ? where planName = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, tariffPlan);
        preparedStatement.setDouble(2, cost);
        preparedStatement.setString(3, speed);
        preparedStatement.setString(4, oldNameOfTariffPlan);
        preparedStatement.executeUpdate();
    }

    public void adminDeleteTariffPlan() throws IOException, ClassNotFoundException, SQLException {
        String tariff = (String) ois.readObject();
        String command = "delete from tariff_plans where planName = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, tariff);
        preparedStatement.executeUpdate();
    }

    public void adminAddDeleteModerator() throws SQLException, IOException, ClassNotFoundException
    {
        String command = "select * from users where role = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, "moderator");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (true)
        {
            if (resultSet.next())
            {
                oos.writeObject("true");
                oos.writeObject(resultSet.getString("login"));
                oos.writeObject(resultSet.getString("password"));
            }
            else
            {
                oos.writeObject("false");
                break;
            }
        }

        String message;
        boolean going = true;
        while (going)
        {
            message = (String) ois.readObject();
            switch (message)
            {
                case "back":
                    going = false;
                    break;
                case "addModer":
                    addingModer();
                    going = false;
                    break;
                case "deleteModer":
                    deletingModer();
                    going = false;
                    break;
                default:
                    System.out.println("mist while creatin' moder");
            }
        }
    }

    public void addingModer() throws IOException, ClassNotFoundException, SQLException
    {
        String whatLogin = (String) ois.readObject();
        String whatPassword =(String) ois.readObject();
        String command = "insert into users(login, password, role) values (?, ?, ?)";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, whatLogin);
        preparedStatement.setString(2, whatPassword);
        preparedStatement.setString(3, "moderator");
        preparedStatement.executeUpdate();
    }


    public void deletingModer() throws IOException, ClassNotFoundException, SQLException
    {
        String loginToDelete = (String) ois.readObject();
        String command = "delete from users where login = ? and role = ?";
        preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, loginToDelete);
        preparedStatement.setString(2, "moderator");
        preparedStatement.executeUpdate();
    }
}
