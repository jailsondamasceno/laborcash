package luck.materialdesign.tabsnavigator.model;

import com.google.firebase.database.Exclude;

/**
 * Created by BeS on 09.09.2017.
 */

public class accountFreeModel {


    private String specgroup;
    private String skills;
    private String finished;
    private String current;
    private String rating;
    private String stars;
    private String budget;
    private Long feedback_count;
    private feedbackModel feedback;

    public accountFreeModel() {
    }

    public accountFreeModel(String specgroup, String skills, String finished, String current, String rating, String stars, String budget, feedbackModel feedback) {
        this.specgroup = specgroup;
        this.skills = skills;
        this.finished = finished;
        this.current = current;
        this.rating = rating;
        this.stars = stars;
        this.budget = budget;
        this.feedback = feedback;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
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

    public String getSpecgroup() {
        return specgroup;
    }

    public void setSpecgroup(String specgroup) {
        this.specgroup = specgroup;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }
}

