package ru.develonica.intership.filemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.develonica.intership.filemanager.model.annotation.Controller;
import ru.develonica.intership.filemanager.model.exception.*;
import ru.develonica.intership.filemanager.model.service.FileManagerService;
import ru.develonica.intership.filemanager.view.FileManagerView;

import java.util.Scanner;

/**
 * Класс контроллер, предназначенный для связи между представлением и моделью приложения.
 * Также используется класс {@code FilesService} для работы с моделью.
 *
 * @see ru.develonica.intership.filemanager.model
 * @see ru.develonica.intership.filemanager.view
 * @see ru.develonica.intership.filemanager.model.service.FileManagerService
 */
@Controller
public class FileManagerController {

    /**
     * Ввод соответствующего значения вызывает создание файла.
     */
    private static final String FILE_CREATION = "1";

    /**
     * Ввод соответствующего значения вызывает создание директории.
     */
    private static final String DIRECTORY_CREATION = "2";

    /**
     * Ввод соответствующего значения вызывает удаление элемента.
     */
    private static final String ELEMENT_DELETE = "3";

    /**
     * Ввод соответствующего значения вызывает смену директории.
     */
    private static final String CHANGE_DIRECTORY = "4";

    /**
     * Ввод соответствующего значения вызывает обновление содержимого директории.
     */
    private static final String UPDATE = "5";

    /**
     * Ввод соответствующего значения вызывает выход из приложения.
     */
    private static final String EXIT = "6";

    /**
     * Класс, реализующий отображение информации в приложении.
     *
     * @see FileManagerView
     */
    private final FileManagerView fileManagerView;

    /**
     * Класс сканера, предназначенный для ввода.
     */
    private final Scanner scanner;

    /**
     * Класс сервиса для работы с моделью.
     *
     * @see FileManagerService
     */
    private final FileManagerService fileManagerService;

    /**
     * В конструкторе проходит стартовая инициализация файлового дерева.
     *
     * @param fileManagerService путь в стартовую директорию
     */
    @Autowired
    public FileManagerController(FileManagerService fileManagerService, Scanner scanner,
                                 FileManagerView fileManagerView) {
        this.fileManagerService = fileManagerService;
        this.scanner = scanner;
        this.fileManagerView = fileManagerView;
    }

    /**
     * Метод, отвечающий за инициализацию приложения.
     * Здесь определяются эндпоинты приложения.
     * <p>
     * Пункты меню:
     * <br>1 — создать файл
     * <br>2 — создать директорию
     * <br>3 — удалить файл или директорию
     * <br>4 — переместиться в другую директорию
     * <br>5 — обновить список файлов
     * <br>6 — выход
     * </p>
     */
    public void startApp() {
        String command;
        boolean runningApp = true;
        try {
            fileManagerService.initializeFileStructure();
            fileManagerView.showFileTree(fileManagerService.getFileTree());
        } catch (CreateFileTreeException ex) {
            fileManagerView.showGenerationFileTreeExceptionMessage(ex.getPath());
            runningApp = false;
        } catch (NestingLevelException ex) {
            fileManagerView.showNestingLevelExceptionMessage(ex.getNestingLevel());
            runningApp = false;
        }
        while (runningApp) {
            fileManagerView.showMenuMessage();
            command = scanner.nextLine();
            switch (command) {
                case FILE_CREATION: {
                    createFile();
                    break;
                }
                case DIRECTORY_CREATION: {
                    createDirectory();
                    break;
                }
                case ELEMENT_DELETE: {
                    deleteFileOrDirectory();
                    break;
                }
                case CHANGE_DIRECTORY: {
                    changeDirectory();
                    break;
                }
                case UPDATE: {
                    fileManagerView.showFileTree(fileManagerService.getFileTree());
                    break;
                }
                case EXIT: {
                    runningApp = false;
                    break;
                }
                default: {
                    fileManagerView.showWrongCommandMessage();
                    break;
                }
            }
        }
        scanner.close();
    }

    /**
     * Метод, реализующий создание файла.
     */
    private void createFile() {
        fileManagerView.showFileNameCreationMessage();
        String fileName = scanner.nextLine();
        try {
            fileManagerService.createFile(fileName);
        } catch (CreateFileException ex) {
            fileManagerView.showCreateFileExceptionMessage(ex.getPath(), ex.getFileName());
        }
    }

    /**
     * Метод, реализующий создание директории.
     */
    private void createDirectory() {
        fileManagerView.showDirectoryNameCreationMessage();
        String directoryName = scanner.nextLine();
        try {
            fileManagerService.createDirectory(directoryName);
        } catch (CreateDirectoryException ex) {
            fileManagerView.showCreateDirectoryExceptionMessage(ex.getPath(), ex.getDirectoryName());
        }
    }

    /**
     * Метод, реализующий удаление элемента.
     */
    private void deleteFileOrDirectory() {
        fileManagerView.showFileOrDirectorySelectionMessage();
        String name = scanner.nextLine();
        try {
            fileManagerService.delete(name);
        } catch (DeleteElementException ex) {
            fileManagerView.showDeleteElementExceptionMessage(ex.getPath(), ex.getName());
        }
    }

    /**
     * Метод, реализующий переход в директорию.
     */
    private void changeDirectory() {
        fileManagerView.showDirectoryChangeMessage();
        String directoryName = scanner.nextLine();
        try {
            if (directoryName.isEmpty()) {
                fileManagerService.navigateToParentDirectory();
            } else {
                fileManagerService.navigateToChildDirectory(directoryName);
            }
            fileManagerView.showFileTree(fileManagerService.getFileTree());
        } catch (ChangeDirectoryException ex) {
            fileManagerView.showChangeDirectoryExceptionMessage(ex.getPath(), ex.getDirectoryName());
        }
    }
}
