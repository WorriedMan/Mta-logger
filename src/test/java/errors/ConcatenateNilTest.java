package errors;

import me.oegodf.mta.errors.ConcatenateNilValue;
import me.oegodf.mta.errors.LineDeprecated;
import me.oegodf.mta.reader.MtaError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConcatenateNilTest {
    private static ConcatenateNilValue errorType;
    private static MtaError error;

    @BeforeAll
    static void initAll() {
        errorType = new ConcatenateNilValue();
        String errorText = "[2017-08-25 19:27:23] ERROR: shop-system/faction_drop_s.lua:242: attempt to concatenate a nil value";
        error = new MtaError(null,null, errorText, null, 0, 0, null, 0,null);
    }

    @Test
    void errorPassesTest() {
        assertTrue(errorType.checkErrorPasses(error));
    }
    @Test
    void errorNotPassesTest() {
        String errorText = "[2017-08-25 19:27:23] ERROR: shop-system/faction_drop_s.lua:242: exports: Call to non-running server resource (global) [string \"?\"]  [DUP x2]";
        MtaError error = new MtaError(null,null, errorText, null, 0, 0, null, 0,null);
        assertFalse(errorType.checkErrorPasses(error));
    }
}
