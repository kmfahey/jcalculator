package org.magentatobe.jcalculator;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ParseTreeNode {

    public static final int TOP_LEVEL = 0;
    public static final int PARENTHESES = 1;
    public static final int EXPON_OR_SQRT = 2;
    public static final int MULT_OR_DIV = 3;
    public static final int ADD_OR_SUBTR = 4;

    public static int nodeType;

    public String expression;

    public ParseTreeNode[] parseTreeSubNodes;

    private static final Pattern balParensRegex = Pattern.compile("(?=\\()(?:(?=.*?\\((?!.*?\\1)(.*\\)(?!.*\\2).*))"
                                                                  + "(?=.*?\\)(?!.*?\\2)(.*)).)+?.*?(?=\\1)[^(]*"
                                                                  + "(?=\\2$)");

    public ParseTreeNode(String expr) {
        this(expr, -1);
    }

    public ParseTreeNode(String expr, int nodeTypeVal) throws IllegalArgumentException {
        if (!balParensRegex.matcher(expr).matches()) {
            throw new IllegalArgumentException("expression '" + expr + "' doesn't have balanced parentheses");
        }

        if (expr.matches("^\\(.*\\)$")) {
            expression = expr.substring(1, expr.length() - 1);
        } else {
            expression = expr;
        }

        if (nodeTypeVal != -1) {
            noteType = nodeTypeVal;
        }

        String[] parenTokens = this.tokenizeByParens(expression);
    }

    private String[] tokenizeByParens(String expression) {
        assert balParensRegex.matcher(expression).matches();
        // Tests for balanced parens. The code can rely on the assumption that
        // the parens balance, and not include any error testing for unbalanced
        // parens.

        ArrayList<String> tokensList = new ArrayList<>();
        String curTokenAccum = "";
        int index = 0;

        while (index < expression.length()) {
            if (expression.charAt(index) == '(') {
                int leftParenCount = 1;
                int rightParenCount = 0;

                if (curTokenAccum.length() > 0) {
                    tokensList.add(curTokenAccum);
                    curTokenAccum = '(';
                }

                while (leftParenCount != rightParenCount) {
                    index++;
                    curTokenAccum += expression.charAt(index);
                    if (expression.charAt(index) == '(') {
                        leftParenCount++;
                    } else if (expression.charAt(index) == ')') {
                        rightParenCount++;
                    }
                }

                curTokenAccum += expression.charAt(index);
                tokensList.add(curTokenAccum);
                curTokenAccum = "";
                index++;
            } else {
                curTokenAccum += expression.charAt(index);
                index++;
            }
        }

        tokensList.add(curTokenAccum);

        return tokensList.toArray();
    }
}
