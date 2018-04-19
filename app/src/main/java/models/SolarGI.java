package models;

/**
 * Created by vinee_000 on 16-01-2018.
 */

public class SolarGI {
    String month;
    float pastEnergy;
    float targetEnergy;

    public SolarGI(String month, float pastEnergy, float targetEnergy) {
        this.month = month;
        this.pastEnergy = pastEnergy;
        this.targetEnergy = targetEnergy;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getPastEnergy() {
        return pastEnergy;
    }

    public void setPastEnergy(float pastEnergy) {
        this.pastEnergy = pastEnergy;
    }

    public float getTargetEnergy() {
        return targetEnergy;
    }

    public void setTargetEnergy(float targetEnergy) {
        this.targetEnergy = targetEnergy;
    }


}
