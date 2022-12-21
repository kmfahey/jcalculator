package org.magentatobe.jcalculator;

import static java.lang.Float.NaN;
import java.util.StringJoiner;

/**
 * Implements a node in a parse tree, such that a parse tree can be composed
 * entirely of members of this class. It stores both a left and a right
 * child, and can store a child as either a floating-point value or another
 * ParseTreeNode. It also stores the operator of the expression the node
 * represents; the operator can be either binary, in which case both children
 * are defined, or unary, in which case only the right child is defined.
 */
public class ParseTreeNode {

    /** The left child of the node, if it's a terminal float value. */
    private final Float leftChildFloat;

    /** The left child of the node, if it's another ParseTreeNode. */
    private final ParseTreeNode leftChildNode;

    /** The node's arithmetical operator. */
    private final Character centerOperator;

    /** The right child of the node, if it's another ParseTreeNode. */
    private final ParseTreeNode rightChildNode;

    /** The right child of the node, if it's a terminal float value. */
    private final Float rightChildFloat;

    /**
     * Constructs a ParseTreeNode object with the centerOperator, and
     * rightChildNode instance vars set, and leftChildFloat, leftChildNode, and
     * rightChildFloat set to null.
     *
     * @param centerOperatorVal  the value for the node's operator, as a 
     *                           char
     * @param rightChildFloatVal the value for the node's right child, as a
     *                           terminal float value
     */
    public ParseTreeNode(final Character centerOperatorVal, final ParseTreeNode rightChildNodeVal) {
        if (centerOperatorVal == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildNodeVal == null)  { throw new IllegalArgumentException("argument 'rightChildNodeVal' was null"); }
        leftChildFloat = null;
        leftChildNode = null;
        centerOperator = centerOperatorVal;
        rightChildNode = rightChildNodeVal;
        rightChildFloat = null;
    }

    /**
     * Constructs a ParseTreeNode object with the centerOperator and
     * rightChildFloat instance vars set, and leftChildFloat, leftChildNode, and
     * rightChildNode set to null.
     *
     * @param centerOperatorVal  the value for the node's operator, as a 
     *                           char
     * @param rightChildFloatVal the value for the node's right child, as a
     *                           terminal float value
     */
    public ParseTreeNode(final Character centerOperatorVal, final Float rightChildFloatVal) {
        if (centerOperatorVal == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildFloatVal == null) { throw new IllegalArgumentException("argument 'rightChildFloatVal' was null"); }
        leftChildFloat = null;
        leftChildNode = null;
        centerOperator = centerOperatorVal;
        rightChildNode = null;
        rightChildFloat = rightChildFloatVal;
    }

    /**
     * Constructs a ParseTreeNode object with the leftChildFloat,
     * centerOperator, and rightChildNode instance vars set, and leftChildNode
     * and rightChildFloat set to null.
     *
     * @param leftChildFloatVal the value for the node's left child, as a
     *                          terminal float value
     * @param centerOperatorVal the value for the node's operator, as a
     *                          char
     * @param rightChildNodeVal the value for the node's right child, as a
     *                          ParseTreeNode
     */
    public ParseTreeNode(final Float leftChildFloatVal, final Character centerOperatorVal,
                         final ParseTreeNode rightChildNodeVal) {
        if (leftChildFloatVal == null)   { throw new IllegalArgumentException("argument 'leftChildFloatVal' was null"); }
        if (centerOperatorVal == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildNodeVal == null) { throw new IllegalArgumentException("argument 'rightChildNodeVal' was null"); }
        leftChildFloat = leftChildFloatVal;
        leftChildNode = null;
        centerOperator = centerOperatorVal;
        rightChildNode = rightChildNodeVal;
        rightChildFloat = null;
    }

    /**
     * Constructs a ParseTreeNode object with the leftChildNode, centerOperator,
     * and rightChildNode instance vars set, and leftChildFloat and
     * rightChildFloat set to null.
     *
     * @param leftChildNodeVal  the value for the node's left child, as a
     *                          ParseTreeNode
     * @param centerOperatorVal the value for the node's operator, as a char
     * @param rightChildNodeVal the value for the node's right child, as a
     *                          ParseTreeNode
     */
    public ParseTreeNode(final ParseTreeNode leftChildNodeVal, final Character centerOperatorVal,
                         final ParseTreeNode rightChildNodeVal) {
        if (leftChildNodeVal == null)  { throw new IllegalArgumentException("argument 'leftChildNodeVal' was null"); }
        if (centerOperatorVal == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildNodeVal == null) { throw new IllegalArgumentException("argument 'rightChildNodeVal' was null"); }
        leftChildFloat = null;
        leftChildNode = leftChildNodeVal;
        centerOperator = centerOperatorVal;
        rightChildNode = rightChildNodeVal;
        rightChildFloat = null;
    }

