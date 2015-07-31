import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Shazambom on 7/22/2015.
 */
public class Main {
    public static void main(String[] args) {
        Organizer organizer = new Organizer("http://boards.4chan.org/w/", "F:\\RippedWallpapers\\");
        organizer.runRip();

//        Timer timer = new Timer();
//        TimerTask hourlyTask = new TimerTask() {
//            @Override
//            public void run () {
//                ThreadRipper threadRipper = new ThreadRipper("F:\\RippedWallpapers\\");
//                System.out.println("Finding threads...");
//                ArrayList<String> threadUrls = threadRipper.getThreads("http://boards.4chan.org/w/");
//                System.out.println(threadUrls.size() + " threads found");
//                System.out.println("Ripping threads now...");
//                for (int i = 1; i < threadUrls.size(); i++) {
//                    threadRipper.RipThread(threadUrls.get(i));
//                }
//                System.out.println("\nBoard Rip complete");
//                System.out.println(threadRipper.getTotal() + " images Ripped");
//                System.out.println("Cleaning up folder...");
//                threadRipper.cleanUp();
//                System.out.println("Cleanup complete");
//            }
//        };
//        timer.schedule(hourlyTask, 0l, 1000*60*60*2);

//        ThreadRipper threadRipper = new ThreadRipper("F:\\Testing\\");
//        System.out.println("Cleaning up folder...");
//        threadRipper.cleanUp();
//        System.out.println("Cleanup complete");

    }
}
