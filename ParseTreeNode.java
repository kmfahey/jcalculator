package org.magentatobe.jcalculator;

import java.util.Arrays;
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

    public ParseTreeNode(String expr) {
        this(expr, -1);
    }

    public ParseTreeNode(String expr, int nodeTypeVal) throws IllegalArgumentException {
        if (!this.hasBalancedParentheses(expr)) {
            throw new IllegalArgumentException("expression '" + expr + "' doesn't have balanced parentheses");
        }

        if (expr.matches("^\\(.*\\)$")) {
            expression = expr.substring(1, expr.length() - 1);
        } else {
            expression = expr;
        }

        if (nodeTypeVal != -1) {
            this.nodeType = nodeTypeVal;
        }

        String[] exprTokens = this.tokenizeByParens(expression);
        exprTokens = this.tokenizeByExpOrSqrt(exprTokens);
        System.out.println(Arrays.toString(exprTokens));
    }

    public static String[] tokenizeByExpOrSqrt(String[] exprTokens) {
        assert !exprTokens[exprTokens.length - 1].matches("[√\\^]$") && !exprTokens[0].matches("^\\^.*$");

        ArrayList<String> tokensList = new ArrayList<>();

        for (int index = 0; index < exprTokens.length; index++) {
            String exprToken = exprTokens[index];

            if (exprToken.matches("^\\(.*\\)$") || exprToken.matches("^[^√\\^]+$")) {
                tokensList.add(exprToken);
            } else {
                String[] exprSubTokens = exprToken.split(
                        "(?=√)" +                                  // matching ahead of a sqrt symbol
                        "|(?<=√[0-9.]+)(?=[^0-9.])" +              // matching the end of a sqrt'd number 
                        "|(?<=^|[^0-9.]+)(?=[0-9.]+\\^[0-9.]+)" +  // matching the start of one number exponent another number
                        "|(?<=[0-9.]+^[0-9.]+)(?=$|[^0-9.]+)" +    // matching the end of one number exponent another number
                        "|(?<=^\\^[0-9.]+)(?=$|[^0-9.]+)" +        // matching the end of string-start exponent-symbol number
                        "|(?<=^|[^0-9.]+)(?=[0-9.]+\\^$)" +        // matching the beginning of number exponent-symbol string-end
                        "|(?=^[√^]$)" +                            // matching the beginning of string-start exponent-symbol or sqrt-symbol string-end
                        "|(?<=^[√^]$)");                           // matching the end of string-start exponent-symbol or sqrt-symbol string-end
                for (String exprSubToken : exprSubTokens) {
                    tokensList.add(exprSubToken);
                }
            }
        }

        return tokensList.toArray(new String[] {});
    }

    public static String[] tokenizeByParens(String expression) {
        assert hasBalancedParentheses(expression);
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
                }

                curTokenAccum = "(";

                while (leftParenCount != rightParenCount) {
                    index++;
                    curTokenAccum += expression.charAt(index);
                    if (expression.charAt(index) == '(') {
                        leftParenCount++;
                    } else if (expression.charAt(index) == ')') {
                        rightParenCount++;
                    }
                }

                tokensList.add(curTokenAccum);
                curTokenAccum = "";
                index++;
            } else {
                curTokenAccum += expression.charAt(index);
                index++;
            }
        }

        if (curTokenAccum.length() != 0) {
            tokensList.add(curTokenAccum);
        }

        return tokensList.toArray(new String[] {});
    }

    public static boolean hasBalancedParentheses(String expression) {
        int leftParenCount = 0;
        int rightParenCount = 0;

        for (int index = 0; index < expression.length(); index++) {
            if (expression.charAt(index) == '(') {
                leftParenCount++;
            } else if (expression.charAt(index) == ')') {
                rightParenCount++;
            }
        }

        return leftParenCount == rightParenCount;
    }
}
