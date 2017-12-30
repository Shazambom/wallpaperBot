package main.java;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.UserAgent;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class ThreadRipper {
    private int total;
    private HashMap<Integer, String> duplicateNames;
    private String filePath;
    private File duplicates;

    public ThreadRipper(String filePath) {
        this.filePath = filePath;
        duplicates = initDuplicates();
        total = 0;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }


    public void RipThread(String url) {
        try {
            UserAgent userAgent = new UserAgent();
            userAgent.visit(url);
            System.out.println(userAgent.doc.getUrl());
            Elements fileNames = userAgent.doc.findEach("<a class=\"fileThumb\">");
            System.out.println(fileNames.size() + " images found");
            ArrayList<String> links = new ArrayList<String>();
            ArrayList<String> names = new ArrayList<String>();
            for (Element element: fileNames) {
                links.add(element.toString());
            }
            for (int i = 0; i < links.size(); i++) {
                links.set(i, parseLink(links.get(i)));
                names.add(parseThreadName(userAgent.doc.getUrl()) + "_" + parseFileName(links.get(i)));
            }
            String[] folder = new File(filePath).list();
            HashMap<Integer, String> files = new HashMap<Integer, String>();
            if (folder != null) {
                for (String element : folder) {
                    files.put(element.hashCode(), element);
                }
            }

            System.out.print("[");
            FileOutputStream duplicateStream;
            try {
                duplicateStream = new FileOutputStream(duplicates, true);
            } catch (Exception e) {
                e.printStackTrace();
                duplicateStream = null;
            }
            int success = downloadImages(links, names, userAgent, files, duplicateStream);
            if(duplicateStream != null) {
                try {
                    duplicateStream.flush();
                    duplicateStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("]");
            System.out.println(success + " unique images successfully downloaded");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private int downloadImages(List<String> links, List<String> names, UserAgent userAgent, HashMap<Integer, String> files, FileOutputStream duplicateStream) {
        int success = 0;
        for (int i = 0; i < links.size(); i++) {
            if (!duplicateNames.containsValue(names.get(i))) {
                if ((files.size() == 0 || !files.containsValue(names.get(i)))) {
                    try {
                        userAgent.download(links.get(i), new File(filePath + names.get(i)));
                        if(duplicateStream != null) {
                            try {
                                duplicateStream.write((names.get(i) + "\n").getBytes());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        success++;
                        total++;
                        System.out.print("#");
                    } catch (Exception e) {
                        System.out.println("\nFile: " + (i + 1) + " at the url: " + links.get(i) + " failed to download");
                        success += downloadImages(links.subList(i + 1, links.size()), names.subList(i + 1, names.size()), userAgent, files, duplicateStream);
                        i = links.size();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        return success;
    }


    private String parseLink(String link) {
        String toReturn = "";
        for (int i = 0; i < link.length(); i++) {
            if (link.substring(i, i + 7).equals("http://")){
                while (link.charAt(i) != '\"') {
                    toReturn += link.charAt(i);
                    i++;
                }
                return (toReturn);
            }
        }
        return toReturn;
    }
    private String parseFileName(String link) {
        for (int i = 0; i < link.length(); i++) {
            if (link.substring(i, i + 3).equals("/w/")) {
                return link.substring(i + 4);
            }
        }
        return "";
    }


    public ArrayList<String> getThreads(String url) {
        ArrayList<String> threadUrls = new ArrayList<String>();
        try {
            UserAgent userAgent = new UserAgent();
            userAgent.visit(url);
            Elements clickHere = userAgent.doc.findEvery("<a class=\"replylink\">");
            for (Element element: clickHere) {
                threadUrls.add(parseLink(element.toString()));
            }
            removeCopyCats(threadUrls);
            try {
                userAgent.doc.submit("Next");
            } catch (Exception e) {
                return threadUrls;
            }
            threadUrls.addAll(getThreads(userAgent.doc.getUrl()));

        } catch(Exception e) {
            e.printStackTrace();
        }
        return threadUrls;
    }

    private void removeCopyCats(ArrayList<String> threadUrls) {
        ArrayList<String> copyCatUrls = new ArrayList<String>();
        for (String element: threadUrls) {
            if (element.matches("http://boards\\.4chan\\.org/w/thread/[0-9]+/.+")) {
                String copyCatUrl = "";
                int slashCount = 0;
                for (int i = 0; i < element.length(); i++) {
                    if (element.charAt(i) == '/') {
                        slashCount++;
                    }
                    if (slashCount == 6) {
                        break;
                    }
                    copyCatUrl += element.charAt(i);
                }
                copyCatUrls.add(copyCatUrl);
            }
        }
        for (String element: copyCatUrls) {
            threadUrls.remove(element);
        }
    }

    public int getTotal() {
        return total;
    }

    public void cleanUp() {
        ArrayList<File> folder = new ArrayList<File>();
        try {
            for (File element : new File(filePath).listFiles()) {
                folder.add(element);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<File, String> checkSum = new HashMap<>();
        System.out.println("Computing Checksums...");
        int percentage = (int)Math.ceil(folder.size() / 100);
        System.out.print("[");
        for (int i = 0; i < folder.size(); i++) {
            checkSum.put(folder.get(i), getHash(folder.get(i)));
            if (i % percentage == 0) {
                System.out.print("#");
            }
        }
        System.out.println("]");
        ArrayList<File> toRemove = new ArrayList<File>();
        try {
            System.out.println("Checking for duplicates...");
            System.out.print("[");
            for (int i = 0; i < folder.size(); i++) {
                if (!toRemove.contains(folder.get(i))) {
                    String suffix = getFileSuffix(folder.get(i).getPath());
                    if (suffix.equals("jpeg") || suffix.equals("png")
                            || suffix.equals("jpg") || suffix.equals("apng")
                            || suffix.equals("bmp") || suffix.equals("tiff")
                            || suffix.equals("tif") || suffix.equals("xcf")
                            || suffix.equals("pdf")) {
                        Dimension fileDim = getImageDim(folder.get(i).getPath(), suffix);
                        if (fileDim.getHeight() < 720 || fileDim.getWidth() < 1080) {
                            toRemove.add(folder.get(i));
                        } else {
                            for (int j = i + 1; j < folder.size(); j++) {
                                if (folder.get(i).isFile() && folder.get(j).isFile()
                                        && FileUtils.sizeOf(folder.get(i)) == FileUtils.sizeOf(folder.get(j))
                                        && checkSum.get(folder.get(i)).equals(checkSum.get(folder.get(j)))){
                                    toRemove.add(folder.get(j));
                                }
                            }
                        }
                    } else if (suffix.equals("webm")) {
                        toRemove.add(folder.get(i));
                    }
                }
                if (i % percentage == 0) {
                    System.out.print("#");
                }
            }
            System.out.println("]");
            System.out.println(toRemove.size() + " invalid wallpapers found");
            System.out.println("Resolving invalid wallpapers...");
            resolveDuplicates(toRemove);
        } catch (Exception e) {
            System.out.println("Well shit");
            StringBuilder sb = new StringBuilder(e.toString());
            for (StackTraceElement ste : e.getStackTrace()) {
                sb.append("\n\tat ");
                sb.append(ste);
            }
            String trace = sb.toString();
            System.out.println(trace);
        }
    }
    private String getHash(File file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream in = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            DigestInputStream dis = new DigestInputStream(in, md);
            try {
                while (dis.read(buffer) != -1);
            } finally {
                dis.close();
            }
            return bytesToHex(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
    private void resolveDuplicates(ArrayList<File> toRemove) {
        System.out.print("[");
        for (File element: toRemove) {
            try {
                Files.delete(element.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.print("#");
        }
        System.out.println("]");
    }
    private File initDuplicates() {
        try {
            duplicateNames = new HashMap<Integer, String>();
            File duplicates = new File(new File(filePath).getParentFile().getPath() + "/duplicates.txt");
            FileReader fileReader = new FileReader(duplicates);
            BufferedReader in = new BufferedReader(fileReader);

            String line;
            while ((line = in.readLine()) != null) {
                duplicateNames.put(line.hashCode(), line);
            }
            in.close();
            fileReader.close();
            return duplicates;

        } catch (Exception e) {
            try {
                PrintWriter out = new PrintWriter(new File(new File(filePath).getParentFile().getPath() + "/duplicates.txt"));
                out.print("\n");
                out.close();
                return new File(new File(filePath).getParentFile().getPath() + "/duplicates.txt");
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
        return new File(new File(filePath).getParentFile().getPath() + "/duplicates.txt");
    }

    private String parseThreadName(String threadName) {
        String name = threadName.substring(33);
        String toReturn = "";
        boolean isName = false;
        for (int i = 0; i < name.length(); i++) {
            if (isName) {
                toReturn += name.charAt(i);
            }
            if (name.charAt(i) == '/') {
                isName = true;
            }
        }
        return toReturn;
    }

    private Dimension getImageDim(final String path, String suffix) {
        Dimension result = null;
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        if (iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                ImageInputStream stream = new FileImageInputStream(new File(path));
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                result = new Dimension(width, height);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reader.dispose();
            }
        } else {
            System.out.println("No reader found for given format: " + suffix);
        }
        return result;
    }

    private String getFileSuffix(final String path) {
        String result = null;
        if (path != null) {
            result = "";
            if (path.lastIndexOf('.') != -1) {
                result = path.substring(path.lastIndexOf('.'));
                if (result.startsWith(".")) {
                    result = result.substring(1);
                }
            }
        }
        return result;
    }

}
