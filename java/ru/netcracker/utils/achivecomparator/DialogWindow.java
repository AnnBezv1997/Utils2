package ru.netcracker.utils.achivecomparator;

import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.Objects;
import javax.swing.*;

public class DialogWindow extends JFrame {
    private String routeOneFile = "";
    private String routeTwoFile = "";
    private String routeDirectory = "";

    DialogWindow(){
       JFileChooser fileopen = new JFileChooser();    // создаем новый объект JFileChooser
        fileopen.setFileFilter(new FileNameExtensionFilter("ZIP & JAR Archives","zip", "jar")); //фильтр для файлов
        int ret = fileopen.showDialog(null, "Open one file");
        if(ret == JFileChooser.APPROVE_OPTION){ // APPROVE_OPTION - выбор файла в диалоговом окне прошел успешно;
            routeOneFile = fileopen.getSelectedFile().getPath(); //записали путь к первому файлу
        }
        int ret1 = fileopen.showDialog(null,"Open two file");
        if(ret1 == JFileChooser.APPROVE_OPTION){
            routeTwoFile = fileopen.getSelectedFile().getPath();
        }
        int ret2 = fileopen.showDialog(null,"Open directory for saving output file");
        if(ret2 == JFileChooser.APPROVE_OPTION){
            routeDirectory = fileopen.getSelectedFile().getPath();
        }


    }

   public String getRouteOneFile() {
        return routeOneFile;
    }

    public String getRouteTwoFile() {
        return routeTwoFile;
    }
    String getDirectoryPath() {
        if (Objects.equals(routeDirectory, "")) {  //если директория не выбрана сохраним в корневой папке проекта
            return routeDirectory;
        }
        return routeDirectory + '/';
    }
}
