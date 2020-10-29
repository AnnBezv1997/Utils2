package ru.netcracker.utils.achivecomparator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import ru.netcracker.utils.achivecomparator.Qualities.StatusFile;

/**
 * класс создает файл - содержащий результат сравнения архивов
 */

public class Print {
    private static final String ROUTE_DEFAULT_OUTPUT_TXT = "/";
    private static final String NAME_OUTPUT_FILE = "output.txt";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private AchiveComparator achiveComparator;
    private String routeDirectory;

    /**
     * конструктор если используем диалоговое окно
     */

    Print(String oldRouteAchive,String newRouteAchive,String routeDirectory) throws IOException{
        this.achiveComparator = new AchiveComparator(new Achives(oldRouteAchive),new Achives(newRouteAchive));
        this.routeDirectory = routeDirectory;
    }

    /**
     * конструктор если пути заданы из терминала
     * @param oldRouteAchive
     * @param newRouteAchive
     * @throws IOException
     */

    Print(String oldRouteAchive,String newRouteAchive) throws IOException{
        this(oldRouteAchive,newRouteAchive,ROUTE_DEFAULT_OUTPUT_TXT);
    }

    /**строковое представление
     *
     * @param name
     * @return
     */
    private String getDelFile (String name){
        return String.format("- %-28.28s|%-30.30s\n", name, "");
    }
    private String getNewFile (String name){
        return String.format("%-30.30s|+ %-28.28s\n", "", name);
    }
    private String getUpFile (String name){
        return String.format("* %-28.28s|* %-28.28s\n",name,name);
    }
    private String getReFile (String name){
        return String.format("? %-28.28s|?%-28.28s\n",
                name.substring(0,name.indexOf('/')), name.substring(name.indexOf('/')+1));
    }

    /**Вид представление строки
     * @return
     */
    private String getLine(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<30;i++){
            sb.append('-');
        }
        sb.append('+');
        for (int i =0;i<30;i++){
            sb.append('-');
        }
        sb.append('\n');
        return sb.toString();
    }

    /**Строки справки HELP
     *
     * @return
     */
    private String getHelp(){
        return String.format("\nHELP\n + %s\n- %s\n* %s\n? %s\n",
                StatusFile.NEW,StatusFile.DELETE,StatusFile.RENAMED,StatusFile.UPDATED);
    }


    /**Создает выходной файл с результатом сравнения архивов
     *
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    void createOutputFile() throws FileNotFoundException, UnsupportedEncodingException{

         PrintWriter writer = new PrintWriter(routeDirectory +NAME_OUTPUT_FILE,DEFAULT_ENCODING);
         achiveComparator.compare();
         HashMap<StatusFile,String> map = achiveComparator.getMap();
         String nameOldAchive = achiveComparator.getOldAchive().getName();
         String nameNewAchive = achiveComparator.getNewAchive().getName();
         writer.printf("%-30.30s|%-30.30s\n", nameOldAchive.substring(nameOldAchive.lastIndexOf('/')+1),
                 nameNewAchive.substring(nameNewAchive.lastIndexOf('/')+1));
         writer.printf(this.getLine());
         for (Map.Entry<StatusFile,String> entry : map.entrySet()){
             switch (entry.getKey()){
                 case NEW:
                     writer.print(this.getNewFile(entry.getValue()));
                     break;
                 case UPDATED:
                     writer.print(this.getUpFile(entry.getValue()));
                     break;
                 case DELETE:
                     writer.print(this.getDelFile(entry.getValue()));
                     break;
                 case RENAMED:
                     writer.print(this.getReFile(entry.getValue()));
                     break;
             }
         }
         writer.print(this.getLine());
         writer.print(this.getHelp());
         writer.close();
         System.out.println("Create :" + routeDirectory + NAME_OUTPUT_FILE);
    }
}
