package ru.develonica.intership.filemanager.view;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.develonica.intership.filemanager.model.FileNode;
import ru.develonica.intership.filemanager.model.FileSystemElement;
import ru.develonica.intership.filemanager.model.annotation.View;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

/**
 * Класс, реализующий отображение в приложении.
 */
@View
public class FileManagerView {

    /**
     * Константа, которая хранит единицы измерения количества информации.
     */
    private static final String[] FILE_SIZE_UNITS = {"bytes", "Kb", "Mb", "Gb", "Tb", "Pb"};

    /**
     * Разница в типах данных.
     */
    private static final double DIFFERENCE_IN_FILE_SIZE_UNITS = 1024;

    /**
     * Строка для вывода неизмененного размера.
     */
    private static final String NOT_CAST_SIZE_STRING = "%6.0f %-5s";

    /**
     * Строка для вывода измененного размера.
     */
    private static final String CAST_SIZE_STRING = "%6.1f %-5s";

    /**
     * Элемент, из которого состоит разделитель между блоками приложения.
     */
    private static final String SEPARATOR_ELEMENT = "─";

    /**
     * Количество разделителей в строке.
     */
    private static final int SEPARATOR_ELEMENT_COUNT = 108;

    /**
     * Меню приложения.
     */
    private static final String MENU = """
            1. Создать пустой файл
            2. Создать пустую директорию
            3. Удалить файл или директорию
            4. Перейти в директорию
            5. Обновить список файлов
            6. Выход
            Введите цифру, соответствующую вашему запросу:\s""";

    /**
     * Запрос названия файла.
     */
    private static final String INPUT_FILE = "Введите название файла: ";

    /**
     * Запрос названия директории.
     */
    private static final String INPUT_DIRECTORY = "Введите название директории: ";

    /**
     * Запрос названия объекта.
     */
    private static final String INPUT_FILE_OR_DIRECTORY = "Введите полное название файла или директории: ";

    /**
     * Запрос названия директории для перехода в нее.
     */
    private static final String CHANGE_DIRECTORY =
            "Введите название директории (оставьте поле пустым чтобы вернуться на директорию выше): ";

    /**
     * Неверная команда.
     */
    private static final String WRONG_COMMAND = "Неверная команда";

    /**
     * Сообщение об ошибке создания файла.
     */
    private static final String FILE_CREATION_EXCEPTION_MESSAGE = "Ошибка создания файла [%s] по пути %s\n";

    /**
     * Сообщение об ошибке создания директории.
     */
    private static final String DIRECTORY_CREATION_EXCEPTION_MESSAGE = "Ошибка создания директории [%s] по пути %s\n";

    /**
     * Сообщение об ошибке удаления элемента.
     */
    private static final String ELEMENT_DELETION_EXCEPTION_MESSAGE = "Ошибка удаления элемента [%s] по пути %s\n";

    /**
     * Сообщение об ошибке смены директории.
     */
    private static final String DIRECTORY_CHANGING_EXCEPTION_MESSAGE =
            "Ошибка смены директории по пути %s в директорию [%s]\n";

    /**
     * Сообщение об ошибке создания файлового дерева.
     */
    private static final String FILE_TREE_GENERATION_EXCEPTION_MESSAGE =
            "Ошибка генерации файлового дерева по пути %s\n";

    /**
     * Сообщение об ошибке заданного уровня вложенности.
     */
    private static final String NESTING_LEVEL_EXCEPTION_MESSAGE =
            "Ошибка заданного уровня вложения элементов файловой системы, уровень вложения: %d\n";

    /**
     * Разбиение элементов информации файла/директории в строке.
     */
    private static final String ELEMENT_INFORMATION = "%s %-15s %-11s  %-3s  %-6d %-11s\n";

    /**
     * Размер для названия элемента.
     */
    private static final int ELEMENT_NAME_SIZE = 50;

    /**
     * Элемент, отвечающий за начало подстроки.
     */
    private static final int START_SUBSTRING = 0;

    /**
     * Элемент, при делении на который получается половина подстроки.
     */
    private static final int HALF_SUBSTRING = 2;

    /**
     * Файл.
     */
    private static final String FILE = "file %s";

    /**
     * Директория.
     */
    private static final String DIRECTORY = "dir";

    /**
     * Атрибуты.
     */
    private static final String ATTRIBUTES = "%s%s%s";

    /**
     * Файл/директория читается.
     */
    private static final String READABLE = "r";

    /**
     * В файл/директорию можно записать.
     */
    private static final String WRITABLE = "w";

    /**
     * Файл/директорию можно выполнить.
     */
    private static final String EXECUTABLE = "x";

    /**
     * Отсутствие какого-либо атрибута.
     */
    private static final String ABSENCE_ATTRIBUTE = "-";

