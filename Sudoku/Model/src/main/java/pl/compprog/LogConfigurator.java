package pl.compprog;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class LogConfigurator {
    static {
        InputStream stream = LogConfigurator.class.getClassLoader().
                getResourceAsStream("pl.compprog.logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
