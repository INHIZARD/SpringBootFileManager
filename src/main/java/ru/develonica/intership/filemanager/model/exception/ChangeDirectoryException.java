package ru.develonica.intership.filemanager.model.exception;

import java.nio.file.Path;

/**
 * Исключение, отвечающее за ошибку смены директории.
 */
public class ChangeDirectoryException extends Exception {
    private static final String message = "Ошибка смены директории";
    private static final String parentDirectory = "↑";
    private final Path path;
    private final String directoryName;

    public ChangeDirectoryException(Path path) {
        super(message);
        this.path = path;
        this.directoryName = parentDirectory;
    }

    public ChangeDirectoryException(Path path, String directoryName) {
        super(message);
        this.path = path;
        this.directoryName = directoryName;
    }

    public Path getPath() {
        return path;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}
