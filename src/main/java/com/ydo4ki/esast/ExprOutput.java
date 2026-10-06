package com.ydo4ki.esast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An expression output that parses a sequence of tokens into an expressly-symbolic abstract
 * syntax tree (ESAST) of located expressions.
 *
 * <p>This class implements {@link Iterable} and produces a sequence of
 * {@link LocatedExpr} objects representing the parsed expressions.</p>
 *
 * @see LocatedExpr
 * @see TokenOutput
 * @see BracketsTypes
 *
 * @author Sulphuris
 * @since 4/16/2025 7:50 PM
 */
public final class ExprOutput implements Iterable<LocatedExpr<? extends Expr>> {

	/**
	 * Creates an {@code ExprOutput} by reading from the given input stream.
	 *
	 * @param in the input stream to read from
	 * @return a new {@code ExprOutput}
	 * @see TokenOutput#valueOf(InputStream)
	 */
	public static ExprOutput valueOf(InputStream in) {
		return new ExprOutput(TokenOutput.valueOf(in));
	}

	private final Iterable<Token> tokenOutput;
	private final BracketsTypes bracketsTypes;

	/**
	 * Creates an {@code ExprOutput} from the given token output
	 *
	 * @param tokenOutput the token output to parse
	 */
	public ExprOutput(TokenOutput tokenOutput) {
		this(tokenOutput, tokenOutput.getBracketsTypes());
	}

	/**
	 * Creates an {@code ExprOutput} from the given token iterable and bracket
	 * types.
	 *
	 * @param tokenOutput   the token iterable to parse
	 * @param bracketsTypes the bracket types to recognize
	 */
    private ExprOutput(Iterable<Token> tokenOutput, BracketsTypes bracketsTypes) {
        this.tokenOutput = tokenOutput;
		this.bracketsTypes = bracketsTypes;
	}

	/**
	 * Returns the bracket types recognized by this expression output.
	 *
	 * @return the bracket types
	 */
	BracketsTypes getBracketsTypes() {
		return bracketsTypes;
	}

	/**
	 * Returns an iterator over the parsed expressions.
	 *
	 * @return an iterator
	 */
	// @Override
    public Iterator<LocatedExpr<? extends Expr>> iterator() {
        return new ExprIterator();
    }

	/**
	 * An iterator that parses tokens into located expressions.
	 */
    private class ExprIterator implements Iterator<LocatedExpr<? extends Expr>> {
		private LocatedExpr<? extends Expr> next;
		
		private Token currentToken;
		private final Iterator<Token> tokenIterator;
		
		ExprIterator() {
			this.tokenIterator = tokenOutput.iterator();
			nextToken();
			next = parseExpr(null);
		}
        
        // @Override
        public boolean hasNext() {
            return next != null;
        }
        
        // @Override
        public LocatedExpr<? extends Expr> next() {
			LocatedExpr<? extends Expr> token = next;
            next = parseExpr(null);
            return token;
        }

		// @Override
		public void remove() {
			throw new UnsupportedOperationException();
		}


		private boolean isEOF() {
			return currentToken == null || currentToken.type == TokenType.EOF;
		}
		
		private boolean isMatchingCloseBracket(BracketsType type) {
			if (isEOF() || currentToken.text.length() == 0) return false;
			return currentToken.text.charAt(0) == type.close();
		}
		
		private BracketsType getBracketType() {
			if (currentToken.type == TokenType.OPEN) {
				return bracketsTypes.byOpen(currentToken.text.charAt(0));
			}
			return null;
		}
		
		private void nextToken() {
			while (tokenIterator.hasNext()) {
				currentToken = tokenIterator.next();
				if (currentToken.type != TokenType.COMMENT) {
					return;
				}
			}
			currentToken = null;
		}
		
		private LocatedSymbol parseSymbol() {
			if (isEOF()) return null;
			Token token = currentToken;
			nextToken();
			return Symbol.of(token.location, token.text);
		}
		
		private LocatedExprList parseDList(BracketsType bracketsType) {
			Token startToken = currentToken;
			List<LocatedExpr<? extends Expr>> elements = new ArrayList<LocatedExpr<? extends Expr>>();
			nextToken(); // eat opening bracket
			
			while (!isEOF() && !isMatchingCloseBracket(bracketsType)) {
				LocatedExpr<? extends Expr> next = parseExpr(bracketsType);
				if (next == null) break;
				elements.add(next);
			}
			
			LocatedExprList exprList = ExprList.of(Location.between(startToken.location, currentToken.location), bracketsType, elements);
			
			nextToken(); // eat closing bracket
			return exprList;
		}
		
		private LocatedExpr<? extends Expr> parseExpr(BracketsType brackets) {
			if (isEOF()) return null;
			if (brackets != null && isMatchingCloseBracket(brackets)) return null;
			
			BracketsType bracketType = getBracketType();
			if (bracketType != null) {
				return parseDList(bracketType);
			}

			assert currentToken != null;
			if (currentToken.type == TokenType.CLOSE) {
				throw new IllegalArgumentException("Unexpected bracket: " + currentToken);
			}
			
			return parseSymbol();
		}
    }
}