    /**
     * Начальный уровень вложенности элементов в файловом дереве.
     */
    private static final int START_NESTING_LEVEL = 0;

    /**
     * Конец ветвления.
     */
    private static final String BRANCHING_END = "└─";

    /**
     * Продолжение ветвления.
     */
    private static final String BRANCHING_CONTINUE = "├─";

    /**
     * Ветка дерева.
     */
    private static final String BRANCH = "│ ";

    /**
     * Отсутствие ветки дерева.
     */
    private static final String ABSENCE_BRANCH = "  ";

    /**
     * Сокращение строки.
     */
    private static final String PASS = "...";

    /**
     * Название элемента с регулируемым размером.
     */
    private static final String ELEMENT_NAME_WITH_DEFINABLE_SIZE = "%-Xs";

    /**
     * Элемент для замены на нужный размер.
     */
    private static final String CHANGE_OF_SIZE = "X";

    /**
     * Разделитель названия файла и его расширения.
     */
    private static final String FILE_SEPARATOR = "\\.";

    /**
     * Неопределенный файл.
     */
    private static final String UNDEFINED_FILE = "undefined";

    /**
     * Мапа, в которой находятся ключ - расширение, значение - тип файла.
     */
    private final Map<String, String> typesOfExtensions;

    public FileManagerView(@Qualifier("fileTypeForExtension") Map<String, String> typesOfExtensions) {
        this.typesOfExtensions = typesOfExtensions;
    }

    /**
     * Метод, выводящий все файлы/директории на экран.
     *
     * @param fileSystemElements объекты
     */
    public void showFileTree(FileNode fileSystemElements) {
        showLine();
        System.out.println(fileSystemElements.getNode().getPath());
        for (FileNode child : fileSystemElements.getChildren()) {
            FileSystemElement element = child.getNode();
            if (child == fileSystemElements.getChildren().getLast()) {
                System.out.print(BRANCHING_END);
                showFileSystemElement(element, START_NESTING_LEVEL);
                showFileTree(child, START_NESTING_LEVEL + 1, ABSENCE_BRANCH);
            } else {
                System.out.print(BRANCHING_CONTINUE);
                showFileSystemElement(element, START_NESTING_LEVEL);
                showFileTree(child, START_NESTING_LEVEL + 1, BRANCH);
            }
        }
    }

    /**
     * Метод, выводящий меню приложения на экран.
     */
    public void showMenuMessage() {
        showLine();
        System.out.print(MENU);
    }

    /**
     * Метод, выводящий запрос на ввод названия директории.
     */
    public void showFileNameCreationMessage() {
        System.out.print(INPUT_FILE);
    }

    /**
     * Метод, выводящий запрос на ввод названия директории.
     */
    public void showDirectoryNameCreationMessage() {
        System.out.print(INPUT_DIRECTORY);
    }

    /**
     * Метод, выводящий запрос на ввод название объекта.
     */
    public void showFileOrDirectorySelectionMessage() {
        System.out.print(INPUT_FILE_OR_DIRECTORY);
    }

    /**
     * Метод, выводящий запрос на ввод директории для её смены.
     */
    public void showDirectoryChangeMessage() {
        System.out.print(CHANGE_DIRECTORY);
    }

    /**
     * Метод, выводящий информацию при вводе неверной команды.
     */
    public void showWrongCommandMessage() {
        showLine();
        System.out.println(WRONG_COMMAND);
    }

    /**
     * Метод, выводящий информацию при ошибке создания файла.
     *
     * @param path     путь
     * @param fileName название файла
     */
    public void showCreateFileExceptionMessage(Path path, String fileName) {
        showLine();
        System.out.printf(FILE_CREATION_EXCEPTION_MESSAGE, fileName, path);
    }

    /**
     * Метод, выводящий информацию при ошибке создания директории.
     *
     * @param path          путь
     * @param directoryName название директории
     */
    public void showCreateDirectoryExceptionMessage(Path path, String directoryName) {
        showLine();
        System.out.printf(DIRECTORY_CREATION_EXCEPTION_MESSAGE, directoryName, path);
    }

    /**
     * Метод, выводящий информацию при ошибке удаления объекта.
     *
     * @param path        путь
     * @param elementName название объекта
     */
    public void showDeleteElementExceptionMessage(Path path, String elementName) {
        showLine();
        System.out.printf(ELEMENT_DELETION_EXCEPTION_MESSAGE, elementName, path);
    }

    /**
     * Метод, выводящий информацию при ошибке смены директории.
     *
     * @param path         путь
     * @param newDirectory новая директория
     */
    public void showChangeDirectoryExceptionMessage(Path path, String newDirectory) {
        showLine();
        System.out.printf(DIRECTORY_CHANGING_EXCEPTION_MESSAGE, path, newDirectory);
    }

