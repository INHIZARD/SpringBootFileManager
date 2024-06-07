package ru.develonica.intership.filemanager.model.exception;

/**
 * Исключение, отвечающее за ошибку неверно заданного уровня вложенности.
 */
public class NestingLevelException extends Exception {
    private static final String message = "Неверно заданный уровень вложения";
    private final int nestingLevel;

    public NestingLevelException(int nestingLevel) {
        super(message);
        this.nestingLevel = nestingLevel;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }
}
