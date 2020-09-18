package luck.materialdesign.tabsnavigator.utils;

import android.support.v4.app.FragmentTransaction;

/**
 * Created by BeS on 20.08.2017.
 */

public class album {
    private int icon;
    private String name;
    private String adress;
    private String open;
    private FragmentTransaction fTrans;

    public album() {
    }



    public album(FragmentTransaction fTrans, int icon, String name) {
        this.fTrans = fTrans;
        this.icon = icon;
        this.name = name;
        this.adress = adress;
        this.open = open;
    }

    public int getIcon() {
        return icon;
    }
    public String getName() {
        return name;
    }
    public String getAdress() {
        return adress;
    }
    public String getOpen() {
        return open;
    }
    public FragmentTransaction getfTrans() {return fTrans;}
}
