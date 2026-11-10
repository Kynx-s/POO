package pe.edu.utp.adaptive.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import pe.edu.utp.adaptive.api.Strategy;

public class PluginRegistry {

    private final Properties catalog = new Properties();

    public PluginRegistry() {
        try (InputStream in = PluginRegistry.class.getResourceAsStream("/plugins.properties")) {
            if (in == null) {
                throw new PluginException("No se encontró plugins.properties en el classpath");
            }
            catalog.load(in);
        } catch (IOException e) {
            throw new PluginException("No se pudo leer plugins.properties", e);
        }
    }

    public boolean contains(String id) {
        return catalog.containsKey(id);
    }

    @SuppressWarnings("unchecked")
    public Strategy<String, String> create(String id) {
        String className = catalog.getProperty(id);
        if (className == null) {
            throw new PluginException("Plugin no registrado: " + id);
        }
        try {
            Class<?> type = Class.forName(className);
            if (!Strategy.class.isAssignableFrom(type)) {
                throw new PluginException(className + " no implementa Strategy");
            }
            return (Strategy<String, String>) type.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new PluginException("No se pudo crear el plugin " + id + " (" + className + ")", e);
        }
    }
}