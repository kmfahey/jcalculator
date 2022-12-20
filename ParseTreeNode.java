package org.magentatobe.jcalculator;

public class ParseTreeNode {

    private String leftChildStr;
    private ParseTreeNode leftChildNode;
    private Character centerOperator;
    private ParseTreeNode rightChildNode;
    private String rightChildStr;

    public ParseTreeNode(final Character centerOperatorVal, final ParseTreeNode soleChildNodeVal) {
        if (centerOperator == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (soleChildNode == null)  { throw new IllegalArgumentException("argument 'soleChildNodeVal' was null"); }
        leftChildStr = null;
        leftChildNode = null;
        centerOperator = centerOperatorVal;
        rightChildNode = soleChildNodeVal;
        rightChildStr = null;
        assert !(leftChildStr != null && leftChildNode != null) && !(rightChildNode != null && rightChildStr != null);
    }

    public ParseTreeNode(final Character soleOperatorVal, final String soleChildStrVal) {
        if (soleOperator == null) { throw new IllegalArgumentException("argument 'soleOperatorVal' was null"); }
        if (soleChildStr == null) { throw new IllegalArgumentException("argument 'soleChildStrVal' was null"); }
        leftChildStr = null;
        leftChildNode = null;
        centerOperator = soleOperatorVal;
        rightChildNode = null;
        rightChildStr = soleChildStrVal;
        assert !(leftChildStr != null && leftChildNode != null) && !(rightChildNode != null && rightChildStr != null);
    }

    public ParseTreeNode(final String leftChildStrVal, final Character centerOperatorVal,
                         final ParseTreeNode rightChildNode) {
        if (leftChildStr == null)   { throw new IllegalArgumentException("argument 'leftChildStrVal' was null"); }
        if (centerOperator == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildNode == null) { throw new IllegalArgumentException("argument 'rightChildNodeVal' was null"); }
        leftChildStr = leftChildStr;
        leftChildNode = null;
        centerOperator = centerOperator;
        rightChildNode = rightChildNode;
        rightChildStr = null;
        assert !(leftChildStr != null && leftChildNode != null) && !(rightChildNode != null && rightChildStr != null);
    }

    public ParseTreeNode(final ParseTreeNode leftChildNodeVal, final Character centerOperatorVal,
                         final ParseTreeNode rightChildNode) {
        if (leftChildNode == null)  { throw new IllegalArgumentException("argument 'leftChildNodeVal' was null"); }
        if (centerOperator == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildNode == null) { throw new IllegalArgumentException("argument 'rightChildNodeVal' was null"); }
        leftChildStr = null;
        leftChildNode = leftChildNode;
        centerOperator = centerOperator;
        rightChildNode = rightChildNode;
        rightChildStr = null;
        assert !(leftChildStr != null && leftChildNode != null) && !(rightChildNode != null && rightChildStr != null);
    }

    public ParseTreeNode(final String leftChildStrVal, final Character centerOperatorVal, final String rightChildStr) {
        if (leftChildStr == null)   { throw new IllegalArgumentException("argument 'leftChildStrVal' was null"); }
        if (centerOperator == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildStr == null)  { throw new IllegalArgumentException("argument 'rightChildStrVal' was null"); }
        leftChildStr = leftChildStr;
        leftChildNode = null;
        centerOperator = centerOperator;
        rightChildNode = null;
        rightChildStr = rightChildStr;
        assert !(leftChildStr != null && leftChildNode != null) && !(rightChildNode != null && rightChildStr != null);
    }

    public ParseTreeNode(final ParseTreeNode leftChildNodeVal, final Character centerOperatorVal,
                         final String rightChildStr) {
        if (leftChildNode == null)  { throw new IllegalArgumentException("argument 'leftChildNodeVal' was null"); }
        if (centerOperator == null) { throw new IllegalArgumentException("argument 'centerOperatorVal' was null"); }
        if (rightChildStr == null)  { throw new IllegalArgumentException("argument 'rightChildStrVal' was null"); }
        leftChildStr = null;
        leftChildNode = leftChildNode;
        centerOperator = centerOperator;
        rightChildNode = null;
        rightChildStr = rightChildStr;
        assert !(leftChildStr != null && leftChildNode != null) && !(rightChildNode != null && rightChildStr != null);
    }

    public String getLeftChildStr()          { return leftChildStr; }
    public ParseTreeNode getLeftChildNode()  { return leftChildNode; }
    public Character getCenterOperator()     { return centerOperator; }
    public ParseTreeNode getRightChildNode() { return rightChildNode; }
    public String getRightChildStr()         { return rightChildStr; }

    public void setLeftChildStr(String leftChildStrVal)            { leftChildStr = leftChildStrVal; }
    public void setLeftChildNode(ParseTreeNode leftChildNodeVal)   { leftChildNode = leftChildNodeVal; }
    public void setCenterOperator(Character centerOperatorVal)     { centerOperator = centerOperatorVal; }
    public void setRightChildNode(ParseTreeNode rightChildNodeVal) { rightChildNode = rightChildNodeVal; }
    public void setRightChildStr(String rightChildStrVal)          { rightChildStr = rightChildStrVal; }

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
