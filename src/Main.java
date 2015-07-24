import java.util.ArrayList;

/**
 * Created by Shazambom on 7/22/2015.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Finding threads...");
        ArrayList<String> threadUrls = ThreadRipper.getThreads("http://boards.4chan.org/w/");
        System.out.println(threadUrls.size() + " threads found");
        System.out.println("Ripping threads now...");
        for (int i = 1; i < threadUrls.size(); i++) {
            ThreadRipper.RipThread(threadUrls.get(i), "F:\\RippedWallpapers\\");
        }
        System.out.println("\nBoard Rip complete");
    }
}
