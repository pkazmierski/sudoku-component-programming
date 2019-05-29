package pl.compprog.logs;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class FileAndConsoleLogger extends Logger {


    private static boolean loggerConfigured = false;

    private FileAndConsoleLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }


    public static Logger getFileAndConsoleLogger(String name) {
        if (!loggerConfigured) {
            InputStream stream = FileAndConsoleLogger.class.
                    getResourceAsStream("/pl/compprog/logging.properties");
            try {
                LogManager.getLogManager().readConfiguration(stream);
                loggerConfigured = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return getLogger(name);
    }
}
