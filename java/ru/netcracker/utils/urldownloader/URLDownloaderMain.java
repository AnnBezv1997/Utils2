package ru.netcracker.utils.urldownloader;
import java.net.MalformedURLException;
import java.net.URL;

public class URLDownloaderMain {
    public static void main(String[] args){
        StartDownloader sd = null;
        switch (args.length){
            case 1:
                sd = new StartDownloader(args[0]);
                break;
            case 2:
                sd = new StartDownloader(args[0], args[1]);
                break;
            case 3:
                sd = new StartDownloader(args[0], args[1], args[2]);
        }

        sd.startDownload();
    }
}
