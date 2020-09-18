package luck.materialdesign.tabsnavigator.utils;

import android.support.v4.app.FragmentTransaction;

/**
 * Created by BeS on 20.08.2017.
 */

public class categoryAlbum {
    private int icon;
    private int color;
    private String name;
    private Long count;

    private FragmentTransaction fTrans;

    public categoryAlbum() {
    }



    public categoryAlbum(int icon, String name, Long count, int color) {
        this.fTrans = fTrans;
        this.icon = icon;
        this.name = name;
        this.count = count;
        this.color = color;
    }

    public int getIcon() { return icon;}
    public int getColor() { return color; }
    public String getName() {
        return name;
    }
    public Long getCount() {
        return count;
    }
    public FragmentTransaction getfTrans() {return fTrans;}
}
