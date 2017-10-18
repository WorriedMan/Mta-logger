package me.oegodf.mta.ui.codeloader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import me.oegodf.mta.errors.ErrorLines;
import me.oegodf.mta.reader.MtaError;

import java.io.IOException;
import java.net.URL;

public class CodeLoader {
    private MtaError mError;
    private ErrorLines mErrorLines;
    private Stage mStage;

    public CodeLoader(MtaError error, ErrorLines errorLines) {
        mError = error;
        mErrorLines = errorLines;
        initCodeRootLayout();
    }

    private void initCodeRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/code_loader.fxml"));
            AnchorPane mRootLayout = loader.load();
            CodeLoaderController controller = loader.getController();
            controller.setSourceCode(mErrorLines);
            mStage = new Stage();
            mStage.setTitle(mError.getFileName() +":"+mError.getFileLine()+" ["+mError.getErrorText()+"]");
            Scene scene = new Scene(mRootLayout);
            mStage.setScene(scene);
            mStage.show();
            URL resource = getClass().getResource("/lua-keywords.css");
            scene.getStylesheets().add(resource.toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        mStage.hide();
    }
}
