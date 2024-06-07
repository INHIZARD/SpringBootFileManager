package ru.develonica.intership.filemanager.model.exception;

import java.nio.file.Path;

/**
 * Исключение, отвечающее за ошибку создания дерева файловой системы.
 */
public class CreateFileTreeException extends Exception {
    private static final String message = "Ошибка создания дерева файлов";
    private final Path path;

    public CreateFileTreeException(Path path) {
        super(message);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
