package ru.netcracker.utils.achivecomparator;
import ru.netcracker.utils.achivecomparator.Qualities.StatusFile;

/**
 * информация о файле который хранится в архиве имя размер файла и статус
 */

public class File {
    private String nameFile;
    private long size;
    private StatusFile status;

    public File(String nameFile, long size){
        this.nameFile = nameFile;
        this.size = size;
        this.status = StatusFile.DEFAULT;
    }
    public String getNameFile(){
        return nameFile;
    }
    public long getSize(){
        return size;
    }

    public StatusFile getStatus() {
        return status;
    }

    public void setStatus(StatusFile status) {
        this.status = status;
    }
}
