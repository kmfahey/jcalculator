package org.magentatobe.jcalculator;

import static java.lang.Float.NaN;
import java.util.StringJoiner;

public class ParseTreeNode {

    private final String leftChildStr;
    private final ParseTreeNode leftChildNode;
    private final Character centerOperator;
    private final ParseTreeNode rightChildNode;
    private final String rightChildStr;

    public ParseTreeNode(final Character centerOperatorVal, final ParseTreeNode soleChildNodeVal) {
        if (centerOperatorVal == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (soleChildNodeVal == null)  { throw new IllegalArgumentException("argument 'soleChildNodeVal' was null"); }
        leftChildStr = null;
        leftChildNode = null;
        centerOperator = centerOperatorVal;
        rightChildNode = soleChildNodeVal;
        rightChildStr = null;
    }

    public ParseTreeNode(final Character soleOperatorVal, final String soleChildStrVal) {
        if (soleOperatorVal == null) { throw new IllegalArgumentException("argument 'soleOperatorVal' was null"); }
        if (soleChildStrVal == null) { throw new IllegalArgumentException("argument 'soleChildStrVal' was null"); }
        leftChildStr = null;
        leftChildNode = null;
        centerOperator = soleOperatorVal;
        rightChildNode = null;
        rightChildStr = soleChildStrVal;
    }

    public ParseTreeNode(final String leftChildStrVal, final Character centerOperatorVal,
                         final ParseTreeNode rightChildNodeVal) {
        if (leftChildStrVal == null)   { throw new IllegalArgumentException("argument 'leftChildStrVal' was null"); }
        if (centerOperatorVal == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildNodeVal == null) { throw new IllegalArgumentException("argument 'rightChildNodeVal' was null"); }
        leftChildStr = leftChildStrVal;
        leftChildNode = null;
        centerOperator = centerOperatorVal;
        rightChildNode = rightChildNodeVal;
        rightChildStr = null;
    }

    public ParseTreeNode(final ParseTreeNode leftChildNodeVal, final Character centerOperatorVal,
                         final ParseTreeNode rightChildNodeVal) {
        if (leftChildNodeVal == null)  { throw new IllegalArgumentException("argument 'leftChildNodeVal' was null"); }
        if (centerOperatorVal == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildNodeVal == null) { throw new IllegalArgumentException("argument 'rightChildNodeVal' was null"); }
        leftChildStr = null;
        leftChildNode = leftChildNodeVal;
        centerOperator = centerOperatorVal;
        rightChildNode = rightChildNodeVal;
        rightChildStr = null;
    }

    public ParseTreeNode(final String leftChildStrVal, final Character centerOperatorVal, final String rightChildStrVal) {
        if (leftChildStrVal == null)   { throw new IllegalArgumentException("argument 'leftChildStrVal' was null"); }
        if (centerOperatorVal == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildStrVal == null)  { throw new IllegalArgumentException("argument 'rightChildStrVal' was null"); }
        leftChildStr = leftChildStrVal;
        leftChildNode = null;
        centerOperator = centerOperatorVal;
        rightChildNode = null;
        rightChildStr = rightChildStrVal;
    }

    public ParseTreeNode(final ParseTreeNode leftChildNodeVal, final Character centerOperatorVal,
                         final String rightChildStrVal) {
        if (leftChildNodeVal == null)  { throw new IllegalArgumentException("argument 'leftChildNodeVal' was null"); }
        if (centerOperatorVal == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildStrVal == null)  { throw new IllegalArgumentException("argument 'rightChildStrVal' was null"); }
        leftChildStr = null;
        leftChildNode = leftChildNodeVal;
        centerOperator = centerOperatorVal;
        rightChildNode = null;
        rightChildStr = rightChildStrVal;
    }

    public float evaluate() throws NullPointerException {
        Float leftChildValue = (leftChildStr != null) ? Float.valueOf(leftChildStr) : (leftChildNode != null) ? leftChildNode.evaluate() : null;
        Float rightChildValue = (rightChildStr != null) ? Float.parseFloat(rightChildStr) : rightChildNode.evaluate();
        switch (centerOperator) {
            case '+':
                return leftChildValue + rightChildValue;
            case '−':
                return (leftChildValue != null) ? leftChildValue - rightChildValue : -rightChildValue;
            case '×':
                return leftChildValue * rightChildValue;
            case '÷':
                return leftChildValue / rightChildValue;
            case '^':
                return (float) Math.pow(leftChildValue, rightChildValue);
            case '√':
                return (float) Math.sqrt(rightChildValue);
            default:
                return NaN;
        }
    }

    public String toString() {
        StringJoiner strJoin = new StringJoiner(", ", "{ ", " }");
        if (leftChildStr != null)   { strJoin.add("{ " + leftChildStr + " }"); }
        if (leftChildNode != null)  { strJoin.add(leftChildNode.toString()); }
        if (centerOperator != null) { strJoin.add("'" + centerOperator + "'"); }
        if (rightChildNode != null) { strJoin.add(rightChildNode.toString()); }
        if (rightChildStr != null)  { strJoin.add("{ " + rightChildStr + " }"); }
        return strJoin.toString();
    }
}
