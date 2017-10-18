package me.oegodf.mta.main;

public class MtaUtil {
    private MtaUtil(){}

    public static String readableByteCount(long bytes) {
        int unit = 1024;
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        return String.format("%.1f", bytes / Math.pow(unit, exp));
    }

}
