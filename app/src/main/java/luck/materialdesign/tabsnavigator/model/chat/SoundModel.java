package luck.materialdesign.tabsnavigator.model.chat;

/**
 * Created by BeS on 30.08.2017.
 */

public class SoundModel {
    private int position;
    private boolean playing;


    public SoundModel() {
    }

    public SoundModel(int position, boolean playing) {
        this.position = position;
        this.playing = playing;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

}
