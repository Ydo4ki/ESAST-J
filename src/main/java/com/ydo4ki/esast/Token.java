package com.ydo4ki.esast;

import java.io.File;

/**
 * Represents a lexical token produced by {@link TokenOutput}.
 *
 * <p>A token has a {@link TokenType}, a text value, and a {@link Location}
 * in the source code.</p>
 *
 * @see TokenType
 * @see Location
 */
public final class Token {

    /**
     * The type of this token.
     */
    public final TokenType type;

    /**
     * The text value of this token.
     */
    public final String text;

    /**
     * The location of this token in the source code.
     */
    public final Location location;

    /**
     * Creates a token with the given type, text, and location.
     *
     * @param type     the token type
     * @param text     the text value
     * @param startpos the start position (character offset)
     * @param endpos   the end position (character offset)
     * @param line     the line number
     * @param file     the source file
     * @param source   the source code
     */
    public Token(TokenType type, String text, int startpos, int endpos, int line, File file, String source) {
        this.type = type;
        this.text = text;
        this.location = new Location(startpos, endpos, line, line, file, source);
    }

	/**
	 * Creates an empty token with the given type and location.
	 *
	 * @param type     the token type
	 * @param startpos the start position (character offset)
	 * @param endpos   the end position (character offset)
	 * @param line     the line number
	 * @param file     the source file
	 * @param source   the source code
	 */
    public Token(TokenType type, int startpos, int endpos, int line, File file, String source) {
        this(type, "\0", startpos, endpos, line, file, source);
    }

	/**
	 * Returns a string representation of this token.
	 *
	 * @return the string representation
	 */
    // @Override
    public String toString() {
        return type + "(" +
                (text != null ? "'" + text + '\'' : "") +
                ", startpos=" + location.getStartPos() +
                ", endpos=" + location.getEndPos() +
                ", line=" + location.getStartLine() +
                ')';
    }
}
