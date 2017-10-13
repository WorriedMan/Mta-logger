package me.oegodf.mta.ui;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import me.oegodf.mta.reader.ErrorReader;
import me.oegodf.mta.reader.MtaError;
import me.oegodf.mta.reader.MtaErrorList;

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
        mPrimaryStage = primaryStage;
        primaryStage.setTitle("A simple FXML Example");
        initRootLayout();

        ErrorReader reader = new ErrorReader("server.log");
        if (mController != null) {
            Flowable<MtaErrorList> readerLoader = Flowable.fromCallable(reader)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.single());
            readerLoader.subscribe(next -> mController.setMtaErrors(next));
        }
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initCodeRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/logger.fxml"));
            mRootLayout = loader.load();
//            mController = loader.getController();
            Scene scene = new Scene(mRootLayout);
            mPrimaryStage.setScene(scene);
            mPrimaryStage.show();
            URL resource = getClass().getResource("/lua-keywords.css");
            System.out.println("RES " + resource);
            scene.getStylesheets().add(resource.toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
