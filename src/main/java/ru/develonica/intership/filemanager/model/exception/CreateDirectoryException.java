package ru.develonica.intership.filemanager.model.exception;

import java.nio.file.Path;

/**
 * Исключение, отвечающее за ошибку создания директории.
 */
public class CreateDirectoryException extends Exception {
    private static final String message = "Ошибка создания директории";
    private final Path path;
    private final String directoryName;

    public CreateDirectoryException(Path path, String directoryName) {
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
