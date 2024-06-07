package ru.develonica.intership.filemanager.model;

/**
 * Класс, отвечающий за вычисляемые атрибуты директории.
 */
public class DirectoryAttributes {

    /**
     * Суммарное количество объектов в директории.
     */
    private int totalObjects;

    /**
     * Суммарный размер объектов в директории.
     */
    private long totalSize;

    public DirectoryAttributes(int totalObjects, long totalSize) {
        this.totalObjects = totalObjects;
        this.totalSize = totalSize;
    }

    public int getTotalObjects() {
        return totalObjects;
    }

    public void setTotalObjects(int totalObjects) {
        this.totalObjects = totalObjects;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    /**
     * Метод, позволяющий увеличить суммарный размер файлов в директории.
     *
     * @param amount сумма, на которую увеличится общий размер
     */
    public void increaseTotalSize(long amount) {
        this.totalSize += amount;
    }

    /**
     * Метод, позволяющий увеличить все атрибуты директории на аттрибуты другой, например вложенной директории.
     *
     * @param attributes аттрибуты другой директории.
     */
    public void addAttributes(DirectoryAttributes attributes) {
        this.totalObjects += attributes.totalObjects;
        this.totalSize += attributes.totalSize;
    }
}
