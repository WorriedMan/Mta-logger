package me.oegodf.mta.ui;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
import me.oegodf.mta.reader.MtaError;

class ErrorTableColumn<T> extends TableColumn<MtaError, T> {
    ErrorTableColumn(TableView<MtaError> table, String text, double size, String tooltipText, Callback<CellDataFeatures<MtaError, T>, ObservableValue<T>> cellValue ) {
        super("");
        Label label = new Label(text);
        label.setTooltip(new Tooltip(tooltipText));
        this.setGraphic(label);
        table.getColumns().add(this);
        this.prefWidthProperty().bind(table.widthProperty().multiply(size));
//        this.setResizable(false);
        this.setCellValueFactory(cellValue);
    }

}
