package me.oegodf.mta.ui;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import me.oegodf.mta.main.Settings;
import me.oegodf.mta.main.Translation;
import me.oegodf.mta.reader.ErrorReader;
import me.oegodf.mta.reader.MtaError;
import me.oegodf.mta.reader.MtaErrorList;
import me.oegodf.mta.ui.MainMenuController;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class LoggerUi extends Application {

    private Stage mPrimaryStage;
    private AnchorPane mRootLayout;
    private MainMenuController mController;

    public void startUi() {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Settings.load();
        if (!Settings.get().isTranslationLoaded()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Translation not found");
            alert.setHeaderText(null);
            alert.setContentText("\""+Settings.get().language()+"\" language file not found\nНе найден файл перевода");
            alert.showAndWait();
            System.exit(0);
            return;
        }
        mPrimaryStage = primaryStage;
        primaryStage.setTitle(Translation.get().string("title"));
        initRootLayout();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/main_menu.fxml"));
            mRootLayout = loader.load();
            mController = loader.getController();
            Scene scene = new Scene(mRootLayout);
            mPrimaryStage.setScene(scene);
            mPrimaryStage.show();
            URL resource = getClass().getResource("/lua-keywords.css");
            scene.getStylesheets().add(resource.toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/main_menu.css").toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
