package models;

/**
 * Created by vinee_000 on 15-01-2018.
 */

// LoadDistributionGraphItem

public class LoadDistGI {
    String category;
    double kW,percent;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getkW() {
        return kW;
    }

    public void setkW(double kW) {
        this.kW = kW;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public LoadDistGI(String category, double kW, double percent) {
        this.category = category;
        this.kW = kW;
        this.percent = percent;
    }
}
