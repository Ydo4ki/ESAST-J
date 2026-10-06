package com.ydo4ki.esast;

/**
 * The type of a token produced by {@link TokenOutput}.
 *
 * @see Token
 */
public enum TokenType {
	/**
	 * End of file. Indicates that there are no more tokens.
	 */
	EOF,
	/**
	 * An error token. Produced when the lexer encounters an unexpected character.
	 */
	ERROR,
	/**
	 * An identifier token. Represents a sequence of characters that form a name.
	 */
	IDENTIFIER,
	/**
	 * A comment token. Represents either a line comment
	 * or a block comment.
	 */
	COMMENT,
	/**
	 * A string literal token. Conventionally represents a string enclosed in single or double quotes.
	 */
	STRING,
	/**
	 * An opening bracket token. Represents one of the opening brackets
	 * defined in {@link BracketsTypes}.
	 */
	OPEN,
	/**
	 * A closing bracket token. Represents a closing brackets of {@link BracketsType}.
	 */
	CLOSE,
}