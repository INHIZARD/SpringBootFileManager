package ru.develonica.intership.filemanager.model.exception;

import java.nio.file.Path;

/**
 * Исключение, отвечающее за ошибку создания файла.
 */
public class CreateFileException extends Exception {
    private static final String message = "Ошибка создания файла";
    private final Path path;
    private final String fileName;

    public CreateFileException(Path path, String fileName) {
        super(message);
        this.path = path;
        this.fileName = fileName;
    }

    public Path getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }
}
