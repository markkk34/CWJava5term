package visualPart.adminSettings.workWithModerators;

public class Moder
{
    protected String login, password;

    public Moder(String whatLogin, String whatPassword)
    {
        this.login = whatLogin;
        this.password = whatPassword;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
