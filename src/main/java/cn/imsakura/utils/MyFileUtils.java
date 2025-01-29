package cn.imsakura.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyFileUtils {
    public static String readFromFile(String fileName) {
        try {
            return Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void writeToFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {  // true 表示追加写入
            writer.write(content);
            writer.newLine();  // 换行
            //System.out.println("File written successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
