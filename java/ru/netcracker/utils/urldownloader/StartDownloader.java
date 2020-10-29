package ru.netcracker.utils.urldownloader;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartDownloader{
    private URL url = null;
    private String path = null;
    private boolean open = false;

    public StartDownloader() {

    }

    /**
     * Введено все и url, и путь, и открыть (open/true)
     * @param url
     * @param path
     * @param open
     */
    public StartDownloader(String url, String path,String open){
        try {
            this.url = new URL(url);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        this.path=path;
        Pattern pattern = Pattern.compile("([Tt]rue)|([Oo]pen)");
        Matcher matcher = pattern.matcher(open);
        if (matcher.matches()){
            this.open = true;
        }
    }

    /**
     * Введено url  и либо путь либо открытие файла
     * @param url
     * @param str
     */
    public StartDownloader(String url, String str){
        try {
            this.url = new URL(url);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        Pattern pattern = Pattern.compile("([Tt]rue)|([Oo]pen)");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()){
            this.open = true;
            path = new File("").getAbsolutePath();
        }else {
            this.path = str;
            this.open = false;
        }
    }

    /**
     * Введен только url
     * @param url
     */
    public StartDownloader(String url){
        try {
            this.url=new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        path=new File("").getAbsolutePath();
        this.open=false;
    }
    public void startDownload(){
        URLDownloader down = new URLDownloader();
        if(down.download(url,path).getAbsolutePath().endsWith("html")){
            down.subdirectoryDownload();
        }
        if (open){
            down.open();
        }
    }

}
