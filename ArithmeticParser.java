package org.magentatobe.jcalculator;

import java.util.HashSet;
import java.util.ArrayList;

public class ArithmeticParser {

    private static final HashSet<Character> NUMERIC_CHARS = new HashSet<>() {{
        this.add('1'); this.add('2'); this.add('3'); this.add('4'); this.add('5'); this.add('6');
        this.add('7'); this.add('8'); this.add('9'); this.add('0'); this.add('.');
    }};

    private record ParseTreeNode(Character leftOperator,
                                 String leftChildStr, ParseTreeNode leftChildNode,
                                 Character centerOperator,
                                 ParseTreeNode rightChildNode, String rightChildStr,
                                 Character rightOperator) {
        String recursiveToString() {
            StringBuilder strBld = new StringBuilder();
            strBld.append("{");
            if (leftOperator != null)   { strBld.append(leftOperator); }
            if (leftChildStr != null)   { strBld.append(leftChildStr); }
            if (leftChildNode != null)  { strBld.append(leftChildNode.recursiveToString()); }
            if (centerOperator != null) { strBld.append(centerOperator); }
            if (rightChildNode != null) { strBld.append(rightChildNode.recursiveToString()); }
            if (rightChildStr != null)  { strBld.append(rightChildStr); }
            if (rightOperator != null)  { strBld.append(rightOperator); }
            strBld.append("}");
            return strBld.toString();
        }
    }

    private record StackElem(String token, ParseTreeNode node) {
        public Character tokenChar() {
            return this.token().charAt(0);
        }
    }

    private final ArrayList<StackElem> elementsStack;
    private final ArrayList<String> tokensList;
    private ParseTreeNode parseTreeResult;

    public ArithmeticParser(final String expression) {
        elementsStack = new ArrayList<>();
        tokensList = tokenizeExpression(expression);
        parseTreeResult = null;
    }

    public ParseTreeNode parseExpression() {
        if (parseTreeResult != null) {
            return parseTreeResult;
        }

        boolean reducedViaParennedBinaryOperatorRule = false;
        boolean reducedViaBinaryOperatorRule = false;
        boolean reducedViaParennedSquareRootRule = false;
        boolean reducedViaSquareRootRule = false;
        boolean reducedViaParennedRule = false;
        boolean shiftedNewTokenOntoStack = false;

        while (tokensList.size() > 0 && elementsStack.size() > 1) {

            if (!reducedViaParennedBinaryOperatorRule && !reducedViaBinaryOperatorRule
                    && !reducedViaParennedSquareRootRule && !reducedViaSquareRootRule
                    && !reducedViaParennedRule && !shiftedNewTokenOntoStack) {
                throw new IllegalStateException("stack has not reduced to one node, but tokens list is empty and "
                                                + "entire loop executed with no rules applicable-- parsing stalled "
                                                + "(averting infinite loop)");
            } else {
                reducedViaParennedBinaryOperatorRule = false;
                reducedViaBinaryOperatorRule = false;
                reducedViaParennedSquareRootRule = false;
                reducedViaSquareRootRule = false;
                reducedViaParennedRule = false;
                shiftedNewTokenOntoStack = false;
            }

            if (stackPatternMatch("(", "EXPR", "+", "EXPR", ")")
                    || stackPatternMatch("(", "EXPR", "−", "EXPR", ")")
                    || stackPatternMatch("(", "EXPR", "×", "EXPR", ")")
                    || stackPatternMatch("(", "EXPR", "÷", "EXPR", ")")
                    || stackPatternMatch("(", "EXPR", "^", "EXPR", ")")) {
                StackElem rightParen = popStack();
                StackElem rightChild = popStack();
                StackElem operator = popStack();
                StackElem leftChild = popStack();
                StackElem leftParen = popStack();
                pushStack(instanceStackElem(leftParen.tokenChar(), leftChild, operator.tokenChar(), rightChild,
                                            rightParen.tokenChar()));
                reducedViaParennedBinaryOperatorRule = true;
            } else if (stackPatternMatch("EXPR", "+", "EXPR") || stackPatternMatch("EXPR", "−", "EXPR")
                    || stackPatternMatch("EXPR", "×", "EXPR") || stackPatternMatch("EXPR", "÷", "EXPR")
                    || stackPatternMatch("EXPR", "^", "EXPR")) {
                StackElem rightChild = popStack();
                StackElem operator = popStack();
                StackElem leftChild = popStack();
                pushStack(instanceStackElem(rightChild, operator.tokenChar(), leftChild));
                reducedViaBinaryOperatorRule = true;
            } else if (stackPatternMatch("√", "(", "EXPR", ")") || stackPatternMatch("(", "√", "EXPR", ")")) {
                StackElem rightOper = popStack();
                StackElem soleChild = popStack();
                StackElem centerOper = popStack();
                StackElem leftOper = popStack();
                pushStack(instanceStackElem(leftOper.tokenChar(), centerOper.tokenChar(), soleChild,
                                            rightOper.tokenChar()));
                reducedViaParennedSquareRootRule = true;
            } else if (stackPatternMatch("√", "EXPR")) {
                StackElem soleChild = popStack();
                StackElem sqrtOperator = popStack();
                pushStack(instanceStackElem(sqrtOperator.tokenChar(), soleChild));
                reducedViaSquareRootRule = true;
            } else if (stackPatternMatch("(", "EXPR", ")")) {
                StackElem rightParen = popStack();
                StackElem soleChild = popStack();
                StackElem leftParen = popStack();
                pushStack(instanceStackElem(leftParen.tokenChar(), soleChild, rightParen.tokenChar()));
                reducedViaParennedRule = true;
            } else if (tokensList.size() > 0) {
                StackElem newStackElement = new StackElem(tokensList.remove(0), null);
                assert (newStackElement.token() == null && newStackElement.node() != null
                        || newStackElement.token() != null && newStackElement.node() == null);
                pushStack(newStackElement);
                shiftedNewTokenOntoStack = true;
            }
        }

        parseTreeResult = popStack().node();

        return parseTreeResult;
    }

