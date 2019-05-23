package pl.compprog;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class LogConfigurator {
    static {
        System.out.println("AAAA");
        InputStream stream = LogConfigurator.class.
                getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
