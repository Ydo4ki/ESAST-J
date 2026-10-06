package com.ydo4ki.esast;

import java.io.*;
import java.util.Iterator;

/**
 * A token output that reads tokens from a source.
 *
 * <p>This class implements {@link Iterable} and produces a sequence of
 * {@link Token} objects by lexing the source code.</p>
 *
 * @see Token
 * @see TokenType
 * @see BracketsTypes
 */
public final class TokenOutput implements Iterable<Token> {

	/**
	 * Creates a {@code TokenOutput} by reading the entire content of the given
	 * input stream as UTF-8.
	 *
	 * @param in the input stream to read from
	 * @return a new {@code TokenOutput}
	 * @throws NullPointerException if {@code in} is {@code null}
	 * @throws RuntimeException     if an I/O error occurs while reading the stream
	 */
	public static TokenOutput valueOf(InputStream in) {
		return new TokenOutput(readStream(in), null, BracketsTypes.bracketsTypes);
	}

	// todo: add support for infinite streams
	private static String readStream(InputStream inputStream) {
		if (inputStream == null) {
			throw new NullPointerException();
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			String line;
			while ((line = reader.readLine()) != null) {
				if (!first)
					sb.append('\n');

				sb.append(line);
				first = false;
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception ignored) { }
			}
		}
	}
	
	private final String source;
	private final File file;
	private final BracketsTypes bracketsTypes;

	/**
	 * Creates a {@code TokenOutput} from the given source code, file, and
	 * bracket types.
	 *
	 * @param source        the source code
	 * @param file          the source file, or {@code null} if unknown
	 * @param bracketsTypes the bracket types to recognize
	 */
	public TokenOutput(String source, File file, BracketsTypes bracketsTypes) {
		this.source = source;
		this.file = file;
		this.bracketsTypes = bracketsTypes;
	}

	/*
	 * Creates a {@code TokenOutput} by reading the entire content of the given
	 * file as UTF-8.
	 *
	 * @param file          the path to the source file
	 * @param bracketsTypes the bracket types to recognize
	 * @throws IOException if an I/O error occurs while reading the file
	 */
//	public TokenOutput(Path file, BracketsTypes bracketsTypes) throws IOException {
//		this(String.join("\n", Files.readAllLines(file)), file.toFile(), bracketsTypes);
//	}

	/**
	 * Creates a {@code TokenOutput} by reading the entire content of the given
	 * file as UTF-8.
	 *
	 * @param file          the source file
	 * @param bracketsTypes the bracket types to recognize
	 * @throws IOException if an I/O error occurs while reading the file
	 */
	public TokenOutput(File file, BracketsTypes bracketsTypes) throws IOException {
		this($esast$1$0$0.readAllLinesJoined(file), file, bracketsTypes);
	}

	/**
	 * Returns the bracket types recognized by this token output.
	 *
	 * @return the bracket types
	 */
	public BracketsTypes getBracketsTypes() {
		return bracketsTypes;
	}

	/**
	 * Returns an iterator over the tokens.
	 *
	 * @return an iterator
	 */
	// @Override
	public Iterator<Token> iterator() {
		return new TokenIterator();
	}

	/**
	 * An iterator that lexes the source code into tokens.
	 */
	private class TokenIterator implements Iterator<Token> {
		
		private int pos = 0;
		private int line = 1;
		
		private Exception exception = null;
		
		private Token next = nextToken();
		
		// @Override
		public boolean hasNext() {
			return next.type != TokenType.EOF;
		}
		
		// @Override
		public Token next() {
			Token token = next;
			next = nextToken();
			return token;
		}

		// @Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		private Token nextToken() {
			char ch = nextChar();
			
			// skip whitespace
			while (Character.isWhitespace(ch)) {
				if (ch == '\n') line++;
				ch = nextChar();
			}
			
			if (ch == '\0')
				return new Token(TokenType.EOF, pos - 1, pos, line, file, source);
			
			
			// brackets
			{
				TokenType type;
				BracketsType bracketsType = bracketsTypes.byOpen(ch);
				if (bracketsType == null) {
					bracketsType = bracketsTypes.byClose(ch);
					type = TokenType.CLOSE;
				} else type = TokenType.OPEN;
				if (bracketsType != null) return new Token(type, String.valueOf(ch), pos - 1, pos, line, file, source);
			}
			
			// comments
			if (ch == '/') {
				Token comment = readComment(ch);
				if (comment != null) return comment;
			}
			
			// string literals
			if (ch == '"' || ch == '\'') {
				return readLiteral(ch);
			}
			
			// identifiers
			if (isValidNameChar(bracketsTypes, ch)) {
				return readIdentifier(ch);
			}
			
			exception = new Exception("I have no idea what this is: " + ch);
			return new Token(TokenType.ERROR, String.valueOf(ch), pos - 1, pos, line, file, source);
		}

		private Token readComment(char ch) {
			char next = seeNextChar();
			if (next != '/' && next != '*') {
				return null;
			}
			
			int startpos = pos - 1;
			StringBuilder builder = new StringBuilder();
			boolean isLineComment = nextChar() == '/';
			
			if (isLineComment) {
				readUntil('\n', builder);
				pos--;
			} else {
				readMultilineComment(builder);
				pos++;
			}
			
			return new Token(TokenType.COMMENT, builder.toString(), startpos, pos, line, file, source);
		}
		
		private void readUntil(char end, StringBuilder builder) {
			char ch;
			while ((ch = nextChar()) != end && ch != '\0') {
				builder.append(ch);
			}
		}
		
		private void readMultilineComment(StringBuilder builder) {
			char ch;
			while (true) {
				ch = nextChar();
				if (ch == '*' && seeNextChar() == '/') {
					break;
				}
				if (ch == '\n') {
					line++;
				}
				builder.append(ch);
			}
		}

		private Token readLiteral(char ch) {
			int startpos = pos - 1;
			char separator = ch;
			StringBuilder builder = new StringBuilder();
			builder.append(separator);
			while ((ch = nextChar()) != separator) {
				if (ch == '\\') {
					ch = nextChar();
					builder.append('\\');
					if (ch == separator) {
						builder.setLength(builder.length() - 1);
					}
				}
				builder.append(ch);
			}
			builder.append(separator);
			return new Token(TokenType.STRING, builder.toString(), startpos, pos, line, file, source);
		}
		
		private Token readIdentifier(char ch) {
			int startpos = pos - 1;
			StringBuilder builder = new StringBuilder().append(ch);
			while (isValidNameChar(bracketsTypes, ch = nextChar())) {
				builder.append(ch);
			}
			pos--;
			return new Token(TokenType.IDENTIFIER, builder.toString(), startpos, pos, line, file, source);
		}
		
		private char nextChar() {
			if (pos >= source.length()) {
				pos++;
				return '\0';
			}
			return source.charAt(pos++);
		}
		
		private char seeNextChar() {
			if (pos >= source.length()) return '\0';
			return source.charAt(pos);
		}
	}

	private static boolean isValidNameChar(BracketsTypes bracketsTypes, char ch) {
		return ch != '\0' && !Character.isWhitespace(ch) && !bracketsTypes.isBracket(ch);
	}
}
