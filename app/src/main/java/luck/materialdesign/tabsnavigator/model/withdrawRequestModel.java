package luck.materialdesign.tabsnavigator.model;

/**
 * Created by BeS on 26.09.2017.
 */

public class withdrawRequestModel {

    String timeStamp;
    String amount;
    String type;

    public withdrawRequestModel(String timeStamp, String amount, String type) {
        this.timeStamp = timeStamp;
        this.amount = amount;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
