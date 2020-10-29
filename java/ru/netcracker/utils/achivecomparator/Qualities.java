package ru.netcracker.utils.achivecomparator;

/**хранит перечисление статуса файлов из архива
 *
 */
public class Qualities {
    public enum StatusFile{
        DELETE,
        NEW,
        DEFAULT,
        UPDATED,
        RENAMED
    }
}
