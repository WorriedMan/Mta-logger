package me.oegodf.mta.ui;

import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class LogLoadingProgressBar {
    final private ProgressBar mBar;
    final private VBox mBox;

    LogLoadingProgressBar(Pane parent) {
        mBar = new ProgressBar(0);
        ProgressIndicator indicator = new ProgressIndicator();
        mBox = new VBox();
        mBox.setSpacing(5);
        mBox.setAlignment(Pos.CENTER);
        mBox.getChildren().addAll(indicator, mBar);

        AnchorPane.setLeftAnchor(mBox, 100.0);
        AnchorPane.setRightAnchor(mBox, 100.0);
        AnchorPane.setTopAnchor(mBox, 50.0);
        parent.getChildren().add(mBox);
    }

    public void setProgress(double value) {
        mBar.setProgress(value);
    }

    public void hide() {
        mBox.setVisible(false);
    }
}
