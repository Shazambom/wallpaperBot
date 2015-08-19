import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Shazambom on 7/22/2015.
 */
public class Main {
    public static void main(String[] args) {
//        Organizer organizer = new Organizer("http://boards.4chan.org/w/", "/Users/ian/Desktop/RippedWallpapers/");
        Organizer organizer = new Organizer("http://boards.4chan.org/w/", "/media/UNTITLED/Wallpapers/");
        organizer.runRip();
    }
}
