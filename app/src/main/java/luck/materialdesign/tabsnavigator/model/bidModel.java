package luck.materialdesign.tabsnavigator.model;

/**
 * Created by BeS on 13.09.2017.
 */

public class bidModel {

    String message;
    String bid;
    String user;
    String dur;
    String icon;

    public bidModel(String user, String bid,String message, String icon) {
        this.message = message;
        this.user = user;
        this.bid = bid;
        this.icon = icon;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public bidModel() {
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }


}
