package ru.develonica.intership.filemanager.model;

import org.springframework.beans.factory.annotation.Autowired;
import ru.develonica.intership.filemanager.model.annotation.Model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, реализующий узел в файловом дереве.
 */
@Model
public class FileNode {

    /**
     * Объект, на котором построен узел.
     */
    private FileSystemElement node;

    /**
     * Дочерние объекты.
     */
    private List<FileNode> children;

    @Autowired
    public FileNode(FileSystemElement node, List<FileNode> children) {
        this.node = node;
        this.children = children;
    }

    public FileNode(FileSystemElement node) {
        this.node = node;
        this.children = new ArrayList<>();
    }

    public FileSystemElement getNode() {
        return node;
    }

    public void setNode(FileSystemElement node) {
        this.node = node;
    }

    public List<FileNode> getChildren() {
        return children;
    }

    public void setChildren(List<FileNode> children) {
        this.children = children;
    }

    public void addChild(FileNode child) {
        children.add(child);
    }

    /**
     * Метод, который перестраивает узел под новый первоначальный путь.
     *
     * @param newPath новый путь
     */
    public void buildNodeOnNewPath(Path newPath) {
        this.node = new FileSystemElement(newPath);
        this.children = new ArrayList<>();
    }
}
