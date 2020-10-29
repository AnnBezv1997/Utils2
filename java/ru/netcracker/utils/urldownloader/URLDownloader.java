package ru.netcracker.utils.urldownloader;


import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLDownloader {
    private ParserURL parser;
    private File file;
    private File uploadedFile;
    private List<File> allRsc = new ArrayList<>();
    private List<String> allPathsRsc = new ArrayList<>();

    /**
     * Читает данные с url создает файл и записывает эти данные в файл.
     *
     * @param url которую сохарнить  и path путь куда хотим сохранить файл
     * @return file - результат скачивания
     */
    public File download(URL url, String path) {
        OutputStream output = null;
        InputStream input = null;
        URLConnection urlConnection = null;
        try {
            boolean login = true;
            parser = new ParserURL(url);
            while (true) {
                if (!login) {
                    url = parser.getURL();
                }
                urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(2 * 1000);
                try {
                    urlConnection.connect();
                    input = urlConnection.getInputStream();
                    break;
                } catch (IOException e) {
                    if (e.getMessage().contains("FtpProtocolException")) {
                        login = false;
                    } else {
                        if (e.getMessage().contains("Invalid username/password")) {
                            System.out.println("Invalid username/password");
                        } else {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            }
            if (input == null) return null;
            file = new File(path + parser.getFileName());
            File directory = new File(path);
            try {
                if (directory.exists()) {
                    output = new FileOutputStream(file);
                } else {
                    output = new FileOutputStream(fileNoPath(url, path, parser));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] buff = new byte[64 * 1024];
            int i =0;
            while ((i = input.read(buff)) > 0) {
                output.write(buff, 0, i);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return file;
    }


    private File fileNoPath(URL url, String path, ParserURL parser) {
        String defPath = new File("").getAbsolutePath();
        StringBuilder nameFileNoPath = null;
        if (url.getPath().contains(".")) {
            nameFileNoPath = new StringBuilder(path.replace("/", " ")).append(parser.getFileExtension());
        } else {
            nameFileNoPath = new StringBuilder(path.replace("/", " ")).append(".html");
        }
        file = new File(defPath + "/" + nameFileNoPath.toString());
        return file;
    }

    /**
     * В поддиректорию следует сохранять имеющиеся на странице картинки (указанные в тегах img),
     * а также другие (указанные в тегах link) ресурсы, необходимые для корректного отображения страницы.
     */
    public void subdirectoryDownload() {
        String str;
        File upFile = file;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(upFile);
            byte[] text = new byte[fileInputStream.available()];
            fileInputStream.read(text);
            str = new String(text);
            String defName = upFile.getName().substring(0, upFile.getName().indexOf("."));
            String defPath = upFile.getAbsolutePath().substring(0, upFile.getPath().indexOf(".") - defName.length());
            File dir = new File(defPath + "/" + defName + "_files");
            getResources(str, dir);
            uploadedFile = replacePathResources(allRsc, str, allPathsRsc, upFile);
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getResources(String str, File dir) throws MalformedURLException {
        Pattern p = Pattern.compile("(<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>)");
        Matcher m = p.matcher(str);
        while (m.find()) {
            Pattern pattern = Pattern.compile("(((http[s]?)|ftp)://.+\\.((jpg)|(png)|(gif)))");
            Matcher matcher = pattern.matcher(m.group(1));
            if (matcher.find()) {
                if (!dir.exists()) {
                    try {
                        dir.mkdir();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                allPathsRsc.add(matcher.group(1));
                allRsc.add(download(new URL(matcher.group(1)), dir.getAbsolutePath()));
            }
        }

        p = Pattern.compile("(<link[^>]+href\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>)");
        m = p.matcher(str);
        while (m.find()) {
            Pattern pattern = Pattern.compile("(((http[s]?)|ftp)://.+\\.(.)+(\"))");
            Matcher matcher = pattern.matcher(m.group(1));
            if (matcher.find()) {
                if (!dir.exists()) {
                    try {
                        dir.mkdir();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                allPathsRsc.add(matcher.group(1).substring(0, matcher.group(1).length() - 1));
                allRsc.add(download(new URL(matcher.group(1).substring(0, matcher.group(1).length() - 1)), dir.getAbsolutePath()));
            }
        }
    }

    private File replacePathResources(List<File> img, String str, List<String> path, File upFile) {
        String newStr;
        FileOutputStream fileOutputStream;
        try {
            upFile.delete();
            upFile.createNewFile();
            newStr = str;
            for (int i = 0; i < img.size(); i++) {
                newStr = newStr.replace(path.get(i), img.get(i).getAbsolutePath());
            }
            byte[] buff = newStr.getBytes();
            fileOutputStream = new FileOutputStream(upFile);
            fileOutputStream.write(buff);
            fileOutputStream.flush();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return upFile;
    }


    public void open() {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }
        try {
            if (uploadedFile != null) {
                desktop.open(uploadedFile);
            } else {
                desktop.open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}