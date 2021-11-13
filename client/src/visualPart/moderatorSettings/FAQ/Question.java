package visualPart.moderatorSettings.FAQ;

public class Question
{
    protected String login, question;

    public Question(String loginAc, String questionAc)
    {
        this.login = loginAc;
        this.question = questionAc;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
