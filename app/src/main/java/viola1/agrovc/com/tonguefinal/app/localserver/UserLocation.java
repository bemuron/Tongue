package viola1.agrovc.com.tonguefinal.app.localserver;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */


public class UserLocation {
    private String country;
    private String district;
    private String parish;
    private String subparish;
    private String village;

    private String opt_country;
    private String opt_district;
    private String opt_parish;
    private String opt_subparish;
    private String opt_village;


    public UserLocation(String country, String district, String parish, String subparish, String village) {
        this.country = country;
        this.district = district;
        this.parish = parish;
        this.subparish = subparish;
        this.village = village;
    }

    public UserLocation() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public String getSubparish() {
        return subparish;
    }

    public void setSubparish(String subparish) {
        this.subparish = subparish;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getOpt_country() {
        return opt_country;
    }

    public void setOpt_country(String opt_country) {
        this.opt_country = opt_country;
    }

    public String getOpt_district() {
        return opt_district;
    }

    public void setOpt_district(String opt_district) {
        this.opt_district = opt_district;
    }

    public String getOpt_parish() {
        return opt_parish;
    }

    public void setOpt_parish(String opt_parish) {
        this.opt_parish = opt_parish;
    }

    public String getOpt_subparish() {
        return opt_subparish;
    }

    public void setOpt_subparish(String opt_subparish) {
        this.opt_subparish = opt_subparish;
    }

    public String getOpt_village() {
        return opt_village;
    }

    public void setOpt_village(String opt_village) {
        this.opt_village = opt_village;
    }
}

