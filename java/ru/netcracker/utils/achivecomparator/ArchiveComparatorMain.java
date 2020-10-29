package ru.netcracker.utils.achivecomparator;
import java.io.IOException;
public class ArchiveComparatorMain {
    public static void main(String[] args) throws IOException {
        try {

            Print printer = new Print(args[0], args[1]);
            printer.createOutputFile();
        } catch (ArrayIndexOutOfBoundsException e) {
            DialogWindow dialogWindow = new DialogWindow();
            Print printer = new Print(
                    dialogWindow.getRouteOneFile(),
                    dialogWindow.getRouteTwoFile(),
                    dialogWindow.getDirectoryPath()
            );
            printer.createOutputFile();
        }
    }
}
