package visualPart.adminSettings.workWithAllTariffPlans;

public class Tariff
{
    protected String tariffPlanName;
    protected Double cost;
    protected String speed;

    public Tariff(String whatTariffPanName, Double whatCost, String whatSpeed)
    {
        this.cost = whatCost;
        this.speed = whatSpeed;
        this.tariffPlanName = whatTariffPanName;
    }

    public String getTariffPlanName() {
        return tariffPlanName;
    }

    public void setTariffPlanName(String tariffPlanName) {
        this.tariffPlanName = tariffPlanName;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
