package visualPart.adminSettings.workWithTariffPlans;

public class Users
{
    protected String loginInTable;
    protected String nameInTable;
    protected String tariffInTable;
    protected Double balanceInTable;

    public Users(String login, String name, String tariff, Double balance)
    {
        this.loginInTable = login;
        this.nameInTable = name;
        this.tariffInTable = tariff;
        this.balanceInTable = balance;
    }

    public String getLoginInTable() {
        return loginInTable;
    }

    public void setLoginInTable(String loginInTable) {
        this.loginInTable = loginInTable;
    }

    public String getNameInTable() {
        return nameInTable;
    }

    public void setNameInTable(String nameInTable) {
        this.nameInTable = nameInTable;
    }

    public String getTariffInTable() {
        return tariffInTable;
    }

    public void setTariffInTable(String tariffInTable) {
        this.tariffInTable = tariffInTable;
    }

    public Double getBalanceInTable() {
        return balanceInTable;
    }

    public void setBalanceInTable(Double balanceInTable) {
        this.balanceInTable = balanceInTable;
    }
}
