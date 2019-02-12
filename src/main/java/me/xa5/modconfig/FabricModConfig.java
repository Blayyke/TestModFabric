package me.xa5.modconfig;

import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class FabricModConfig {
    private CommentedConfigurationNode config;
    private Logger logger = LogManager.getFormatterLogger(getClass().getSimpleName());
    private File configFolder = new File("config");
    private HoconConfigurationLoader loader;

    public void loadConfig(File configFile, Runnable onLoad) throws IOException {
        configFolder.mkdirs();
        if (configFile.createNewFile()) {
            getLogger().info("Created default config file " + configFile.getName());
        }

        HoconConfigurationLoader.Builder builder = HoconConfigurationLoader.builder();
        this.loader = builder.setFile(configFile).build();

        this.config = loader.load(ConfigurationOptions.defaults().setShouldCopyDefaults(true));
        onLoad.run();
        saveConfig();
    }

    private Logger getLogger() {
        return logger;
    }

    public CommentedConfigurationNode getNode(Object... node) {
        return config.getNode(node);
    }

    public File getConfigFolder() {
        return configFolder;
    }

    public void saveConfig() {
        try {
            this.loader.save(config);
        } catch (IOException e) {
            getLogger().warn("Failed to save config!", e);
        }
    }
}