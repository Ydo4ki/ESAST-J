package com.ydo4ki.esast;

import java.util.*;

/**
 * A set of bracket types.
 *
 * <p>Contains standard bracket types: round, square, and curly braces.
 * Allows looking up a bracket type by opening, closing, or any bracket
 * character.</p>
 *
 * @see BracketsType
 *
 * @author Sulphuris
 * @since 5/31/2025 3:23 PM
 */
public final class BracketsTypes implements Iterable<BracketsType> {

	private final Set<BracketsType> content = new HashSet<BracketsType>();

	/**
	 * Creates a set of bracket types.
	 *
	 * @param types the bracket types
	 */
	public BracketsTypes(BracketsType... types) {
		Collections.addAll(content, types);
	}

	/**
	 * Round brackets: {@code ( )}.
	 */
	public static final BracketsType round = new BracketsType('(',')');

	/**
	 * Square brackets: {@code [ ]}.
	 */
	public static final BracketsType square = new BracketsType('[',']');

	/**
	 * Curly braces: {@code { }}.
	 */
	public static final BracketsType braces = new BracketsType('{','}');

	/**
	 * Angle braces: {@code < >}. These are not used by default, so they are present for deduplication purposes only.
	 */
	static final BracketsType angle = new BracketsType('<','>');

	/**
	 * The standard set of bracket types: round, square, and curly braces.
	 */
	public static final BracketsTypes bracketsTypes = new BracketsTypes(round, square, braces);

	/**
	 * Looks up a bracket type by its opening bracket.
	 *
	 * @param ch the opening bracket character
	 * @return the found bracket type, or {@code null} if not found
	 */
	public BracketsType byOpen(char ch) {
		for (BracketsType value : this) {
			if (ch == value.open()) return value;
		}
		return null;
	}

	/**
	 * Looks up a bracket type by its closing bracket.
	 *
	 * @param ch the closing bracket character
	 * @return the found bracket type, or {@code null} if not found
	 */
	public BracketsType byClose(char ch) {
		for (BracketsType value : this) {
			if (ch == value.close()) return value;
		}
		return null;
	}

	/**
	 * Looks up a bracket type by an opening or closing bracket character.
	 *
	 * @param ch the bracket character
	 * @return the found bracket type, or {@code null} if not found
	 */
	public BracketsType byChar(char ch) {
		for (BracketsType value : this) {
			if (ch == value.open() || ch == value.close()) return value;
		}
		return null;
	}

	/**
	 * Checks whether the character is a bracket of any type in this set.
	 *
	 * @param ch the character to check
	 * @return {@code true} if the character is an opening or closing bracket
	 *         of any type in this set
	 */
	public boolean isBracket(char ch) {
		for (BracketsType value : this) {
			if (ch == value.close() || ch == value.open()) return true;
		}
		return false;
	}

	/**
	 * Returns an iterator over the brackets types the list.
	 *
	 * @return an iterator
	 */
	// @Override
	public Iterator<BracketsType> iterator() {
		return content.iterator();
	}
}
