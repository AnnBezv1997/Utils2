package ru.netcracker.utils.achivecomparator;

import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.List;
import java.util.Enumeration;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Класс хранит информацию о архивах
 */
public class Achives extends JarFile{

    private List<File> files;
    private int size;

    public Achives(String name) throws IOException{ //если произойдет ошибка ввода вывода
        super(name);
        Enumeration<JarEntry> enums = super.entries(); // перечисление записей zip-файла
        this.files = new ArrayList<File>();

        while (enums.hasMoreElements()){ //он обязан вернуть true, пока всё ещё существуют элементы для извлечения, и false, когда все элементы были перечислены
            JarEntry jarEntry = enums.nextElement(); //Возвращает следующий объект в перечислении
            this.files.add(new File(jarEntry.getName(), jarEntry.getSize()));
        }
        this.size = this.files.size();

    }
    //хранит файлы
    public List<File> getFiles(){
        return files;
    }
    //количество файлов
    public int getSize(){
        return size;
    }
}
