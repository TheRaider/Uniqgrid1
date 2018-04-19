package models;

/**
 * Created by vinee_000 on 20-01-2018.
 */

public class Asset {
    private String category;
    private String assetType;
    private String wattage;
    private String quantity;
    private String totalWattage;

    public Asset(String category, String assetType, String wattage, String quantity, String totalWattage) {
        this.category = category;
        this.assetType = assetType;
        this.wattage = wattage;
        this.quantity = quantity;
        this.totalWattage = totalWattage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getWattage() {
        return wattage;
    }

    public void setWattage(String wattage) {
        this.wattage = wattage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalWattage() {
        return totalWattage;
    }

    public void setTotalWattage(String totalWattage) {
        this.totalWattage = totalWattage;
    }
}
