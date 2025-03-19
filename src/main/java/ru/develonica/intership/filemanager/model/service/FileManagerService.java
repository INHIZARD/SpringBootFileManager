package ru.develonica.intership.filemanager.model.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import ru.develonica.intership.filemanager.model.DirectoryAttributes;
import ru.develonica.intership.filemanager.model.FileNode;
import ru.develonica.intership.filemanager.model.FileSystemElement;
import ru.develonica.intership.filemanager.model.annotation.Service;
import ru.develonica.intership.filemanager.model.exception.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Класс сервис, предназначенный для работы с моделью.
 */
@Service
public class FileManagerService {

    /**
     * Логгер.
     */
    private static final Logger LOG = LoggerFactory.getLogger(FileManagerService.class);

    /**
     * Файловое дерево.
     */
    private final FileNode fileTree;

    /**
     * Уровень сканирования дерева файлов.
     */
    private final int scanLevel;

    public FileManagerService(FileNode fileTree, @Value("${application.level}") int scanLevel) {
        this.fileTree = fileTree;
        this.scanLevel = scanLevel;
    }

    /**
     * Метод, предназначенный для инициализации файлов дерева.
     *
     * @throws CreateFileTreeException ошибка создания файлового дерева
     */
    public void initializeFileStructure() throws CreateFileTreeException {
        if (Files.isRegularFile(fileTree.getNode().getPath())) {
            LOG.error("Не получилось проинициализировать файловое дерево, + " +
                            "так как он ведет к файлу, а не к директории: {}",
                    fileTree.getNode().getPath());
            throw new CreateFileTreeException(fileTree.getNode().getPath());
        }
        if (!Files.exists(fileTree.getNode().getPath())) {
            LOG.error("Не получилось проинициализировать файловое дерево, так как пути не существует: {}",
                    fileTree.getNode().getPath());
            throw new CreateFileTreeException(fileTree.getNode().getPath());
        }
        LOG.debug("Файловое дерево успешно сканируется на пути: {}", fileTree.getNode().getPath());
        scanRootDirectory(fileTree, scanLevel);
        LOG.debug("Инициализация дерева по пути [{}] завершена", fileTree.getNode().getPath());
    }

    /**
     * Метод, который возвращает дерево файлов с действующем корнем.
     *
     * @return список файлов в директории
     */
    public FileNode getFileTree() {
        return fileTree;
    }

    /**
     * Метод, создающий файл.
     *
     * @param fileName название нового файла
     * @throws CreateFileException ошибка создания файла
     */
    public void createFile(String fileName) throws CreateFileException {
        try {
            Path newPath = Path.of(String.valueOf(fileTree.getNode().getPath()), fileName);
            Files.createFile(newPath);
            fileTree.addChild(new FileNode(new FileSystemElement(newPath, fileName, true)));
            LOG.debug("Файл [{}] успешно создан в директории: {}", fileName, fileTree.getNode().getPath());
        } catch (IOException e) {
            LOG.error("Файл [{}] не может быть создан, так как такой уже существует на пути: {}",
                    fileName, fileTree.getNode().getPath());
            throw new CreateFileException(fileTree.getNode().getPath(), fileName);
        } catch (InvalidPathException e) {
            LOG.error("Файл [{}] не может быть создан, так как его имя некорректно", fileName);
            throw new CreateFileException(fileTree.getNode().getPath(), fileName);
        }
    }

    /**
     * Метод, создающий директорию.
     *
     * @param directoryName имя директории
     * @throws CreateDirectoryException ошибка создания директории
     */
    public void createDirectory(String directoryName) throws CreateDirectoryException {
        try {
            Path newPath = Path.of(String.valueOf(fileTree.getNode().getPath()), directoryName);
            Files.createDirectory(newPath);
            fileTree.addChild(new FileNode(new FileSystemElement(newPath, directoryName, false)));
            LOG.debug("Директория [{}] успешно создана по пути: {}", directoryName, fileTree.getNode().getPath());
        } catch (IOException e) {
            LOG.error("Директория [{}] не может быть создана, так как такая уже существует на пути: {}",
                    directoryName, fileTree.getNode().getPath());
            throw new CreateDirectoryException(fileTree.getNode().getPath(), directoryName);
        } catch (InvalidPathException e) {
            LOG.error("Директория [{}] не может быть создана, так как ее имя некорректно", directoryName);
            throw new CreateDirectoryException(fileTree.getNode().getPath(), directoryName);
        }
    }

