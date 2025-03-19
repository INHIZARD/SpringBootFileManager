package ru.develonica.intership.filemanager.model;

import java.nio.file.Path;

/**
 * Класс, реализующий объект файловой системы.
 */
public class FileSystemElement {

    /**
     * Путь до объекта в системе.
     */
    private Path path;

    /**
     * Название файла/директории.
     */
    private String name;

    /**
     * Тип объекта: файл или директория.
     */
    private boolean type;

    /**
     * Размер файла. Для директории — 0.
     */
    private long size;

    /**
     * Объект можно/нельзя прочитать.
     */
    private boolean isReadable;

    /**
     * В объект можно/нельзя записать.
     */
    private boolean isWritable;

    /**
     * Объект можно/нельзя выполнить.
     */
    private boolean isExecutable;

    /**
     * Объект, который содержит в себе атрибуты директории.
     * Если элемент системы не является директорией, то атрибуты будут иметь дефолтные значения.
     */
    private final DirectoryAttributes directoryAttributes;

    /**
     * Конструктор для директории, с которой инициализируется приложение.
     *
     * @param path путь
     */
    public FileSystemElement (Path path) {
        this(path, path.toString(), false);
    }
    
    /**
     * Конструктор для дефолтных элементов.
     *
     * @param path путь
     * @param name имя
     * @param type тип
     */
    public FileSystemElement(Path path, String name, boolean type) {
        this(path, name, type, 0, true, true, true, 0, 0);
    }

    /**
     * Конструктор для инициализации еще неизвестного элемента
     * (по дефолту элемент инициализируется как файл со всеми размерами равными 0).
     *
     * @param path         путь
     * @param name         название
     * @param isReadable   файл можно прочитать
     * @param isWritable   в файл можно записать
     * @param isExecutable файл можно выполнить
     */
    public FileSystemElement(Path path, String name, boolean isReadable, boolean isWritable, boolean isExecutable) {
        this(path, name, true, 0, isReadable, isWritable, isExecutable, 0, 0);
    }

    /**
     * @param path         путь
     * @param name         название
     * @param type         тип
     * @param size         размер
     * @param isReadable   файл можно прочитать
     * @param isWritable   в файл можно записать
     * @param isExecutable файл можно выполнить
     * @param totalObjects количество объектов
     * @param totalSize    суммарный размер
     */
    public FileSystemElement(Path path, String name, boolean type, long size,
                             boolean isReadable, boolean isWritable, boolean isExecutable,
                             int totalObjects, long totalSize) {
        this.path = path;
        this.name = name;
        this.type = type;
        this.size = size;
        this.isReadable = isReadable;
        this.isWritable = isWritable;
        this.isExecutable = isExecutable;
        this.directoryAttributes = new DirectoryAttributes(totalObjects, totalSize);
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isReadable() {
        return isReadable;
    }

    public void setReadable(boolean readable) {
        isReadable = readable;
    }

    public boolean isWritable() {
        return isWritable;
    }

    public void setWritable(boolean writable) {
        isWritable = writable;
    }

    public boolean isExecutable() {
        return isExecutable;
    }

    public void setExecutable(boolean executable) {
        isExecutable = executable;
    }

    public int getTotalObjects() {
        return directoryAttributes.getTotalObjects();
    }

    public void setTotalObjects(int totalObjects) {
        this.directoryAttributes.setTotalObjects(totalObjects);
    }

    public long getTotalSize() {
        return directoryAttributes.getTotalSize();
    }

    public void setTotalSize(long totalSize) {
        this.directoryAttributes.setTotalSize(totalSize);
    }

    public void setDirectoryAttributes(DirectoryAttributes directoryAttributes) {
        this.directoryAttributes.setTotalObjects(directoryAttributes.getTotalObjects());
        this.directoryAttributes.setTotalSize(directoryAttributes.getTotalSize());
    }
}
