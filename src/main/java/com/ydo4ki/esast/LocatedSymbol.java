package com.ydo4ki.esast;

import java.util.*;

/**
 * A located symbol, i.e. a {@link Symbol} with location information.
 */
public final class LocatedSymbol extends LocatedExpr<Symbol> {

    /**
     * Creates a located symbol.
     *
     * @param expr the underlying symbol
     * @param location the location of the symbol
     */
    LocatedSymbol(Symbol expr, Location location) {
        super(expr, location);
    }

    /**
     * Creates a located symbol with the given value.
     *
     * @param location the position in the source text
     * @param value the symbol value
     * @return a located symbol
     */
    public static LocatedSymbol of(Location location, String value) {
        return Symbol.of(value).located(location);
    }

    /**
     * Splits the symbol value into a sequence of located symbols using the given
     * separator strings.
     *
     * <p>The splitting is greedy: at each position the longest matching
     * separator is chosen. Empty separator strings are ignored. The found
     * separators are also returned as separate {@link LocatedSymbol} instances.
     * The locations of the resulting symbols are computed relative to the
     * location of this symbol.</p>
     *
     * <p>If no non-empty separator is provided or there are no separators, a collection containing a single located symbol
     * with the original value is returned.</p>
     *
     * @param separateLines separator strings
     * @return a collection of located symbols obtained after splitting
     */
    // @Override
    public Collection<? extends LocatedExpr<?>> split(String... separateLines) {
        String line = getExpr().getValue();

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
            return Collections.singletonList(this);
        }

        List<LocatedExpr<Symbol>> result = new ArrayList<LocatedExpr<Symbol>>();
        while (current <= lineLength) {
            LocatedExpr<Symbol> foundSep = null;
            int maxLen = 0;

            for (String sep : validSeparators) {
                if (line.startsWith(sep, current)) {
                    if (sep.length() > maxLen) {
                        maxLen = sep.length();
                        foundSep = of(new Location(
                                getLocation().getStartPos() + start,
                                getLocation().getStartPos() + current,
                                getLocation().getStartLine(),
                                getLocation().getEndLine(),
                                getLocation().getSourceFile()
                        ), sep);
                    }
                }
            }

            if (foundSep != null) {
                if (current > start) {
                    result.add(of(new Location(
                                    getLocation().getStartPos() + start,
                                    getLocation().getStartPos() + current,
                                    getLocation().getStartLine(),
                                    getLocation().getEndLine(),
                                    getLocation().getSourceFile()
                            ), line.substring(start, current))
                    );
                }
                result.add(foundSep);
                start = current + foundSep.getExpr().getValue().length();
                current = start;
            } else {
                current++;
            }
        }

        if (start < lineLength) {
            result.add(of(new Location(
                    getLocation().getStartPos() + start,
                    getLocation().getStartPos() + (line.length() - start),
                    getLocation().getStartLine(),
                    getLocation().getEndLine(),
                    getLocation().getSourceFile()
            ), line.substring(start)));
        }

        return result;
    }

    /**
     * Returns the value of the underlying symbol.
     *
     * @return the symbol value
     */
    public String getValue() {
        return getExpr().getValue();
    }
}
