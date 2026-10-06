package com.ydo4ki.esast;

import java.util.*;

/**
 * An atomic node of the ESAST.
 *
 * <p>A symbol represents any token that is not a bracket.
 * The symbol value cannot be {@code null}.</p>
 *
 * @see Expr
 * @see ExprList
 *
 * @since 4/7/2025 10:33 PM
 * @author Sulphuris
 */
public final class Symbol extends Expr {
	private final String value;

	/**
	 * Creates a symbol with the given value.
	 *
	 * @param value the symbol value
	 */
	private Symbol(String value) {
		if (value == null)
			throw new NullPointerException();
		this.value = value;
	}

	/**
	 * Creates a symbol with the given value.
	 *
	 * @param value the symbol value; must not be {@code null}
	 * @return a new symbol
	 * @throws NullPointerException if {@code value} is {@code null}
	 */
	public static Symbol of(String value) {
		return new Symbol(value);
	}

	/**
	 * Returns the value that this symbol was created with.
	 *
	 * @return the symbol value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Splits the symbol value into a sequence of symbols using the given
	 * separator strings.
	 *
	 * <p>The splitting is greedy: at each position the longest matching
	 * separator is chosen. Empty separator strings are ignored. <bold>The found
	 * separators are also returned as separate {@link Symbol} instances.</bold></p>
	 *
	 * <p>If no non-empty separator is provided or there are no separators, a collection containing a single symbol with
	 * the original value is returned.</p>
	 *
	 * @param separateLines separator strings
	 * @return a collection of symbols obtained after splitting
	 */
	// @Override
	public Collection<Symbol> split(String... separateLines) {
		String line = value;
		
		int lineLength = line.length();
		int start = 0;
		int current = 0;

        List<String> validSeparators = new ArrayList<String>();
        for (String separateLine : separateLines) {
            if (separateLine.length() != 0) {
                validSeparators.add(separateLine);
            }
        }

        if (validSeparators.isEmpty()) {
			return Collections.singleton(this);
		}
		
		List<Symbol> result = new ArrayList<Symbol>();
		while (current <= lineLength) {
			Symbol foundSep = null;
			int maxLen = 0;
			
			for (String sep : validSeparators) {
				if (line.startsWith(sep, current)) {
					if (sep.length() > maxLen) {
						maxLen = sep.length();
						foundSep = Symbol.of(sep);
					}
				}
			}
			
			if (foundSep != null) {
				if (current > start) {
					result.add(Symbol.of(line.substring(start, current)));
				}
				result.add(foundSep);
				start = current + foundSep.value.length();
				current = start;
			} else {
				current++;
			}
		}
		
		if (start < lineLength) {
			result.add(Symbol.of(line.substring(start)));
		}
		
		return result;
	}

	/**
	 * Returns the symbol value as a string.
	 *
	 * @return the symbol value
	 */
	// @Override
	public String toString() {
		return value;
	}

	/**
	 * Compares this symbol with another object.
	 *
	 * @param o the object to compare with
	 * @return {@code true} if the symbol values are equal, {@code false} otherwise
	 */
	// @Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Symbol symbol = (Symbol) o;
		return value.equals(symbol.value);
	}

	/**
	 * Returns the hash code of this symbol.
	 *
	 * @return the hash code
	 */
	// @Override
	public int hashCode() {
		return value.hashCode();
	}

	/**
	 * Converts this to a located symbol with the given location.
	 *
	 * @param location the position in the source text
	 * @return a located symbol
	 */
	public LocatedSymbol located(Location location) {
		return new LocatedSymbol(this, location);
	}
}
