package pl.compprog.logs;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class FileAndConsoleLoggerFactory {

    private static boolean loggerConfigured = false;

    public static Logger getConfiguredLogger(String name) {
        if (!loggerConfigured) {
            InputStream stream = FileAndConsoleLoggerFactory.class.
                    getResourceAsStream("/pl/compprog/logging.properties");
            try {
                LogManager.getLogManager().readConfiguration(stream);
                loggerConfigured = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Logger.getLogger(name);
    }
}
