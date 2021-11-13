package visualPart.moderatorSettings.workWithUsersAndTariffs;

public class UserTariffPlan
{
    protected String login, tariffPlan, password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserTariffPlan(String whatLogin, String whatTariff, String whatPassword)
    {
        this.login = whatLogin;
        this.tariffPlan = whatTariff;
        this.password = whatPassword;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTariffPlan() {
        return tariffPlan;
    }

    public void setTariffPlan(String tariffPlan) {
        this.tariffPlan = tariffPlan;
    }
}
