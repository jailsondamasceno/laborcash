package luck.materialdesign.tabsnavigator.model;

import com.google.firebase.database.Exclude;

/**
 * Created by BeS on 09.09.2017.
 */

public class feedbackModel {

    private String user;
    private String feedback;
    private String icon;
    private Long stars;


    public feedbackModel() {
    }

    public feedbackModel(String user, String icon, String feedback, Long stars) {
        this.user = user;
        this.feedback = feedback;
        this.stars = stars;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Long getStars() {
        return stars;
    }

    public void setStars(Long stars) {
        this.stars = stars;
    }

}