    private StackElem popStack() {
        return elementsStack.remove(elementsStack.size() - 1);
    }

    private void pushStack(final StackElem newElem) {
        elementsStack.add(newElem);
    }

    private boolean stackPatternMatch(final String... tokensPattern) {
        if (tokensPattern.length > elementsStack.size()) {
            // If the pattern has more terms than there are elements in the
            // stack, no match is possible.
            return false;
        } else {
            int patternOffset = tokensList.size() - tokensPattern.length;
            for (int index = patternOffset; index < tokensList.size(); index++) {
                String patternPart = tokensPattern[index];
                StackElem stackElement = elementsStack.get(index);
                if (patternPart.equals("EXPR")) {
                    // EXPR matches any number, or any non-null .node() value. If .token()
                    // is non-null then .node() is null. So if .token() isn't a number
                    // then the pattern doesn't match this term.
                    if (stackElement.token() != null && !stackElement.token().matches("^[0-9.]+$")) {
                        return false;
                    }
                } else {
                    // If it's not an EXPR then it's a single character, and can only match
                    // .token(). If .token() is non-null then .node() is null. So if .token()
                    // doesn't equal the pattern then this part of the pattern doesn't match.
                    if (stackElement.token() != null && !stackElement.token().equals(patternPart)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static ArrayList<String> tokenizeExpression(final String expression) {
        ArrayList<Character> exprChars = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {
            exprChars.add(expression.charAt(index));
        }

        ArrayList<String> tokensList = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();

        while (exprChars.size() > 0) {
            if (NUMERIC_CHARS.contains(exprChars.get(0))) {
                while (NUMERIC_CHARS.contains(exprChars.get(0))) {
                    currentToken.append(exprChars.remove(0));
                }
                tokensList.add(currentToken.toString());
                currentToken = new StringBuilder();
            } else {
                tokensList.add(String.valueOf(exprChars.remove(0)));
            }
        }

        return tokensList;
    }

    private StackElem instanceStackElem(final Character leftParen, final StackElem soleChild,
                                        final Character rightParen) {
        StackElem newStackElement;
        if (soleChild.token() != null) {
            newStackElement = new StackElem(null, instanceNode(leftParen, soleChild.token(), rightParen));
        } else {
            newStackElement = new StackElem(null, instanceNode(leftParen, soleChild.node(), rightParen));
        }
        assert (newStackElement.token() == null && newStackElement.node() != null
                || newStackElement.token() != null && newStackElement.node() == null);
        return newStackElement;
    }

    private StackElem instanceStackElem(final Character sqrtOperator, final StackElem soleChild) {
        StackElem newStackElement;
        if (soleChild.token() != null) {
            newStackElement =  new StackElem(null, instanceNode(sqrtOperator, soleChild.token()));
        } else {
            newStackElement =  new StackElem(null, instanceNode(sqrtOperator, soleChild.node()));
        }
        assert (newStackElement.token() == null && newStackElement.node() != null
                || newStackElement.token() != null && newStackElement.node() == null);
        return newStackElement;
    }

    private StackElem instanceStackElem(final Character leftOperator, final Character centerOperator,
                                        final StackElem soleChild, final Character rightOperator) {
        StackElem newStackElement;
        if (soleChild.token() != null) {
            newStackElement =  new StackElem(null, instanceNode(leftOperator, centerOperator, soleChild.token(), rightOperator));
        } else {
            newStackElement =  new StackElem(null, instanceNode(leftOperator, centerOperator, soleChild.node(), rightOperator));
        }
        assert (newStackElement.token() == null && newStackElement.node() != null
                || newStackElement.token() != null && newStackElement.node() == null);
        return newStackElement;
    }

    private StackElem instanceStackElem(final StackElem leftChild, final Character operator,
                                        final StackElem rightChild) {
        StackElem newStackElement;
        if (leftChild.token() != null && rightChild.token() != null) {
            newStackElement =  new StackElem(null, instanceNode(leftChild.token(), operator, rightChild.token()));
        } else if (leftChild.node() != null && rightChild.token() != null) {
            newStackElement =  new StackElem(null, instanceNode(leftChild.node(), operator, rightChild.token()));
        } else if (leftChild.token() != null && rightChild.node() != null) {
            newStackElement =  new StackElem(null, instanceNode(leftChild.token(), operator, rightChild.node()));
        } else {
            newStackElement =  new StackElem(null, instanceNode(leftChild.node(), operator, rightChild.node()));
        }
        assert (newStackElement.token() == null && newStackElement.node() != null
                || newStackElement.token() != null && newStackElement.node() == null);
        return newStackElement;
    }

    private StackElem instanceStackElem(final Character leftParen, final StackElem leftChild, final Character operator,
                                        final StackElem rightChild, final Character rightParen) {
        StackElem newStackElement;
        if (leftChild.token() != null && rightChild.token() != null) {
            newStackElement =  new StackElem(null, instanceNode(leftParen, leftChild.token(), operator, rightChild.token(),
                                                    rightParen));
        } else if (leftChild.node() != null && rightChild.token() != null) {
            newStackElement =  new StackElem(null, instanceNode(leftParen, leftChild.node(), operator, rightChild.token(),
                                                    rightParen));
        } else if (leftChild.token() != null && rightChild.node() != null) {
            newStackElement =  new StackElem(null, instanceNode(leftParen, leftChild.token(), operator, rightChild.node(),
                                                    rightParen));
        } else {
            newStackElement =  new StackElem(null, instanceNode(leftParen, leftChild.node(), operator, rightChild.node(),
                                                    rightParen));
        }
        assert (newStackElement.token() == null && newStackElement.node() != null
                || newStackElement.token() != null && newStackElement.node() == null);
        return newStackElement;
    }

    private ParseTreeNode instanceNode(final Character leftOperator, final Character centerOperator,
                                       final ParseTreeNode soleChildNode, final Character rightOperator) {
        assert leftOperator != null && centerOperator != null && soleChildNode != null && rightOperator != null;
        return new ParseTreeNode(leftOperator, null, null, centerOperator, soleChildNode, null, rightOperator);
    }

    private ParseTreeNode instanceNode(final Character leftOperator, final Character centerOperator,
                                       final String soleChildStr, final Character rightOperator) {
        assert leftOperator != null && centerOperator != null && soleChildStr != null && rightOperator != null;
        return new ParseTreeNode(leftOperator, null, null, centerOperator, null, soleChildStr, rightOperator);
    }

    private ParseTreeNode instanceNode(final Character leftOperator, final String soleChildStr,
                                       final Character rightOperator) {
        assert leftOperator != null && soleChildStr != null && rightOperator != null;
        return new ParseTreeNode(leftOperator, soleChildStr, null, null, null, null, rightOperator);
    }

    private ParseTreeNode instanceNode(final Character leftOperator, final ParseTreeNode soleChildNode,
                                       final Character rightOperator) {
        assert leftOperator != null && soleChildNode != null && rightOperator != null;
        return new ParseTreeNode(leftOperator, null, soleChildNode, null, null, null, rightOperator);
    }

    private ParseTreeNode instanceNode(final Character soleOperator, final String soleChildStr) {
        assert soleOperator != null && soleChildStr != null;
        return new ParseTreeNode(null, null, null, soleOperator, null, soleChildStr, null);
    }

    private ParseTreeNode instanceNode(final Character soleOperator, final ParseTreeNode soleChildNode) {
        assert soleOperator != null && soleChildNode != null;
        return new ParseTreeNode(null, null, null, soleOperator, soleChildNode, null, null);
    }

    private ParseTreeNode instanceNode(final Character leftOperator, final ParseTreeNode leftChildNode,
                                       final Character centerOperator, final ParseTreeNode rightChildNode,
                                       final Character rightOperator) {
        assert (leftOperator != null && leftChildNode != null && centerOperator != null && rightChildNode != null
                && rightOperator != null);
        return new ParseTreeNode(leftOperator, null, leftChildNode, centerOperator, rightChildNode, null,
                                 rightOperator);
    }

    private ParseTreeNode instanceNode(final Character leftOperator, final String leftChildStr,
                                       final Character centerOperator, final String rightChildStr,
                                       final Character rightOperator) {
        assert (leftOperator != null && leftChildStr != null && centerOperator != null && rightChildStr != null
                && rightOperator != null);
        return new ParseTreeNode(leftOperator, leftChildStr, null, centerOperator, null, rightChildStr, rightOperator);
    }

    private ParseTreeNode instanceNode(final Character leftOperator, final ParseTreeNode leftChildNode,
                                       final Character centerOperator, final String rightChildStr,
                                       final Character rightOperator) {
        assert (leftOperator != null && leftChildNode != null && centerOperator != null && rightChildStr != null
                && rightOperator != null);
        return new ParseTreeNode(leftOperator, null, leftChildNode, centerOperator, null, rightChildStr, rightOperator);
    }

    private ParseTreeNode instanceNode(final Character leftOperator, final String leftChildStr,
                                       final Character centerOperator, final ParseTreeNode rightChildNode,
                                       final Character rightOperator) {
        assert (leftOperator != null && leftChildStr != null && centerOperator != null && rightChildNode != null
                && rightOperator != null);
        return new ParseTreeNode(leftOperator, leftChildStr, null, centerOperator, rightChildNode, null, rightOperator);
    }

    private ParseTreeNode instanceNode(final String leftChildStr, final Character centerOperator,
                                       final ParseTreeNode rightChildNode) {
        assert leftChildStr != null && centerOperator != null && rightChildNode != null;
        return new ParseTreeNode(null, leftChildStr, null, centerOperator, rightChildNode, null, null);
    }

    private ParseTreeNode instanceNode(final ParseTreeNode leftChildNode, final Character centerOperator,
                                       final ParseTreeNode rightChildNode) {
        assert leftChildNode != null && centerOperator != null && rightChildNode != null;
        return new ParseTreeNode(null, null, leftChildNode, centerOperator, rightChildNode, null, null);
    }

    private ParseTreeNode instanceNode(final String leftChildStr, final Character centerOperator,
                                       final String rightChildStr) {
        assert leftChildStr != null && centerOperator != null && rightChildStr != null;
        return new ParseTreeNode(null, leftChildStr, null, centerOperator, null, rightChildStr, null);
    }

    private ParseTreeNode instanceNode(final ParseTreeNode leftChildNode, final Character centerOperator,
                                       final String rightChildStr) {
        assert leftChildNode != null && centerOperator != null && rightChildStr != null;
        return new ParseTreeNode(null, null, leftChildNode, centerOperator, null, rightChildStr, null);
    }
}
