package luck.materialdesign.tabsnavigator.model;

import com.google.firebase.database.Exclude;

/**
 * Created by BeS on 09.09.2017.
 */

public class accountModel {
    public String getEmail() {
        return email;
    }

    private String email;
    private String name;
    private String photo_profile;
    private String country;
    private String aboutInfo;
    private String token;
    private String lastActive;
    private String publicKey;
    accountClientModel client;
    accountFreeModel freelancer;

    public accountModel() {
    }

    public accountModel(String name, String email, String photo_profile, String country, String aboutInfo, accountFreeModel accountFreeModel, accountClientModel accountClientModel) {
        this.name = name;
        this.email = email;
        this.photo_profile = photo_profile;
        this.country = country;
        this.aboutInfo = aboutInfo;
        client = accountClientModel;
        freelancer = accountFreeModel;

    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public accountClientModel getClient() {
        return client;
    }

    public void setClient(accountClientModel client) {
        this.client = client;
    }

    public accountFreeModel getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(accountFreeModel freelancer) {
        this.freelancer = freelancer;
    }

    public String getName() {return name;}
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAboutInfo() {
        return aboutInfo;
    }

    public void setAboutInfo(String aboutInfo) {
        this.aboutInfo = aboutInfo;
    }
}

