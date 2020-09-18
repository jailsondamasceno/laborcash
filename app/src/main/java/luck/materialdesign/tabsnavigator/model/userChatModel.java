package luck.materialdesign.tabsnavigator.model;

/**
 * Created by BeS on 15.09.2017.
 */

public class userChatModel {
    String dialog;
    String user;
    String icon;
    String lastMessage;

    public userChatModel() {
    }

    public userChatModel(String dialog, String user, String icon, String lastMessage) {
        this.user = user;
        this.dialog = dialog;
        this.icon = icon;
        this.lastMessage = lastMessage;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
