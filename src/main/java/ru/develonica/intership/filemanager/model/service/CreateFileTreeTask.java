package ru.develonica.intership.filemanager.model.service;

import ru.develonica.intership.filemanager.model.DirectoryAttributes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Класс, наследующий {@code RecursiveAction}, для решения многопоточной задачи создания файлового дерева.
 */
public class CreateFileTreeTask extends RecursiveTask<DirectoryAttributes> {
    private final File directoryToScan;

    public CreateFileTreeTask(File directoryToScan) {
        this.directoryToScan = directoryToScan;
    }

    @Override
    protected DirectoryAttributes compute() {
        File[] elementArray = directoryToScan.listFiles();
        DirectoryAttributes resultAttributes = new DirectoryAttributes(elementArray.length, 0);
        List<CreateFileTreeTask> directoryTasks = new ArrayList<>();
        for (File element : elementArray) {
            if (element.isFile()) {
                resultAttributes.increaseTotalSize(element.length());
            } else if (element.isDirectory() && element.listFiles() != null) {
                directoryTasks.add(new CreateFileTreeTask(element));
            }
        }
        invokeAll(directoryTasks);
        for (CreateFileTreeTask task : directoryTasks) {
            resultAttributes.addAttributes(task.join());
        }
        return resultAttributes;
    }
}
