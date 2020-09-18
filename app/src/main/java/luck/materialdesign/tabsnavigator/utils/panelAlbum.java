package luck.materialdesign.tabsnavigator.utils;

import android.support.v4.app.FragmentTransaction;

/**
 * Created by BeS on 08.09.2017.
 */

public class panelAlbum {
    private int icon;
    private String text;
    private String btnText;

    public panelAlbum() {
    }

    public panelAlbum(int icon, String text, String btnText) {
        this.icon = icon;
        this.text = text;
        this.btnText = btnText;
    }

    public int getIcon() { return icon;}
    public String getText() {
        return text;
    }
    public String getBtnText() {
        return btnText;
    }

}
