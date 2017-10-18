package me.oegodf.mta.reader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MtaLab {

    private Pattern mResourceNamePattern = Pattern.compile(".*\\[[a-zA-Z0-9\\-_]+.]/");
    private Pattern mErrorLinePattern = Pattern.compile(":[0-9]+:|\\(Line [0-9]+\\)");
    private Pattern mErrorLineNumberPattern = Pattern.compile("[0-9]+");
    private SimpleDateFormat mLineTimeDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    private SimpleDateFormat mErrorDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    enum Error {
        ERROR,
        WARNING,
        SCRIPT_ERROR
    }
    MtaLab() {

    }

    MtaError createError(String line, int serverStartId) {
        Error type = getLineType(line);
        if (type != null && !line.contains("Loading script failed") && !line.contains("please recompile")) {
            Date date = getLineTime(line);
            FileAndLineParseResult fileAndLineNum = getLineFileAndLine(line, type);
            String[] errorAndDup = null;
            String resourceName = null;
            if (fileAndLineNum != null && fileAndLineNum.getFile() != null && fileAndLineNum.getLine() != null) {
                resourceName = getLineResourceName(fileAndLineNum.getFile());
                errorAndDup = getLineErrorText(line, fileAndLineNum);
            }
            if (date != null && errorAndDup != null && resourceName != null) {
                int dupAmount;
                try {
                    dupAmount = Integer.parseInt(errorAndDup[1]);
                } catch (Exception e) {
                    return null;
                }
                return new MtaError(type, resourceName, errorAndDup[0], fileAndLineNum.getFile(), fileAndLineNum.getLine(), dupAmount, date, serverStartId, mErrorDateFormat);
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
        } else if (line.contains("Unknown script type specified")) {
            return Error.WARNING;
        } else if (line.contains("] SCRIPT ERROR: ")) {
            return Error.SCRIPT_ERROR;
        }
        return null;
    }

    private String getLineResourceName(String fileName) {
        String commonString = fileName.replaceAll("\\\\", "/");
        Matcher matcher = mResourceNamePattern.matcher(commonString);
        if (matcher.find()) {
            int matcherEnd = matcher.end();
            commonString = commonString.substring(matcherEnd);
        }
        return commonString.replaceAll("/.+", "");
    }

    private Date getLineTime(String line) {
        String dateString = line.substring(1, 20);
        Date date = null;
        try {
            date = mLineTimeDateFormat.parse(dateString);
        } catch (ParseException ignored) {
        }
        return date;
    }

    private FileAndLineParseResult getLineFileAndLine(String line, Error type) {
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
            String lineNum = null;
            Integer lineNumInteger = null;
            Integer matcherEnd = 0;
            Matcher matcher = mErrorLinePattern.matcher(restOfTheString);
            if (matcher.find()) {
                int matcherStart = matcher.start();
                matcherEnd = matcher.end();
                file = restOfTheString.substring(0,matcherStart);
                String lineString = restOfTheString.substring(matcherStart,matcherEnd);
                matcher = mErrorLineNumberPattern.matcher(lineString);
                if (matcher.find()) {
                    lineNum = lineString.substring(matcher.start(),matcher.end());
                } else {
                    lineNum = "0";
                }
            }
            try {
                assert lineNum != null;
                lineNumInteger = Integer.parseInt(lineNum);
            } catch (Exception e) {
                lineNumInteger = 0;
            }
            return new FileAndLineParseResult(file,lineNumInteger,matcherEnd+start);
        }
        return null;
    }

    private String[] getLineErrorText(String line, FileAndLineParseResult lineParseResult) {
        int startPosition = lineParseResult.getLineEndPosition()+1;
        int dupPosition = line.indexOf("[DUP x");
        String dupAmount = "0";
        Integer endPosition = line.length();
        if (dupPosition != -1) {
            endPosition = dupPosition;
            dupAmount = line.substring(dupPosition + 6, line.length() - 1);
        }
        String errorText = line.substring(startPosition, endPosition);
        return new String[]{errorText, dupAmount};
    }

}
