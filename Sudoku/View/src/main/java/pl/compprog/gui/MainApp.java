package pl.compprog.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;



public class MainApp extends Application {
    private static MainApp instance;
    private MainView controller;

    public MainApp() {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("MainApp already created!");
        }
    }

    public static MainApp getInstance() {
        return instance;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void start(Stage stage) throws Exception {
        URL location = getClass().getResource("/pl/compprog/gui/MainView.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setResources(ResourceBundle.getBundle("i18n.SudokuBundle", new Locale("en", "EN")));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        //stage.setTitle("Sudoku Game");
        stage.setScene(scene);
        controller = fxmlLoader.getController();
        stage.setTitle(controller.getCurrentBundle().getString("sudoku_game"));
        controller.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public MainView getController() {
        return controller;
    }
}
