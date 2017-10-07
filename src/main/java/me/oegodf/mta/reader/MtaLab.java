package me.oegodf.mta.reader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MtaLab {

    enum Error {
        ERROR,
        WARNING,
        SCRIPT_ERROR
    }

    MtaLab() {}

    MtaError createError(String line) {
        Error type = getLineType(line);
        if (type != null && !line.contains("Loading script failed") && !line.contains("please recompile")) {
            Date date = getLineTime(line);
            String[] fileAndLineNum = getLineFileAndLine(line, type);
            String[] errorAndDup = null;
            String resourceName = null;
            if (fileAndLineNum != null) {
                resourceName = getLineResourceName(fileAndLineNum[0]);
                errorAndDup = getLineErrorText(line, fileAndLineNum[1]);
            }
            if (date != null && errorAndDup != null && resourceName != null) {
                int fileLine;
                int dupAmount;
                try {
                    fileLine = Integer.parseInt(fileAndLineNum[1]);
                    dupAmount = Integer.parseInt(errorAndDup[1]);
                } catch (Exception e) {
                    return null;
                }
                return new MtaError(type, resourceName, errorAndDup[0], fileAndLineNum[0], fileLine, dupAmount);
            }
            return null;
        }
        return null;
    }

    private Error getLineType(String line) {
        if (line.contains("] WARNING: ")) {
            return Error.WARNING;
        } else if (line.contains("] ERROR: ")) {
            return Error.ERROR;
        } else if (line.contains("] SCRIPT ERROR: ")) {
            return Error.SCRIPT_ERROR;
        }
        return null;
    }

    private String getLineResourceName(String fileName) {
        String commonString = fileName.replaceAll("\\\\", "/");
        Pattern p = Pattern.compile(".*\\[[a-zA-Z0-9]+.]/");
        Matcher matcher = p.matcher(commonString);
        if (matcher.find()) {
            int matcherEnd = matcher.end();
            commonString = commonString.substring(matcherEnd);
        }
        return commonString.replaceAll("/.+", "");
    }

    private Date getLineTime(String line) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        String dateString = line.substring(1, 20);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException ignored) {
        }
        return date;
    }

    private String[] getLineFileAndLine(String line, Error type) {
        int start = 0;
        switch (type) {
            case ERROR:
                start = 29;
                break;
            case WARNING:
                start = 31;
                break;
            case SCRIPT_ERROR:
                start = 36;
                break;
        }
        if (start > 0) {
            String restOfTheString = line.substring(start);
            String file = null;
            String lineNum;
            int fileEnd = 0;
            for (int i = 0; i < restOfTheString.length(); i++) {
                if (restOfTheString.charAt(i) == ':') {
                    if (file == null) {
                        file = restOfTheString.substring(0, i);
                        fileEnd = i;
                    } else {
                        lineNum = restOfTheString.substring(fileEnd + 1, i);
                        return new String[]{file, lineNum};
                    }
                }
            }
        }
        return null;
    }

    private String[] getLineErrorText(String line, String lineNum) {
        String lineNumText = ":" + lineNum + ": ";
        int sartPosition = line.indexOf(lineNumText) + lineNumText.length();
        int dupPosition = line.indexOf("[DUP x");
        String dupAmount = "0";
        Integer endPosition = line.length();
        if (dupPosition != -1) {
            endPosition = dupPosition;
            dupAmount = line.substring(dupPosition + 6, line.length() - 1);
        }
        String errorText = line.substring(sartPosition, endPosition);
        return new String[]{errorText, dupAmount};
    }

    private int[] startEndPosition(String sentence, String word) {
        int startingPosition = sentence.indexOf(word);
        int endingPosition = startingPosition + word.length();
        return new int[]{startingPosition, endingPosition};
    }
}
