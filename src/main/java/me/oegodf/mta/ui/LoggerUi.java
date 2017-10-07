package me.oegodf.mta.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.oegodf.mta.reader.ErrorReader;

import java.io.FileInputStream;
import java.io.IOException;

public class LoggerUi extends Application {

    private Stage mPrimaryStage;
    private AnchorPane mRootLayout;
    private LoggerController mController;

    public void startUi() {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mPrimaryStage = primaryStage;
        primaryStage.setTitle("A simple FXML Example");
        initRootLayout();
        ErrorReader reader = new ErrorReader("server.log");
        reader.load();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/logger.fxml"));
            mController = loader.getController();
            mRootLayout = loader.load();
            Scene scene = new Scene(mRootLayout);
            mPrimaryStage.setScene(scene);
            mPrimaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
