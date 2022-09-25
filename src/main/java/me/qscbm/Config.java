package me.qscbm;

import io.github.mcchampions.DodoOpenJava.Configuration.file.YamlConfiguration;
import io.github.mcchampions.DodoOpenJava.Utils.ConfigUtil;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Config {
    private static JSONObject data = new JSONObject();
    private static final File file1 = new File(ConfigUtil.getJarPath() + "database.json");

    private static final File file2 = new File(ConfigUtil.getJarPath() + "config.yml");
    private static YamlConfiguration yaml;
    public static void init() {
        if (!file1.exists()) {
            try {
                ConfigUtil.copyResourcesToFile("database.json", file1.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        data = new JSONObject(Objects.requireNonNull(ConfigUtil.readFile(new File(ConfigUtil.getJarPath() + "database.json"))));
        if (!file2.exists()) {
            try {
                ConfigUtil.copyResourcesToFile("config.yml", file2.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        yaml = YamlConfiguration.loadConfiguration(file2);
    }

    public static int getMax() {
        return data.getInt("max");
    }

    public static int getSpeed() {
        return yaml.getInt("speed");
    }

    public static double getSpawn() {
        return yaml.getDouble("spawn");
    }

    public static void putMax(int max) {
        data.put("max",max);
        try {
            bufferedWriterMethod(file1,data.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 写入文件
     * @param file 文件
     * @param content  待写入内容
     * @throws IOException 异常时抛出
     */
    public static void bufferedWriterMethod(File file, String content) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(content);
        }
    }
}
