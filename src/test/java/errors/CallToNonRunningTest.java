package errors;

import me.oegodf.mta.errors.CallToNonRunning;
import me.oegodf.mta.reader.MtaError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CallToNonRunningTest {
    private static CallToNonRunning errorClass;
    private static MtaError error;

    @BeforeAll
    static void initAll() {
        errorClass = new CallToNonRunning();
        String errorText = "[2017-08-25 19:27:23] ERROR: shop-system/faction_drop_s.lua:242: exports: Call to non-running server resource (global) [string \"?\"].";
        error = new MtaError(null,"shop-system", errorText, null, 0, 0, null, 0,null);
    }

    @Test
    void errorPassesTest() {
        assertTrue(errorClass.checkErrorPasses(error));
    }
    @Test
    void descriptionTest() {
        String result = "Попытка обращения к не запущенному ресурсу global";
        assertEquals(result, errorClass.getDescription(error));
    }
    @Test
    void suggestionTest() {
        String result = "Добавьте в meta.xml файл ресурса shop-system строку <include resource=\"global\">";
        assertEquals(result, errorClass.getSuggestion(error));
    }
    @Test
    void errorNotPassesTest() {
        String errorText = "[2017-10-14 00:38:48] WARNING: DayZ/survivorSystem_client.lua(Line 584) [Client] setControlState is deprecated and may not work in future versions. Please replace with setPedControlState.";
        MtaError error = new MtaError(null,null, errorText, null, 0, 0, null, 0,null);
        assertFalse(errorClass.checkErrorPasses(error));
    }
}
