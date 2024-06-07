package ru.develonica.intership.filemanager.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

/**
 * Класс, в котором прописана конфигурация приложения, прописаны бины.
 */
@Configuration
public class SpringConfiguration {

    /**
     * Метод, реализующий бин сканера.
     *
     * @return объект класса {@code Scanner}
     */
    @Bean
    public Scanner getScanner() {
        return new Scanner(System.in);
    }

    /**
     * Метод, который реализует бин наличия веток файлового дерева, по заданному уровню вложенности.
     *
     * @param level уровень вложенности файловых объектов в приложении
     * @return массив, с количеством веток + 1, или в случае неверного уровня вложенности количество веток - 0
     */
    @Bean
    public boolean[] getBranches(@Value("${root.level}") int level) {
        if (level <= 0) {
            return new boolean[0];
        }
        return new boolean[level + 1];
    }
}
