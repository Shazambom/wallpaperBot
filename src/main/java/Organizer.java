package main.java;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Shazambom on 7/30/2015.
 */
public class Organizer {
    private ThreadRipper ripper;
    private GregorianCalendar date;
    private String url;
    private String filePath;
    public Organizer(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
        this.date = new GregorianCalendar();
        initRipper();
    }

    public void runRip() {
        System.out.println("Finding threads...");
        ArrayList<String> threadUrls = ripper.getThreads(url);
        System.out.println(threadUrls.size() + " threads found");
        System.out.println("Ripping threads now...");
        for (int i = 1; i < threadUrls.size(); i++) {
            ripper.RipThread(threadUrls.get(i));
        }
        System.out.println("\nBoard Rip complete");
        System.out.println(ripper.getTotal() + " images Ripped");
        System.out.println("Cleaning up folder...");
        ripper.cleanUp();
        System.out.println("Cleanup complete");
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
        initRipper();
    }

    private void initRipper() {
        int weekNum = date.get(GregorianCalendar.WEEK_OF_YEAR);
        int yearNum = date.get(GregorianCalendar.YEAR);
        if (!(new File(filePath + "Y" + yearNum + "-" + "W" + weekNum + "/").isDirectory())) {
            try {
                Files.createDirectory(new File(filePath + "Y" + yearNum + "-" + "W" + weekNum + "/").toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ripper = new ThreadRipper(filePath + "Y" + yearNum + "-" + "W" + weekNum + "/");
    }

}
