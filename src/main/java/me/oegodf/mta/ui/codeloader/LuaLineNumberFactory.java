package me.oegodf.mta.ui.codeloader;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import org.fxmisc.richtext.StyledTextArea;
import org.reactfx.collection.LiveList;
import org.reactfx.value.Val;

import java.util.function.IntFunction;

public class LuaLineNumberFactory implements IntFunction<Node> {
    private static final Insets DEFAULT_INSETS = new Insets(0.0, 5.0, 0.0, 5.0);
    private static final Paint DEFAULT_TEXT_FILL = Color.web("#666");
    private static final Font DEFAULT_FONT =
            Font.font("monospace", FontPosture.ITALIC, 13);
    private static final Background DEFAULT_BACKGROUND =
            new Background(new BackgroundFill(Color.web("#ddd"), null, null));
    private int mErrorLine;

    public static IntFunction<Node> get(StyledTextArea<?> area) {
        return get(area, 1, 1);
    }

    public static IntFunction<Node> get(
            StyledTextArea<?> area,
            int startLine,
            int errorLine) {
        return new LuaLineNumberFactory(area, startLine, errorLine);
    }

    private final Val<Integer> nParagraphs;
    private final int mStartLine;

    private LuaLineNumberFactory(
            StyledTextArea<?> area,
            int startLine,
            int errorLine) {
        this.mStartLine = startLine;
        nParagraphs = LiveList.sizeOf(area.getParagraphs());
        mErrorLine = errorLine;
    }

    @Override
    public Node apply(int idx) {
        Pane pane = new Pane();
        Val<String> formatted = nParagraphs.map(n -> format(idx + mStartLine));

        Label lineNo = new Label();
        lineNo.setFont(DEFAULT_FONT);
        lineNo.setBackground(DEFAULT_BACKGROUND);
        lineNo.setTextFill(DEFAULT_TEXT_FILL);
        lineNo.setPadding(DEFAULT_INSETS);
        lineNo.getStyleClass().add("lineno");

        lineNo.textProperty().bind(formatted.conditionOnShowing(lineNo));
        pane.getChildren().add(lineNo);
        if (idx == mErrorLine) {
            Polygon triangle = new Polygon(0.0, 0.0, 10.0, 5.0, 0.0, 10.0);
            triangle.setFill(Color.RED);
            pane.getChildren().add(triangle);
            triangle.setLayoutX(25);
            triangle.setLayoutY(2);
        }
        return pane;
    }

    private String format(int x) {
        if (x < 10) {
            return "0"+x;
        } else {
            return String.valueOf(x);
        }
    }
}
