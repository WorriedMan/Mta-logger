package errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import me.oegodf.mta.errors.LineDeprecated;
import me.oegodf.mta.reader.MtaError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LineDeprecatedTest {
    private static LineDeprecated errorType;
    private static MtaError error;

    @BeforeAll
    static void initAll() {
        errorType = new LineDeprecated();
        String errorText = "[2017-10-14 00:38:48] WARNING: DayZ/survivorSystem_client.lua(Line 945) [Client] getControlState is deprecated and may not work in future versions. Please replace with getPedControlState.";
        error = new MtaError(null,null, errorText, null, 0, 0, null, 0,null);
    }

    @Test
    void errorPassesTest() {
        assertTrue(errorType.checkErrorPasses(error));
    }
    @Test
    void descriptionTest() {
        String result = "Функция getControlState является устаревшей и может не работать в будущих версиях МТА";
        assertEquals(result, errorType.getDescription(error));
    }
    @Test
    void suggestionTest() {
        String result = "Замените функцию getControlState на функцию getPedControlState в указанной строке (возможна смена аргументов)";
        assertEquals(result, errorType.getSuggestion(error));
    }
    @Test
    void errorNotPassesTest() {
        String errorText = "[2017-08-25 19:27:23] ERROR: shop-system/faction_drop_s.lua:242: exports: Call to non-running server resource (global) [DUP x2]";
        MtaError error = new MtaError(null,null, errorText, null, 0, 0, null, 0,null);
        assertFalse(errorType.checkErrorPasses(error));
    }
}