    /**
     * Метод, выводящий информацию при ошибке генерации файлового дерева.
     *
     * @param path путь
     */
    public void showGenerationFileTreeExceptionMessage(Path path) {
        showLine();
        System.out.printf(FILE_TREE_GENERATION_EXCEPTION_MESSAGE, path);
    }

    /**
     * Метод, выводящий информацию об ошибки заданного уровня вложенности.
     *
     * @param level уровень вложенности
     */
    public void showNestingLevelExceptionMessage(int level) {
        showLine();
        System.out.printf(NESTING_LEVEL_EXCEPTION_MESSAGE, level);
    }

    /**
     * Метод, выводящий на экран линию. Данный метод предназначен только для красивого визуала.
     */
    private void showLine() {
        System.out.println(SEPARATOR_ELEMENT.repeat(SEPARATOR_ELEMENT_COUNT));
    }

    /**
     * Метод, выводящий все файлы/директории на экран.
     *
     * @param fileSystemElements объекты
     * @param nestingLevel       уровень вложенности
     */
    private void showFileTree(FileNode fileSystemElements, int nestingLevel, String prefix) {
        for (FileNode child : fileSystemElements.getChildren()) {
            System.out.print(prefix);
            FileSystemElement element = child.getNode();
            if (child == fileSystemElements.getChildren().getLast()) {
                System.out.print(BRANCHING_END);
                showFileSystemElement(element, nestingLevel);
                showFileTree(child, nestingLevel + 1, prefix + ABSENCE_BRANCH);
            } else {
                System.out.print(BRANCHING_CONTINUE);
                showFileSystemElement(element, nestingLevel);
                showFileTree(child, nestingLevel + 1, prefix + BRANCH);
            }
        }
    }

    /**
     * Метод, предназначенный для демонстрации объекта файловой системы.
     *
     * @param element      объект
     * @param nestingLevel уровень вложенности
     */
    private void showFileSystemElement(FileSystemElement element, int nestingLevel) {
        System.out.printf(ELEMENT_INFORMATION,
                getAbbreviatedName(element.getName(), nestingLevel * ABSENCE_BRANCH.length()),
                element.getType() ? String.format(FILE, defineFileType(element.getName())) : DIRECTORY,
                castSize(element.getSize()),
                String.format(ATTRIBUTES,
                        element.isReadable() ? READABLE : ABSENCE_ATTRIBUTE,
                        element.isWritable() ? WRITABLE : ABSENCE_ATTRIBUTE,
                        element.isExecutable() ? EXECUTABLE : ABSENCE_ATTRIBUTE),
                element.getTotalObjects(),
                castSize(element.getTotalSize()));
    }

    /**
     * Метод, который преобразует название элемента под размер интерфейса.
     *
     * @param fileName название элемента
     * @param indent   уровень вложенности, который показывает отступ
     * @return преобразованное название элемента
     */
    private String getAbbreviatedName(String fileName, int indent) {
        int size = ELEMENT_NAME_SIZE - indent;
        if (fileName.length() > size) {
            return fileName.substring(START_SUBSTRING, size / HALF_SUBSTRING)
                    + PASS
                    + fileName.substring(fileName.length() - size / HALF_SUBSTRING + PASS.length());
        }
        return String.format(ELEMENT_NAME_WITH_DEFINABLE_SIZE.replaceFirst(CHANGE_OF_SIZE, String.valueOf(size)),
                fileName);
    }

    /**
     * Метод, который переводит байтовый размер в максимально возможный.
     *
     * @param initialSize размер в байтах
     * @return результирующий возможный максимальный размер
     */
    private String castSize(long initialSize) {
        double resultSize = initialSize;
        if (resultSize < DIFFERENCE_IN_FILE_SIZE_UNITS) {
            return String.format(NOT_CAST_SIZE_STRING, resultSize, FILE_SIZE_UNITS[0]);
        }
        int unitIndex = 0;
        while (unitIndex < FILE_SIZE_UNITS.length - 1) {
            if (resultSize >= DIFFERENCE_IN_FILE_SIZE_UNITS) {
                resultSize /= DIFFERENCE_IN_FILE_SIZE_UNITS;
                unitIndex++;
                continue;

            }
            break;
        }
        return String.format(CAST_SIZE_STRING, resultSize, FILE_SIZE_UNITS[unitIndex]);
    }

    /**
     * Метод, который по названию файла и исходя из его расширения определяет его тип.
     *
     * @param name название файла
     * @return тип файла
     */
    private String defineFileType(String name) {
        String fileExtension = Arrays.asList(name.split(FILE_SEPARATOR)).getLast().toLowerCase();
        return typesOfExtensions.getOrDefault(fileExtension, UNDEFINED_FILE);
    }
}
