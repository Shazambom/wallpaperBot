

/**
 * Created by Shazambom on 7/22/2015.
 */
public class Main {
    public static void main(String[] args) {
        Organizer organizer = new Organizer("http://boards.4chan.org/w/", "/Users/ian/Desktop/RippedWallpapers/");
//        Organizer organizer = new Organizer("http://boards.4chan.org/w/", "/media/UNTITLED/Wallpapers/");
        organizer.runRip();
//        Runtime rt = Runtime.getRuntime();
//        try {
//            rt.exec("python /home/pi/GitHub/wallpaperBot/src/imgurbot.py");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
