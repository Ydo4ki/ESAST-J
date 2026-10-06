package com.ydo4ki.esast;

import java.util.*;

/**
 * A located expression list, i.e. an {@link ExprList} with location information
 * for the list itself and for each of its elements.
 */
public final class LocatedExprList extends LocatedExpr<ExprList> implements Iterable<LocatedExpr<? extends Expr>> {
    private final List<LocatedExpr<? extends Expr>> elements;

    /**
     * Creates a located expression list.
     *
     * @param expr the underlying expression list
     * @param location the location of the list
     * @param elements the located elements
     */
    LocatedExprList(ExprList expr, Location location, List<LocatedExpr<? extends Expr>> elements) {
        super(expr, location);
        this.elements = elements;
    }

    /**
     * Creates a located expression list.
     *
     * @param location the position in the source text
     * @param bracketsType the bracket type
     * @param elements the list elements with locations
     * @return a located expression list
     */
    public static LocatedExprList of(Location location, BracketsType bracketsType, List<LocatedExpr<? extends Expr>> elements) {
List<Expr> result = new ArrayList<Expr>();
for (LocatedExpr<? extends Expr> element : elements) {
Expr expr = element.getExpr();
result.add(expr);
}
ExprList list = ExprList.of(bracketsType, result);
        return new LocatedExprList(list, location, elements);
    }

    /**
     * Splits all elements of the list by the given separators and merges the
     * results into a single {@link LocatedExprList}.
     *
     * @param separateLines separator strings
     * @return a collection containing a single element — the result of
     *         {@link #splitList(String...)}
     */
    // @Override
    public Collection<? extends LocatedExpr<?>> split(String... separateLines) {
        return Collections.singleton(splitList(separateLines));
    }

    /**
     * Splits each element of the list and merges the resulting parts into a new
     * {@link LocatedExprList} with the same bracket type.
     *
     * @param separateLines separator strings
     * @return a new located expression list obtained after splitting
     */
    private LocatedExpr<ExprList> splitList(String... separateLines) {
        List<LocatedExpr<? extends Expr>> list = new ArrayList<LocatedExpr<? extends Expr>>();
        for (LocatedExpr<? extends Expr> e : getElements()) {
            list.addAll(e.split(separateLines));
        }
        return of(getLocation(), getBracketsType(),
                list);
    }

    /**
     * Returns the bracket type that groups this list.
     *
     * @return the bracket type
     */
    public BracketsType getBracketsType() {
        return getExpr().getBracketsType();
    }

    /**
     * Returns an unmodifiable list of located elements.
     *
     * @return the list of located elements
     */
    public List<? extends LocatedExpr<? extends Expr>> getElements() {
        return elements;
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return the number of elements
     */
    public int size() {
        return elements.size();
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index the element index
     * @return the located element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public LocatedExpr<? extends Expr> get(int index) {
        return elements.get(index);
    }

    /**
     * Returns an iterator over the located elements of the list.
     *
     * @return an iterator
     */
    // @Override
    public Iterator<LocatedExpr<? extends Expr>> iterator() {
        return elements.iterator();
    }
}
