package com.ydo4ki.esast;

import java.io.File;
import java.io.IOException;
import java.util.WeakHashMap;

/**
 * A location in a source file.
 *
 * <p>Stores start and end positions (character offsets), start and end line
 * numbers, and the source file. Optionally, the source code can be provided
 * directly; otherwise it is lazily read from the file.</p>
 *
 * @author Sulphuris
 * @since 4/11/2025 1:04 AM
 */
public final class Location {
    private final int startPos;
    private final int endPos;
    private final int startLine;
    private final int endLine;
    private final File sourceFile;
    private String source;

    /**
     * Creates a location with the given parameters.
     *
     * @param startPos   the start position (character offset)
     * @param endPos     the end position (character offset)
     * @param startLine  the start line number
     * @param endLine    the end line number
     * @param sourceFile the source file; must not be {@code null}
     * @throws NullPointerException if {@code sourceFile} is {@code null}
     */
    public Location(int startPos, int endPos, int startLine, int endLine, File sourceFile) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.startLine = startLine;
        this.endLine = endLine;
        if (sourceFile == null)
            throw new NullPointerException("sourceFile is null");
        this.sourceFile = sourceFile;
    }

    /**
     * Creates a location with the given parameters and source code.
     *
     * @param startPos   the start position (character offset)
     * @param endPos     the end position (character offset)
     * @param startLine  the start line number
     * @param endLine    the end line number
     * @param sourceFile the source file
     * @param source     the source code, or {@code null} to read from the file
     * @throws NullPointerException if both {@code source} and {@code sourceFile}
     *                              are {@code null}
     */
    public Location(int startPos, int endPos, int startLine, int endLine, File sourceFile, String source) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.startLine = startLine;
        this.endLine = endLine;
        if (source == null && sourceFile == null) {
            throw new NullPointerException("No source information provided");
        }
        this.sourceFile = sourceFile;
        this.source = source;
    }

    /**
     * Returns the start position (character offset).
     *
     * @return the start position
     */
    public int getStartPos() {
        return startPos;
    }

    /**
     * Returns the end position (character offset).
     *
     * @return the end position
     */
    public int getEndPos() {
        return endPos;
    }

    /**
     * Returns the start line number.
     *
     * @return the start line number
     */
    public int getStartLine() {
        return startLine;
    }

    /**
     * Returns the end line number.
     *
     * @return the end line number
     */
    public int getEndLine() {
        return endLine;
    }

    /**
     * Returns the source file.
     *
     * @return the source file
     */
    public File getSourceFile() {
        return sourceFile;
    }

    private static final WeakHashMap<File, String> sources_cache = new WeakHashMap<File, String>();

    /**
     * Returns the source code.
     *
     * <p>If the source was provided directly, it is returned as is. Otherwise,
     * it is lazily read from the source file.</p>
     *
     * @return the source code
     * @throws RuntimeException if an I/O error occurs while reading the file
     */
    public String getSource() {
        if (source != null) return source;
        String result;
        String v;
        if ((v = sources_cache.get(sourceFile)) == null) {
            String newValue;
            try {
                newValue = Esast.readAllLinesJoined(sourceFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sources_cache.put(sourceFile, newValue);
            result = newValue;
        } else {
            result = v;
        }

        return source = result;
    }

    /**
     * Creates a location spanning from the start of the first location to the
     * end of the second location.
     *
     * <p>If the source files differ, the first location is returned.</p>
     *
     * @param start the start location
     * @param end   the end location
     * @return the combined location
     */
    public static Location between(Location start, Location end) {
        Object a = end.getSourceFile();
        Object b = start.getSourceFile();
        if (!((a == b) || (a != null && a.equals(b))))
            return start;
        return new Location(start.startPos, end.endPos,
				start.startLine, end.endLine,
				end.getSourceFile(), end.source);
    }

	/**
	 * Creates an unknown location for the given source file and source code.
	 *
	 * @param src the source file
	 * @param source the source code
	 * @return an unknown location
	 */
	@Deprecated
    private static Location unknown(File src, String source) {
        return new Location(0, 0, 0, 0, src, source);
    }

	/**
	 * Creates an unknown location for the given source file.
	 *
	 * @param src the source file
	 * @return an unknown location
	 */
	@Deprecated
    private static Location unknown(File src) {
        return new Location(0, 0, 0, 0, src, null);
    }
}