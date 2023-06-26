package fr.loxoz.isaidnosnow;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class ISNWConfig {
    private final transient Path path;
    public boolean enabled = false;

    public ISNWConfig(Path path) {
        this.path = path;
    }

    public Path getPath() { return path; }
    public File getFile() { return path.toFile(); }

    public boolean load() {
        var file = getFile();
        if (!file.exists() || !file.isFile()) return true;
        try {
            var reader = new FileReader(file);
            var props = new Properties();
            props.load(reader);
            String res = props.getProperty("enabled");
            if (res == null) return false;
            enabled = Boolean.parseBoolean(res);
            return true;
        } catch (IOException ignored) {}
        return false;
    }

    public boolean save() {
        try {
            var reader = new FileWriter(getFile());
            var props = new Properties();
            props.setProperty("enabled", String.valueOf(enabled));
            props.store(reader, null);
            return true;
        } catch (IOException ignored) {}
        return false;
    }

    public void saveAsync() {
        new Thread(this::save).start();
    }
}
