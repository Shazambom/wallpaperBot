import java.util.ArrayList;

/**
 * Created by Shazambom on 7/22/2015.
 */
public class Main {
    public static void main(String[] args) {
        ThreadRipper threadRipper = new ThreadRipper("F:\\RippedWallpapers\\");
        System.out.println("Finding threads...");
        ArrayList<String> threadUrls = threadRipper.getThreads("http://boards.4chan.org/w/");
        System.out.println(threadUrls.size() + " threads found");
        System.out.println("Ripping threads now...");
        for (int i = 1; i < threadUrls.size(); i++) {
            threadRipper.RipThread(threadUrls.get(i));
        }
        System.out.println("\nBoard Rip complete");
        System.out.println(threadRipper.getTotal() + " images Ripped");
        System.out.println("Cleaning up foulder...");
        threadRipper.cleanUp();
        System.out.println("Cleanup complete");

    }
}
