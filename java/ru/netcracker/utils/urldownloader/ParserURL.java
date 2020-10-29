package ru.netcracker.utils.urldownloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ParserURL {
    private URL url;
    private String fileExtension = null;
    private String[] parseUrl = null;

    public   ParserURL(URL url){
        this.url=url;
    }
    /**
     * В этом методе получаем из URL имя файла. Если только имя домена - 'index.html'.
     * Если есть и другие параметры - именем являются символы до расширения.
     * @return имя файла + расширение
     */
    public String getFileName(){
        String name = "";
        StringBuilder sb = new StringBuilder();
        parseUrl = url.getPath().split("/");
        if(url.getPath().isEmpty()){
            name =  "/index.html";
        }
        if(url.getPath().contains("/")){
            if (parseUrl[parseUrl.length-1].isEmpty()) {
               name = sb.append("/").append(parseUrl[parseUrl.length-2]).append(".html").toString();
            }else if(parseUrl[parseUrl.length-1].contains(".")){
                name = sb.append("/").append(parseUrl[parseUrl.length -1]).toString();
            }else {
                name = sb.append("/").append(parseUrl[parseUrl.length -1]).append(".html").toString();
            }
        }
        return name;
    }
     /**
     * Получаем расширение файла c точкой из URL
     * @return расшрирение файла
     */
     public String getFileExtension(){
         int index = parseUrl[parseUrl.length -1].indexOf(".");
         fileExtension = parseUrl[parseUrl.length-1].substring(index,parseUrl[parseUrl.length -1].length());
         return fileExtension;
     }

    /**
     *  ftp://<логин>:<пароль>@<хост>:<порт>/<путь-к-файлу>;
     * @return url
     */
    public URL getURL(){
        String ftp= "ftp://%s:%s@%s";
        Scanner sc = new Scanner(System.in);
        try {
            String login = sc.nextLine();
            String password = sc.nextLine();
            if (password.compareTo("")==0){
                password=" ";
            }
            String hostPath = url.getHost()+url.getPath();
            url=new URL(String.format(ftp, login, password,hostPath));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}

