package me.oegodf.mta.ui;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import me.oegodf.mta.reader.MtaError;
import me.oegodf.mta.reader.MtaErrorList;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;


public class MainMenuController {
    @FXML
    private TableView<MtaError> mErrorsTable;
    @FXML
    private CheckBox mShowOnlyLastLaunchCheckbox;
    @FXML
    private TextField mSearchField;

    private ErrorTableColumn<String> mTextColumn;
    private ErrorTableColumn<String> mDateColumn;
    private ErrorTableColumn<Integer> mDupColumn;
    private ErrorTableColumn<String> mResourceColumn;
    private ObservableList<MtaError> mMtaErrors = FXCollections.observableArrayList();
    private MtaErrorList mMtaErrorList;
    private Disposable mSearchDisposable;
    private SearchFilter mSearchFilter;

    @FXML
    private void initialize() {
        mSearchFilter = new SearchFilter();
        createColumns();
        setTableRowFactory();
        loadCheckbox();
        loadSearchField();

        mErrorsTable.setItems(mMtaErrors);
        mErrorsTable.setRowFactory(tv -> new TableRow<MtaError>() {
            public void updateItem(MtaError item, boolean empty) {
                super.updateItem(item, empty) ;
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

        FilterCallable filter = new FilterCallable(mMtaErrorList,mSearchFilter);

        Flowable<MtaErrorList> readerLoader = Flowable.fromCallable(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single());
        mSearchDisposable = readerLoader.subscribe(next -> {
            mMtaErrors.clear();
            mMtaErrors.addAll(next);
        });

    }


    private void loadCheckbox() {
        mShowOnlyLastLaunchCheckbox.setTooltip(new Tooltip("Показывать ошибки только с последнего запуска сервера"));
        mShowOnlyLastLaunchCheckbox.setOnAction(action -> {
            if (mShowOnlyLastLaunchCheckbox.isSelected())
                mSearchFilter.setLastLaunch(mMtaErrorList.getLastServerStartId());
            else
                mSearchFilter.setLastLaunch(-1);
            startSearch();
        });
    }

    public void setMtaErrors(MtaErrorList list) {
        mMtaErrorList = list;
        refreshMtaErrors();
    }

    private void refreshMtaErrors() {
        System.out.println("mMtaErrors prev "+mMtaErrors.size());
        if (mShowOnlyLastLaunchCheckbox.isSelected()) {
            mMtaErrors.clear();
            mMtaErrorList.stream()
                    .filter(error -> error.getServerStartId() == mMtaErrorList.getLastServerStartId())
                    .forEach(mMtaErrors::add);
        } else {
            mMtaErrors.addAll(mMtaErrorList);
        }
        System.out.println("mMtaErrors "+mMtaErrors.size());
    }

    private void createColumns() {
        mTextColumn = new ErrorTableColumn<>(mErrorsTable,"Ошибка",0.4,"Текст ошибки", cellData -> cellData.getValue().getErrorTextProperty());
        mDateColumn = new ErrorTableColumn<>(mErrorsTable,"Дата",0.2,"Дата ошибки", cellData -> cellData.getValue().getDateProperty());
        mDupColumn = new ErrorTableColumn<>(mErrorsTable,"DUP",0.1,"Сколько раз подряд ошибка повторилась за короткое время", cellData -> cellData.getValue().getDupProperty().asObject());
        mResourceColumn = new ErrorTableColumn<>(mErrorsTable,"Ресурс",0.3,"Ресурс, в котором произошла ошибка", cellData -> cellData.getValue().getResourceProperty());
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
