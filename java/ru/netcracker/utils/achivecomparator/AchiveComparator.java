package ru.netcracker.utils.achivecomparator;
import java.util.HashMap;
import java.util.Objects;
import ru.netcracker.utils.achivecomparator.Qualities.StatusFile;
/** Класс сравнивает два архива,результат HashMap.
 */
public class AchiveComparator {
 private Achives newAchive;
 private Achives oldAchive;

    public AchiveComparator(Achives newAchive,Achives oldAchive) {
        this.newAchive = newAchive;
        this.oldAchive = oldAchive;
    }
    public void compare(){
        this.findDelFiles();
        this.findNewFiles();
        this.findUpFiles();
        this.findReFiles();
    }

    /**
     * Ищет удаленные
     */
    private void findDelFiles(){
        for(int i = 0; i<oldAchive.getSize();i++){
            boolean check = true;
            for(int j=0;j<newAchive.getSize();j++){
                if(Objects.equals(newAchive.getFiles().get(j).getNameFile(), oldAchive.getFiles().get(i).getNameFile())) {
                    check = false;
                    break;
                }
            }
            if (check){
                oldAchive.getFiles().get(i).setStatus(StatusFile.DELETE);
            }
        }

    }

    /**
     * Ищет новые
     */
    private void findNewFiles(){
        for(int i = 0; i<newAchive.getSize();i++){
            boolean check = true;
            for(int j=0;j<oldAchive.getSize();j++){
                if(Objects.equals(newAchive.getFiles().get(i).getNameFile(), oldAchive.getFiles().get(j).getNameFile())) {
                    check = false;
                    break;
                }
            }
            if (check){
                newAchive.getFiles().get(i).setStatus(StatusFile.NEW);
            }
        }
    }

    /**
     * Ищет измененные
     */
    private void findUpFiles(){
        for(int i = 0; i<oldAchive.getSize();i++){
            for(int j=0;j<newAchive.getSize();j++){
              if (Objects.equals(newAchive.getFiles().get(j).getNameFile(),oldAchive.getFiles().get(i).getNameFile())
                      && newAchive.getFiles().get(j).getSize() != oldAchive.getFiles().get(i).getSize()){
                     newAchive.getFiles().get(j).setStatus(StatusFile.UPDATED);
                     oldAchive.getFiles().get(i).setStatus(StatusFile.UPDATED);
                     break;
              }
            }
        }
    }

    /**
     * ищет переименованные
     */
    private void findReFiles(){
        for(int i = 0; i<oldAchive.getSize();i++){
            for(int j=0;j<newAchive.getSize();j++){
                if (!Objects.equals(newAchive.getFiles().get(j).getNameFile(),oldAchive.getFiles().get(i).getNameFile())
                        && newAchive.getFiles().get(j).getSize() == oldAchive.getFiles().get(i).getSize()){
                    newAchive.getFiles().get(j).setStatus(StatusFile.RENAMED);
                    oldAchive.getFiles().get(i).setStatus(StatusFile.RENAMED);
                    break;
                }
            }
        }
    }

    /**
     * Возвращает HashMap ключ статус файла, значение имя файла.
     * @return
     */

    public HashMap<StatusFile,String> getMap(){
        HashMap<StatusFile,String> map = new HashMap<>();
        for (File file : oldAchive.getFiles()){
            if (file.getStatus() == StatusFile.RENAMED){
                for(File file1 : newAchive.getFiles()){
                    if (file.getSize() == file1.getSize()){
                        map.put(file.getStatus(),file.getNameFile() +'/'+file1.getNameFile());
                        break;
                    }
                }
            } else {
                map.put(file.getStatus(),file.getNameFile());
            }
        }
        for(File file : newAchive.getFiles()){
            if(Objects.equals(file.getStatus(),StatusFile.NEW)){
                map.put(file.getStatus(),file.getNameFile());
            }
        }
        return map;
    }

    public Achives getNewAchive(){
        return newAchive;
    }
    public Achives getOldAchive(){
        return oldAchive;
    }
}
