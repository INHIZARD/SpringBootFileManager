package ru.develonica.intership.filemanager.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Перечисление, отвечающее за расширения файлов.
 * Тип файла будет определяться через его расширение.
 * Расширения файлов описываются в сигнатуре через пробел.
 */
public enum FileType {
    PICTURE("bmp png jpeg jpg"),
    TEXT("txt"),
    DOCUMENT("rtf doc docx pdf"),
    VIDEO("avi mpg mov mp4"),
    MARKUP("xml html"),
    SYSTEM("sys"),
    SQL("sql"),
    UNDEFINED("");

    /**
     * Расширение.
     */
    private final String extension;

    /**
     * Мапа с расширениями, где ключ является расширением, а значение объектом класса {@code Extension}.
     */
    private static final Map<String, FileType> TYPES_OF_EXTENSIONS;

    static {
        TYPES_OF_EXTENSIONS = new HashMap<>();
        for (FileType fileType : FileType.values()) {
            for (String extensionName : fileType.extension.split(" ")) {
                TYPES_OF_EXTENSIONS.put(extensionName, fileType);
            }
        }
    }

    FileType(String extension) {
        this.extension = extension;
    }

    /**
     * Статистический метод, который по названию файла возвращает его тип.
     *
     * @param name название файла
     * @return расширение файла
     */
    public static FileType defineFileType(String name) {
        String fileExtension = Arrays.asList(name.split("\\.")).getLast().toLowerCase();
        return TYPES_OF_EXTENSIONS.getOrDefault(fileExtension, UNDEFINED);
    }
}
