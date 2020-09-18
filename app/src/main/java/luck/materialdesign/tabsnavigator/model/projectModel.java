package luck.materialdesign.tabsnavigator.model;

/**
 * Created by BeS on 13.09.2017.
 */

public class projectModel {

    String price;
    String name;
    String timeStamp;
    String description;
    String author;
    String skills;
    String group;
    String userChild;
    Long bid_count;
    String type;

    public projectModel(){

    }

    public projectModel(String price, String name, String group, String timeStamp, String description, String author, String skills, String userChild, String type) {
        this.price = price;
        this.name = name;
        this.timeStamp = timeStamp;
        this.group = group;
        this.description = description;
        this.author = author;
        this.skills = skills;
        this.userChild = userChild;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserChild() {
        return userChild;
    }

    public void setUserChild(String userChild) {
        this.userChild = userChild;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }


    public Long getBid_count() {
        return bid_count;
    }

    public void setBid_count(Long bid_count) {
        this.bid_count = bid_count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
