package net.miarma.api;

import net.miarma.api.backlib.ConfigManager;
import net.miarma.api.backlib.Constants;
import net.miarma.api.backlib.security.SecretManager;
import net.miarma.api.backlib.vertx.VertxJacksonConfig;
import net.miarma.api.backlib.util.MessageUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import io.vertx.core.Launcher;

/**
 * Punto de entrada para inicializar la aplicación.
 * Se encarga de:
 * - Crear directorios base si no existen
 * - Copiar el fichero default.properties
 * - Inicializar ConfigManager y secretos
 * - Configurar Jackson para Vert.x
 * - Desplegar el Verticle Master
 */
public class AppInitializer {

    public static void main(String[] args) {
        AppInitializer initializer = new AppInitializer();
        initializer.init();
        initializer.deployMaster();
        Constants.LOGGER.info("✅ App initialized successfully!");
    }

    private final ConfigManager configManager;

    public AppInitializer() {
        this.configManager = ConfigManager.getInstance();
    }

    public void init() {
        initializeDirectories();
        copyDefaultConfig();
        configManager.loadConfig();
        SecretManager.getOrCreateSecret();
        VertxJacksonConfig.configure();
    }

    private void initializeDirectories() {
        File baseDir = new File(configManager.getBaseDir());
        if (!baseDir.exists() && baseDir.mkdirs()) {
            Constants.LOGGER.info("Created base directory: " + baseDir.getAbsolutePath());
        }
    }

    private void copyDefaultConfig() {
        File configFile = configManager.getConfigFile();
        if (!configFile.exists()) {
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("default.properties")) {
                if (in != null) {
                    Files.copy(in, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Constants.LOGGER.info("Copied default.properties to: " + configFile.getAbsolutePath());
                } else {
                    Constants.LOGGER.error(MessageUtil.notFound("Default config", "resources"));
                }
            } catch (IOException e) {
                Constants.LOGGER.error(MessageUtil.failedTo("copy", "default config", e));
            }
        }
    }
    
    private void deployMaster() {
    	Launcher.executeCommand("run", MasterVerticle.class.getName());
    }
}