    /**
     * Constructs a ParseTreeNode object with the leftChildFloat,
     * centerOperator, and rightChildFloat instance vars set, and leftChildNode
     * and rightChildNode set to null.
     *
     * @param leftChildFloatVal  the value for the node's left child, as a
     *                           terminal float value
     * @param centerOperatorVal  the value for the node's operator, as a char
     * @param rightChildFloatVal the value for the node's right child, as a
     *                           terminal float value
     */
    public ParseTreeNode(final Float leftChildFloatVal, final Character centerOperatorVal, final Float rightChildFloatVal) {
        if (leftChildFloatVal == null)   { throw new IllegalArgumentException("argument 'leftChildFloatVal' was null"); }
        if (centerOperatorVal == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildFloatVal == null)  { throw new IllegalArgumentException("argument 'rightChildFloatVal' was null"); }
        leftChildFloat = leftChildFloatVal;
        leftChildNode = null;
        centerOperator = centerOperatorVal;
        rightChildNode = null;
        rightChildFloat = rightChildFloatVal;
    }

    /**
     * Constructs a ParseTreeNode object with the leftChildNode, centerOperator,
     * and rightChildFloat instance vars set, and leftChildFloat and
     * rightChildNode set to null.
     *
     * @param leftChildNodeVal   the value for the node's left child, as a
     *                           ParseTreeNode
     * @param centerOperatorVal  the value for the node's operator, as a char
     * @param rightChildFloatVal the value for the node's right child, as a
     *                           terminal float value
     */
    public ParseTreeNode(final ParseTreeNode leftChildNodeVal, final Character centerOperatorVal,
                         final Float rightChildFloatVal) {
        if (leftChildNodeVal == null)  { throw new IllegalArgumentException("argument 'leftChildNodeVal' was null"); }
        if (centerOperatorVal == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildFloatVal == null)  { throw new IllegalArgumentException("argument 'rightChildFloatVal' was null"); }
        leftChildFloat = null;
        leftChildNode = leftChildNodeVal;
        centerOperator = centerOperatorVal;
        rightChildNode = null;
        rightChildFloat = rightChildFloatVal;
    }

    /**
     * Recursively evaluates the parse tree, returning a float value. Each child
     * node has evaluate() called on it in turn and its return value is used in
     * the calculation of this node's value.
     *
     * @return the arithmetic result of executing the calculation of this parse
     *         tree node and all child nodes
     */
    public float evaluate() throws NullPointerException, IllegalStateException {
        // Both leftChildFloat and leftChildNode are null when the centerOperator is unary, such as √
        Float leftChildValue = (leftChildFloat != null) ? leftChildFloat : (leftChildNode != null) ? leftChildNode.evaluate() : null;
        Float rightChildValue = (rightChildFloat != null) ? rightChildFloat : rightChildNode.evaluate();
        switch (centerOperator) {
            case '+':
                return leftChildValue + rightChildValue;
            case '-':
                return (leftChildValue != null) ? leftChildValue - rightChildValue : -rightChildValue;
            case '×':
                return leftChildValue * rightChildValue;
            case '÷':
                if (rightChildValue == 0) {
                    throw new IllegalStateException("divide by zero");
                } else {
                    return leftChildValue / rightChildValue;
                }
            case '^':
                return (float) Math.pow(leftChildValue, rightChildValue);
            case '√':
                return (float) Math.sqrt(rightChildValue);
            default:
                return NaN;
        }
    }

    /**
     * Recursively renders this ParseTreeNode and all child nodes into a
     * one-line representation of the parse tree. Each child node (if any)
     * has toString() called on it in turn and the return value is integrated
     * in-line with the string representation of this node.
     *
     * @return a representation of the parse tree in a string
     */
    public String toString() {
        StringJoiner strJoin = new StringJoiner(", ", "{ ", " }");
        if (leftChildFloat != null) {
            if (Math.floor(leftChildFloat) == leftChildFloat) {
                strJoin.add("{ " + String.valueOf((int) (float) leftChildFloat) + " }");
            } else {
                strJoin.add("{ " + String.valueOf(leftChildFloat) + " }");
            }
        }
        if (leftChildNode != null) {
            strJoin.add(leftChildNode.toString());
        }
        if (centerOperator != null) {
            strJoin.add("'" + centerOperator + "'");
        }
        if (rightChildNode != null) {
            strJoin.add(rightChildNode.toString());
        }
        if (rightChildFloat != null) {
            if (Math.floor(rightChildFloat) == rightChildFloat) {
                strJoin.add("{ " + String.valueOf((int) (float) rightChildFloat) + " }");
            } else {
                strJoin.add("{ " + String.valueOf(rightChildFloat) + " }");
            }
        }
        return strJoin.toString();
    }
}
