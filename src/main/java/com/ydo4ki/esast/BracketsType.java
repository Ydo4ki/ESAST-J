package com.ydo4ki.esast;

/**
 * A pair of opening and closing brackets. It is used by {@link ExprList} to group expressions.
 *
 * <p>For example: {@code ('(', ')')}, {@code ('[', ']')}, {@code ('{', '}')}.</p>
 *
 * @see BracketsTypes
 *
 * @author Sulphuris
 * @since 4/6/2025 8:43 PM
 */
public final class BracketsType {

    private final char open;
    private final char close;

    /**
     * Creates a brackets type with the given opening and closing brackets.<br>
     * Package-private constructor to avoid duplications
     *
     * @param open the opening bracket
     * @param close the closing bracket
     */
    BracketsType(char open, char close) {
        this.open = open;
        this.close = close;
    }

    /**
     * Returns a brackets type with the given opening and closing brackets.
     *
     * @param open the opening bracket
     * @param close the closing bracket
     */
    public static BracketsType of(char open, char close) {
        if (open == BracketsTypes.round.open && close == BracketsTypes.round.close) return BracketsTypes.round;
        if (open == BracketsTypes.square.open && close == BracketsTypes.square.close) return BracketsTypes.square;
        if (open == BracketsTypes.braces.open && close == BracketsTypes.braces.close) return BracketsTypes.braces;
        if (open == BracketsTypes.angle.open && close == BracketsTypes.angle.close) return BracketsTypes.angle;

        return new BracketsType(open, close);
    }

    /**
     * Returns the opening bracket.
     *
     * @return the opening bracket
     */
    public char open() {
        return open;
    }

    /**
     * Returns the closing bracket.
     *
     * @return the closing bracket
     */
    public char close() {
        return close;
    }

    /**
     * Returns a string consisting of the opening and closing brackets.
     *
     * @return the string representation of the bracket type
     */
    // @Override
    public String toString() {
        return open + "" + close;
    }

    /**
     * Compares this brackets type with another object.
     *
     * @param o the object to compare with
     * @return {@code true} if the opening and closing brackets are equal
     */
    // @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BracketsType)) return false;
        BracketsType that = (BracketsType) o;
        return open == that.open && close == that.close;
    }

    /**
     * Returns the hash code of the brackets type.
     *
     * @return the hash code
     */
    // @Override
    public int hashCode() {
        return 31 * (31 + open) + close;
    }
}