package org.magentatobe.jcalculator;

import java.util.ArrayList;

public class ParseTreeNode {

    public static final int PARENS = 0;
    public static final int EXPON = 1;
    public static final int MULT_OR_DIV = 2;
    public static final int ADD_OR_SUBTR = 3;

    public static int nodeType;

    public String expression;

    public ParseTreeNode[] parseTreeSubNodes;

    public ParseTreeNode(String expr) {
        this(expr, -1);
    }

    public ParseTreeNode(String expr, int nodeTypeVal) {
        expression = expr;

        if (nodeTypeVal != -1) {
            noteType = nodeTypeVal;
        }

        ArrayList<ParseTreeNode> tempNodeList = new ArrayList<>();

        int leftParenCount = 0;
        int rightParenCount = 0;
        int lastSubstrEndpoint = 0;

        for (int index = 0; index < expression.length(); index++) {
            if (expression.charAt(index) == '(') {
                if (leftParenCount == rightParenCount) {
                    tempNodeList.add(expression.substring(lastSubstrEndpoint, index));
                    lastSubstrEndpoint = index;
                }
                leftParenCount++;
            } else if (expression.charAt(index) == ')') {
                if (leftParenCount == rightParenCount) {
                    tempNodeList.add(expression.substring(lastSubstrEndpoint, index));
                    lastSubstrEndpoint = index;
                }
                rightParenCount++;
            }
        }
    }
}
