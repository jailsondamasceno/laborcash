package luck.materialdesign.tabsnavigator.model;

/**
 * Created by BeS on 13.09.2017.
 */

public class userProjectModel {
    String status;
    String type;
    String rating;
    String paidComis;
    projectModel proj;

    public userProjectModel() {
    }

    public userProjectModel(String status, String type, String rating, projectModel proj) {
        this.status = status;
        this.type = type;
        this.rating = rating;
        this.proj = proj;
    }

    public String getPaidComis() {
        return paidComis;
    }

    public void setPaidComis(String paidComis) {
        this.paidComis = paidComis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public projectModel getProj() {
        return proj;
    }

    public void setProj(projectModel proj) {
        this.proj = proj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
