package com.ydo4ki.esast;

import java.util.*;

/**
 * An expression with an associated location in the source code.
 *
 * @param <E> the type of the underlying expression
 */
public abstract class LocatedExpr<E extends Expr> {
    private final E expr;
    private final Location location;

    // sealed
    LocatedExpr(E expr, Location location) {
        this.expr = expr;
        this.location = location;
        if (this.getClass() != LocatedSymbol.class && this.getClass() != LocatedExprList.class)
            throw new IllegalStateException("Abstract class LocatedExpr is sealed");
    }

    /**
     * Returns the underlying expression.
     *
     * @return the expression
     */
    public E getExpr() {
        return expr;
    }

    /**
     * Returns the location in the source code.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Splits this located expression by the given separator strings.
     *
     * @param separateLines strings to split by
     * @return a collection of located expression parts
     */
    public abstract Collection<? extends LocatedExpr<?>> split(String... separateLines);

    /*
     * Applies one of the given functions depending on the concrete node type.
     *
     * <p>If the current object is a {@link LocatedSymbol}, {@code ifSymbol} is
     * invoked. If the current object is a {@link LocatedExprList},
     * {@code ifList} is invoked.</p>
     *
     * @param <T> the return type
     * @param ifSymbol the function to apply to a {@link LocatedSymbol}
     * @param ifList the function to apply to a {@link LocatedExprList}
     * @see Expr#matched(Function, Function)
     * @return the result of applying the selected function
     */
//    public <T> T matched(Function<LocatedSymbol, T> ifSymbol, Function<LocatedExprList, T> ifList) {
//        return this instanceof LocatedSymbol
//                ? ifSymbol.apply((LocatedSymbol) this)
//                : ifList.apply((LocatedExprList) this);
//    }

    /**
     * Returns a new located expression in which all occurrences of the specified
     * symbol are replaced with the new expression.
     *
     * <p>The replacement is recursive: for a {@link LocatedExprList}, all child
     * elements are traversed.</p>
     *
     * @param symbol the symbol to search for
     * @param newValue the new expression to replace with
     * @see Expr#replace(Symbol, Expr)
     * @return a new located expression with the replacement performed
     */
    public LocatedExpr<? extends Expr> replace(Symbol symbol, LocatedExpr<? extends Expr> newValue) {
        if (this instanceof LocatedSymbol) {
            LocatedSymbol sym = (LocatedSymbol) this;
            if (sym.getValue().equals(symbol.getValue())) return newValue;
            else return sym;
        }
        List<LocatedExpr<? extends Expr>> result = new ArrayList<LocatedExpr<? extends Expr>>();
        for (LocatedExpr<? extends Expr> e : ((LocatedExprList) this).getElements()) {
            LocatedExpr<? extends Expr> replace = e.replace(symbol, newValue);
            result.add(replace);
        }
        return LocatedExprList.of(this.getLocation(), ((LocatedExprList) this).getBracketsType(), result);
    }
}
