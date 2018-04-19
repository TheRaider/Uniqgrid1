package models;

/**
 * Created by vinee_000 on 16-01-2018.
 */

public class ConsumptionGI {
    String month;
    float consumption;

    public ConsumptionGI(String month, float consumption) {
        this.month = month;
        this.consumption = consumption;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getConsumption() {
        return consumption;
    }

    public void setConsumption(float consumption) {
        this.consumption = consumption;
    }
}
