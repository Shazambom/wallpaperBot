import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Shazambom on 7/22/2015.
 */
public class Main {
    public static void main(String[] args) {
        Organizer organizer = new Organizer("http://boards.4chan.org/w/", "/Users/ian/Desktop/RippedWallpapers/");
        organizer.runRip();
//        Timer timer = new Timer();
//        TimerTask hourlyTask = new TimerTask() {
//            @Override
//            public void run () {
//                Organizer organizer = new Organizer("http://boards.4chan.org/w/", "F:\\RippedWallpapers\\");
//                organizer.runRip();
//            }
//        };
//        timer.schedule(hourlyTask, 0l, 1000*60*60*3);
    }
}
