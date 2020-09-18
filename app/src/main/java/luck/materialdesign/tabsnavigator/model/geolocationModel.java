package luck.materialdesign.tabsnavigator.model;

import com.google.firebase.database.Exclude;

/**
 * Created by BeS on 09.09.2017.
 */

public class geolocationModel {

    private String latitude;
    private String longitude;

    public geolocationModel(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

