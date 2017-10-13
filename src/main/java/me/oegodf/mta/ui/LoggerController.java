package me.oegodf.mta.ui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.IndexRange;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.oegodf.mta.errors.ErrorLines;
import org.fxmisc.richtext.*;

import java.io.FileInputStream;
import java.util.*;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoggerController {
    @FXML
    private CodeArea mTextArea;
    private boolean mStylesApplied;

    private static final String[] KEYWORDS = new String[]{
            "end", "function", "break", "do", "double", "else",
            "while", "exports", "until", "local", "or", "in",
            "for", "if", "then", "not"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = ";";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "--";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    private ErrorLines mErrorLines;

    @FXML
    private void initialize() {
        mTextArea.setEditable(false);

        mTextArea.richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                .subscribe(change -> updateTextStyle());
    }

    private void updateTextStyle() {
        if (mStylesApplied) {
            return;
        }
        mStylesApplied = true;
        Matcher matcher = PATTERN.matcher(mTextArea.getText());
        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */
            assert styleClass != null;
            mTextArea.setStyleClass(matcher.start(), matcher.end(), styleClass);
        }
        int[] errorLocation = mErrorLines.getErrorLinePosition();
        for (int i = errorLocation[0]; i < errorLocation[0] + errorLocation[1]; i++) {
            if (!Objects.equals(mTextArea.getText(i, i + 1), "\t")) {
                StyleSpans<Collection<String>> oldStyles = mTextArea.getStyleSpans(i, i);
                List<String> styleList = new ArrayList<>(Collections.singletonList("error-line"));
                oldStyles.forEach(style -> styleList.addAll(style.getStyle()));
                StyleSpans<Collection<String>> newStyles = oldStyles.append(styleList, 1);
                mTextArea.setStyleSpans(i, newStyles);
            }
        }
    }

    public LoggerController() {
    }

    public void setSourceCode(ErrorLines errorLines) {
        mErrorLines = errorLines;
        setSourceCode(errorLines.getErrorLines(), errorLines.getStartIndex());
    }

    public void setSourceCode(String[] text, int startLine) {
        setSourceCode(String.join("\n", text), startLine);
    }

    public void setSourceCode(String text, int startLine) {
        mStylesApplied = false;
        IntFunction<Node> lineNumberFactory = LuaLineNumberFactory.get(mTextArea, startLine, mErrorLines.getErrorLineNum());
        lineNumberFactory.apply(startLine);
        mTextArea.setParagraphGraphicFactory(lineNumberFactory);
        mTextArea.replaceText(0, 0, text);
    }
}
