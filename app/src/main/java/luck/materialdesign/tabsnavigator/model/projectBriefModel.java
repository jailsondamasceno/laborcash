package luck.materialdesign.tabsnavigator.model;

/**
 * Created by BeS on 13.09.2017.
 */

public class projectBriefModel {
    String name;
    String timeStamp;
    String bids;
    String avgBid;

    public projectBriefModel(){

    }

    public projectBriefModel(String name, String timeStamp, String bids, String avgBid) {
        this.name = name;
        this.timeStamp = timeStamp;
        this.bids = bids;
        this.avgBid = avgBid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getBids() {
        return bids;
    }

    public void setBids(String bids) {
        this.bids = bids;
    }

    public String getAvgBid() {
        return avgBid;
    }

    public void setAvgBid(String avgBid) {
        this.avgBid = avgBid;
    }
}
