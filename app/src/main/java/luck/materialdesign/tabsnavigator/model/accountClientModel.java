package luck.materialdesign.tabsnavigator.model;

import com.google.firebase.database.Exclude;

/**
 * Created by BeS on 09.09.2017.
 */

public class accountClientModel {


    private String published;
    private String finished;
    private Long feedback_count;
    private String stars;
    private feedbackModel feedback;

    public accountClientModel() {
    }

    public accountClientModel(String published, String finished, String rating, String stars, feedbackModel feedback) {
        this.published = published;
        this.finished = finished;
        this.stars = stars;
        this.feedback = feedback;
    }

    public Long getFeedback_count() {
        return feedback_count;
    }

    public void setFeedback_count(Long feedback_count) {
        this.feedback_count = feedback_count;
    }

    public feedbackModel getFeedback() {
        return feedback;
    }

    public void setFeedback(feedbackModel feedback) {
        this.feedback = feedback;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }
}

