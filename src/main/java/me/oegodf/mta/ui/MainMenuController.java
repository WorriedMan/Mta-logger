package me.oegodf.mta.ui;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import me.oegodf.mta.errors.ErrorLines;
import me.oegodf.mta.errors.ErrorSuggestion;
import me.oegodf.mta.main.MtaUtil;
import me.oegodf.mta.main.Settings;
import me.oegodf.mta.main.Translation;
import me.oegodf.mta.reader.ErrorReader;
import me.oegodf.mta.reader.MtaError;
import me.oegodf.mta.reader.MtaErrorList;
import me.oegodf.mta.reader.LogLoadingResult;
import me.oegodf.mta.ui.codeloader.CodeLoader;


public class MainMenuController {
    @FXML
    private AnchorPane mMainPane;
    @FXML
    private AnchorPane mFilterPane;
    @FXML
    private AnchorPane mSettingsPane;
    @FXML
    private AnchorPane mInfoPane;
    @FXML
    private TableView<MtaError> mErrorsTable;
    @FXML
    private CheckBox mShowOnlyLastLaunchCheckbox;
    @FXML
    private TextField mSearchField;
    @FXML
    private Button mShowErrorLine;
    @FXML
    private Label mFiltrationLabel;
    @FXML
    private Label mInformationLabel;

    private ErrorTableColumn<String> mTextColumn;
    private ErrorTableColumn<String> mDateColumn;
    private ErrorTableColumn<Integer> mDupColumn;
    private ErrorTableColumn<String> mResourceColumn;
    private ObservableList<MtaError> mMtaErrors = FXCollections.observableArrayList();
    private MtaErrorList mMtaErrorList;
    private Disposable mSearchDisposable;
    private SearchFilter mSearchFilter;
    private CodeLoader mCurrentCodeLoader;
    private LogLoadingProgressBar mProgressBar;

    @FXML
    private void initialize() {
        loadTranslation();
        mMainPane.setDisable(true);
        mSearchFilter = new SearchFilter();
        loadTextChangeFlowable();
        createColumns();
        setTableRowFactory();
        loadCheckbox();
        loadSearchField();
        loadLineWindow();
        mShowErrorLine.setOnAction(event -> showLineClicked());
        mErrorsTable.setOnMouseClicked(event -> errorsTableClicked());
        mMtaErrors.addListener((ListChangeListener<? super MtaError>) error -> {
            errorsTableClicked();
        });

        mErrorsTable.setItems(mMtaErrors);
        mErrorsTable.setRowFactory(tv -> new TableRow<MtaError>() {
            public void updateItem(MtaError item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getDupAmount() > 10) {
                    setStyle("-fx-background-color: tomato;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void loadTranslation() {
        mFiltrationLabel.setText(Translation.get().string("filtration"));
        mSearchField.setPromptText(Translation.get().string("search"));
        mShowOnlyLastLaunchCheckbox.setText(Translation.get().string("only_last_launch"));
        mInformationLabel.setText(Translation.get().string("info_label"));
    }

    private void loadTextChangeFlowable() {
        ErrorReader reader = new ErrorReader("server.log");
        if (reader.checkFileSize()) {
            showFileSizeAlert();
        }
        Flowable<LogLoadingResult> readerLoader = Flowable.create(reader, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single());
        readerLoader.subscribe(this::proceedMtaResult);

        mProgressBar = new LogLoadingProgressBar(mMainPane);
    }

    private void showFileSizeAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(Translation.get().string("big_file"));
        alert.setHeaderText(null);
        long maxSize = Settings.get().maxFileLength();
        alert.setContentText(String.format(Translation.get().string("big_file_description"),MtaUtil.readableByteCount(maxSize)));
        alert.showAndWait();
    }

    private void errorsTableClicked() {
        MtaError error = mErrorsTable.getSelectionModel().getSelectedItem();
        mShowErrorLine.setDisable(error == null);
    }

    private void showLineClicked() {
        if (mCurrentCodeLoader != null) {
            mCurrentCodeLoader.hide();
        }
        MtaError error = mErrorsTable.getSelectionModel().getSelectedItem();
        if (error != null) {
            ErrorSuggestion suggestion = error.getSuggestion();
            if (suggestion != null) {
                ErrorLines errorLines = suggestion.getErrorLines(error);
                if (errorLines != null) {
                    mCurrentCodeLoader = new CodeLoader(error, errorLines);
                } else {
                    showErrorAlert(Translation.get().string("file_line_not_found"), error.getFileName() + ": " + error.getFileLine());
                }
            } else {
                showErrorAlert(Translation.get().string("error_not_supports_line_showing"), Translation.get().string("error_not_supports_line_show_descr"));
            }
        }
    }

    private void showErrorAlert(String text, String bodyText) {
        Alert alert = new Alert(Alert.AlertType.ERROR, bodyText, ButtonType.OK);
        alert.setHeaderText(text);
        alert.setTitle(Translation.get().string("error"));
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    private void loadLineWindow() {

    }

    private void loadSearchField() {
        mSearchField.setOnKeyTyped(event -> {
            mSearchFilter.setTextFilter(mSearchField.getText());
            startSearch();
        });
    }

    private void startSearch() {
        if (mSearchDisposable != null && !mSearchDisposable.isDisposed()) {
            mSearchDisposable.dispose();
        }

        FilterCallable filter = new FilterCallable(mMtaErrorList, mSearchFilter);

        Flowable<MtaErrorList> readerLoader = Flowable.fromCallable(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single());
        mSearchDisposable = readerLoader.subscribe(next -> {
            mMtaErrors.clear();
            mMtaErrors.addAll(next);
        });
    }

    public void proceedMtaResult(LogLoadingResult result) {
        mProgressBar.setProgress(result.getProgress());
        MtaErrorList errors = result.getErrors();
        if (errors != null) {
            setMtaErrors(errors);
            if (result.isCompleted()) {
                mProgressBar.hide();
                mMainPane.setDisable(false);
            }
        }
    }

    public void setMtaErrors(MtaErrorList list) {
        mMtaErrorList = list;
        startSearch();
    }

    private void loadCheckbox() {
        mShowOnlyLastLaunchCheckbox.setTooltip(new Tooltip(Translation.get().string("only_last_launch_descr")));
        mShowOnlyLastLaunchCheckbox.setOnAction(action -> {
            if (mShowOnlyLastLaunchCheckbox.isSelected())
                mSearchFilter.setLastLaunch(mMtaErrorList.getLastServerStartId());
            else
                mSearchFilter.setLastLaunch(-1);
            startSearch();
        });
    }

    private void createColumns() {
        mTextColumn = new ErrorTableColumn<>(mErrorsTable, Translation.get().string("error"), 0.4, Translation.get().string("text_description"), cellData -> cellData.getValue().getErrorTextProperty());
        mDateColumn = new ErrorTableColumn<>(mErrorsTable, Translation.get().string("date"), 0.2, Translation.get().string("date_description"), cellData -> cellData.getValue().getDateProperty());
        mDupColumn = new ErrorTableColumn<>(mErrorsTable, "DUP", 0.1, Translation.get().string("dup_description"), cellData -> cellData.getValue().getDupProperty().asObject());
        mResourceColumn = new ErrorTableColumn<>(mErrorsTable, Translation.get().string("resource"), 0.3, Translation.get().string("resource_description"), cellData -> cellData.getValue().getResourceProperty());
    }

    private void setTableRowFactory() {
        mErrorsTable.setRowFactory(tv -> {
            TableRow<MtaError> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 1) {
//                    setImageView(row.getItem());
                }
            });
            return row;
        });
    }


}
