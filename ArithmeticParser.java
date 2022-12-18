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
                                 Character rightOperator) { }

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

        while (tokensList.size() > 0 && elementsStack.size() > 1) {
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
            } else if (stackPatternMatch("EXPR", "+", "EXPR") || stackPatternMatch("EXPR", "−", "EXPR")
                    || stackPatternMatch("EXPR", "×", "EXPR") || stackPatternMatch("EXPR", "÷", "EXPR")
                    || stackPatternMatch("EXPR", "^", "EXPR")) {
                StackElem rightChild = popStack();
                StackElem operator = popStack();
                StackElem leftChild = popStack();
                pushStack(instanceStackElem(rightChild, operator.tokenChar(), leftChild));
            } else if (stackPatternMatch("√", "(", "EXPR", ")") || stackPatternMatch("(", "√", "EXPR", ")")) {
                StackElem rightOper = popStack();
                StackElem soleChild = popStack();
                StackElem centerOper = popStack();
                StackElem leftOper = popStack();
                pushStack(instanceStackElem(leftOper.tokenChar(), centerOper.tokenChar(), soleChild,
                                            rightOper.tokenChar()));
            } else if (stackPatternMatch("√", "EXPR")) {
                StackElem soleChild = popStack();
                StackElem sqrtOperator = popStack();
                pushStack(instanceStackElem(sqrtOperator.tokenChar(), soleChild));
            } else if (stackPatternMatch("(", "EXPR", ")")) {
                StackElem rightParen = popStack();
                StackElem soleChild = popStack();
                StackElem leftParen = popStack();
                pushStack(instanceStackElem(leftParen.tokenChar(), soleChild, rightParen.tokenChar()));
            } else if (tokensList.size() > 0) {
                pushStack(new StackElem(tokensList.remove(0), null));
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
            return false;
        } else {
            int patternOffset = tokensList.size() - tokensPattern.length;
            for (int index = patternOffset; index < tokensList.size(); index++) {
                String patternComponent = tokensPattern[index];
                StackElem stackElement = elementsStack.get(index);
                if (stackElement.token() != null) { // && stackElement.node() == null
                    if (patternComponent.equals("EXPR") && !stackElement.token().matches("^[0-9.]+$")) {
                        return false;
                    } else if (!stackElement.token().equals(patternComponent)) {
                        return false;
                    }
                } else if (stackElement.node() != null) {
                    if (!patternComponent.equals("EXPR")) {
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
        if (soleChild.token() != null) {
            return new StackElem(null, instanceNode(leftParen, soleChild.token(), rightParen));
        } else {
            return new StackElem(null, instanceNode(leftParen, soleChild.node(), rightParen));
        }
    }

    private StackElem instanceStackElem(final Character sqrtOperator, final StackElem soleChild) {
        if (soleChild.token() != null) {
            return new StackElem(null, instanceNode(sqrtOperator, soleChild.token()));
        } else {
            return new StackElem(null, instanceNode(sqrtOperator, soleChild.node()));
        }
    }

    private StackElem instanceStackElem(final Character leftOperator, final Character centerOperator,
                                        final StackElem soleChild, final Character rightOperator) {
        if (soleChild.token() != null) {
            return new StackElem(null, instanceNode(leftOperator, centerOperator, soleChild.token(), rightOperator));
        } else {
            return new StackElem(null, instanceNode(leftOperator, centerOperator, soleChild.node(), rightOperator));
        }
    }

    private StackElem instanceStackElem(final StackElem leftChild, final Character operator,
                                        final StackElem rightChild) {
        if (leftChild.token() != null && rightChild.token() != null) {
            return new StackElem(null, instanceNode(leftChild.token(), operator, rightChild.token()));
        } else if (leftChild.node() != null && rightChild.token() != null) {
            return new StackElem(null, instanceNode(leftChild.node(), operator, rightChild.token()));
        } else if (leftChild.token() != null && rightChild.node() != null) {
            return new StackElem(null, instanceNode(leftChild.token(), operator, rightChild.node()));
        } else {
            return new StackElem(null, instanceNode(leftChild.node(), operator, rightChild.node()));
        }
    }

    private StackElem instanceStackElem(final Character leftParen, final StackElem leftChild, final Character operator,
                                        final StackElem rightChild, final Character rightParen) {
        if (leftChild.token() != null && rightChild.token() != null) {
            return new StackElem(null, instanceNode(leftParen, leftChild.token(), operator, rightChild.token(),
                                                    rightParen));
        } else if (leftChild.node() != null && rightChild.token() != null) {
            return new StackElem(null, instanceNode(leftParen, leftChild.node(), operator, rightChild.token(),
                                                    rightParen));
        } else if (leftChild.token() != null && rightChild.node() != null) {
            return new StackElem(null, instanceNode(leftParen, leftChild.token(), operator, rightChild.node(),
                                                    rightParen));
        } else {
            return new StackElem(null, instanceNode(leftParen, leftChild.node(), operator, rightChild.node(),
                                                    rightParen));
        }
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
