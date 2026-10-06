package com.ydo4ki.esast;

import java.io.File;
import java.io.PrintStream;

/**
 * Exception that happened during parsing of the ESAST.
 */
public class EsastException extends RuntimeException {
    private final Location location;
    private final String rawMessage;

    /**
     * @return location in source code where the exception happened
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return raw exception text for additional information
     */
    public String getRawMessage() {
        return rawMessage;
    }

    public EsastException(Location location, String message) {
        super(message);
        this.location = location;
        this.rawMessage = message;
    }

    public EsastException(Location location, String message, Throwable cause) {
        super(message, cause);
        this.location = location;
        this.rawMessage = message;
    }


    public EsastException(Location location, String message, String rawMessage) {
        super(message);
        this.location = location;
        this.rawMessage = rawMessage;
    }

    public EsastException(Location location, String message, Throwable cause, String rawMessage) {
        super(message, cause);
        this.location = location;
        this.rawMessage = rawMessage;
    }

    public EsastException(Location location, Throwable cause, String rawMessage) {
        super(cause);
        this.location = location;
        this.rawMessage = rawMessage;
    }

    /**
     * Prints readable error details in {@code err}.
     * @param err {@link PrintStream} to print details to
     * @param e exception to print
     * @param source source code where the exception happened
     * @param file source file
     * @return {@code e}
     */
    public static EsastException handleException(PrintStream err, EsastException e, String source, File file) {
        String filename = file == null ? "unknown" : file.getPath();
        err.println(e.getErrorDescription(filename, source));
        if (e.getCause() != e && e.getCause() instanceof EsastException) {
            err.println("for:");
            err.println(((EsastException) e.getCause()).getErrorDescription(filename, source));
        }
        return e;
    }

    /**
     * Constructs a detailed error description readable to human eye.
     * @param filename displayed source name
     * @param source source code read
     * @return detailed error description readable to human eye
     */
    public String getErrorDescription(String filename, String source) {
        StringBuilder msg = new StringBuilder(getClass().getSimpleName()).append(": ").append(getRawMessage()).append(" (")
                .append(filename);
        if (getLocation() != null) {
            msg.append(':').append(getLocation().getStartLine());
        }
        msg.append(')').append("\n\n");

        if (getLocation() != null) {
            String line;
            try {
                line = source.split("\n")[getLocation().getStartLine() - 1];
            } catch (ArrayIndexOutOfBoundsException ee) {
                line = " ";
            }

            int linePos = source.substring(0, getLocation().getStartPos()).lastIndexOf('\n');
            int errStart = getLocation().getStartPos() - linePos;
            int errEnd = getLocation().getEndPos() - linePos;

            msg.append(line).append('\n');

            char[] underline = new char[line.length()];
            for (int i = 0; i < underline.length; i++) {
                if (i >= errStart - 1 && i < errEnd - 1) underline[i] = '~';
                else if (line.charAt(i) == '\t') underline[i] = '\t';
                else underline[i] = ' ';
            }
            return msg.append(underline).append('\n').toString();
        }

        return msg.append('\n').toString();
    }
}