    /**
     * Метод, предназначенный для удаления объекта по его названию.
     *
     * @param name название объекта
     * @throws DeleteElementException ошибка удаления элемента
     */
    public void delete(String name) throws DeleteElementException {
        try {
            Path newPath = Path.of(String.valueOf(fileTree.getNode().getPath()), name);
            Files.delete(newPath);
            fileTree.getChildren().removeIf(element -> Objects.equals(element.getNode().getPath(), newPath));
            LOG.debug("Элемент [{}] успешно удален по пути: {}", name, fileTree.getNode().getPath());
        } catch (IOException e) {
            LOG.error("Элемент [{}] не может быть удален, так как его не существует: {}",
                    name, fileTree.getNode().getPath());
            throw new DeleteElementException(fileTree.getNode().getPath(), name);
        } catch (InvalidPathException e) {
            LOG.error("Элемент [{}] не может быть удален, так как его имя некорректно", name);
            throw new DeleteElementException(fileTree.getNode().getPath(), name);
        }
    }

    /**
     * Метод, предназначенный для перехода вверх по дереву.
     *
     * @throws ChangeDirectoryException ошибка смены директории
     */
    public void navigateToParentDirectory() throws ChangeDirectoryException {
        Path newPath = fileTree.getNode().getPath().getParent();
        if (newPath == null) {
            LOG.error("Смена директории выше не выполнена, так как настоящая директория находится в корне: {}",
                    fileTree.getNode().getPath());
            throw new ChangeDirectoryException(fileTree.getNode().getPath());
        }
        rebuildFileStructure(newPath);
    }

    /**
     * Метод, предназначенный для перехода вниз по дереву.
     *
     * @param directory целевая директория для перехода
     * @throws ChangeDirectoryException ошибка смены директории
     */
    public void navigateToChildDirectory(String directory) throws ChangeDirectoryException {
        Path newPath;
        try {
            newPath = Path.of(String.valueOf(fileTree.getNode().getPath()), directory);
        } catch (InvalidPathException e) {
            LOG.error("В директорию [{}] нельзя переместиться, так как ее имя некорректно", directory);
            throw new ChangeDirectoryException(fileTree.getNode().getPath(), directory);
        }
        if (!Files.isDirectory(newPath)) {
            LOG.error("Смена директории ниже не выполнена, так как директории [{}] не существует на пути: {}",
                    directory, fileTree.getNode().getPath());
            throw new ChangeDirectoryException(fileTree.getNode().getPath(), directory);
        }
        rebuildFileStructure(newPath);
    }

    /**
     * Метод, предназначенный для сканирования файлового дерева по новому пути.
     */
    private void rebuildFileStructure(Path path) {
        LOG.debug("Смена на новый путь [{}] прошла успешно, начало нового сканирования", path);
        fileTree.buildNodeOnNewPath(path);
        scanRootDirectory(fileTree, scanLevel);
        LOG.debug("Перестройка дерева по пути [{}] завершена", path);
    }

    /**
     * Метод, который сканирует установленную корневую директорию в {@code fileTree}.
     */
    private void scanRootDirectory(FileNode node, int level) {
        File elementByNode = new File(node.getNode().getPath().toUri());
        File[] elementArray = elementByNode.listFiles();
        for (File element : elementArray) {
            FileSystemElement newFileSystemElement = new FileSystemElement(
                    element.toPath(),
                    element.getName(),
                    Files.isReadable(element.toPath()),
                    Files.isWritable(element.toPath()),
                    Files.isExecutable(element.toPath()));
            node.addChild(new FileNode(newFileSystemElement));
            if (element.isFile()) {
                newFileSystemElement.setSize(element.length());
            } else if (element.isDirectory()) {
                newFileSystemElement.setType(false);
                if (newFileSystemElement.isReadable()) {

                    /*
                     Завершаем рекурсивный обход и анализируем дерево многопоточным методом, если уровень равен 1,
                     иначе продолжаем рекурсивный обход и добавляем атрибуты директории, по его окончании.
                     */
                    if (level == 1) {
                        CreateFileTreeTask createFileTreeTask = new CreateFileTreeTask(element);
                        DirectoryAttributes resultAttributes = createFileTreeTask.compute();
                        newFileSystemElement.setDirectoryAttributes(resultAttributes);
                    } else {
                        scanRootDirectory(node.getChildren().getLast(), level - 1);
                        for (FileNode subNode : node.getChildren().getLast().getChildren()) {
                            newFileSystemElement.setTotalObjects(newFileSystemElement.getTotalObjects()
                                    + subNode.getNode().getTotalObjects() + 1);
                            newFileSystemElement.setTotalSize(newFileSystemElement.getTotalSize()
                                    + subNode.getNode().getTotalSize()
                                    + subNode.getNode().getSize());
                        }
                    }
                }
            }
        }
    }
}
