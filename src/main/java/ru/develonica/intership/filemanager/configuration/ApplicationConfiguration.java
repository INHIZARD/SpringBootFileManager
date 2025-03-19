package ru.develonica.intership.filemanager.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.develonica.intership.filemanager.controller.FileManagerController;
import ru.develonica.intership.filemanager.model.FileNode;
import ru.develonica.intership.filemanager.model.FileSystemElement;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Класс, в котором прописана конфигурация приложения, прописаны бины.
 */
@Configuration
@ConfigurationProperties("application")
public class ApplicationConfiguration {
    /**
     * Мапа, в которой хранятся типы файлов и их соответствующие расширения.
     */
    private Map<String, List<String>> fileType;

    private int level;

    /**
     * Бин, реализующий запуск приложения.
     *
     * @param context контекст приложения
     * @return параметры запуска приложения
     */
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context) {
        return args -> context.getBean(FileManagerController.class).startApp(level);
    }

    /**
     * Метод, реализующий бин сканера.
     *
     * @return объект класса {@code Scanner}
     */
    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    /**
     * Бин, реализующий стартовое файловое дерево.
     *
     * @param startPath путь для инициализации дерева.
     * @return файловое дерево.
     */
    @Bean
    public FileNode startFileNode(@Value("${application.directory}") Path startPath) {
        return new FileNode(new FileSystemElement(startPath));
    }

    /**
     * Бин, реализующий мапу с ключом расширением файла и значением - типом файла.
     *
     * @return мапа расширение - тип файла
     */
    @Bean("fileTypeForExtension")
    public Map<String, String> fileTypeMap() {
        Map<String, String> fileTypeForExtension = new HashMap<>();
        for (String type : fileType.keySet()) {
            fileType.get(type).forEach(extension -> fileTypeForExtension.put(extension, type));
        }
        return fileTypeForExtension;
    }

    public void setFileType(Map<String, List<String>> fileType) {
        this.fileType = fileType;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
