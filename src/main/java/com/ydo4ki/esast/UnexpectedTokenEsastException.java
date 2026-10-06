package com.ydo4ki.esast;

/**
 * {@link EsastException} representing a situation where unexpected token is encountered.
 */
public class UnexpectedTokenEsastException extends EsastException {
    private final Token token;

    public Token getToken() {
        return token;
    }

    public UnexpectedTokenEsastException(Token token, String msg, Exception e) {
        super(token.getLocation(), token + " (" + msg + ")", e, msg);
        this.token = token;
    }

    public UnexpectedTokenEsastException(Token token, char expected) {
        this(token, "'" + expected + "' expected");
    }

    public UnexpectedTokenEsastException(Token token, String msg) {
        super(token.getLocation(), token + " (" + msg + ")", msg);
        this.token = token;
    }

    public UnexpectedTokenEsastException(Token token) {
        super(token.getLocation(), String.valueOf(token));
        this.token = token;
    }
}
