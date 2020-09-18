package luck.materialdesign.tabsnavigator.model;

/**
 * Created by BeS on 16.09.2017.
 */

public class notificationModel {
    String project;
    String userChild;
    String userName;
    String group;
    String type;
    String icon;


    public notificationModel(String project, String userChild, String userName, String icon,  String group, String type) {
        this.project = project;
        this.userChild = userChild;
        this.userName = userName;
        this.group = group;
        this.type = type;
        this.icon = icon;
    }

    public notificationModel() {
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getUserChild() {
        return userChild;
    }

    public void setUserChild(String userChild) {
        this.userChild = userChild;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
