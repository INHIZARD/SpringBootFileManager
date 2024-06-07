package ru.develonica.intership.filemanager.model.exception;

import java.nio.file.Path;

/**
 * Исключение, отвечающее за ошибку удаления элемента.
 */
public class DeleteElementException extends Exception {
    private static final String message = "Ошибка удаления элемента";
    private final Path path;
    private final String name;

    public DeleteElementException(Path path, String name) {
        super(message);
        this.path = path;
        this.name = name;
    }

    public Path getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
