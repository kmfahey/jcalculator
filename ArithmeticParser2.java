package org.magentatobe.jcalculator;

import java.util.HashSet;
import java.util.HashMap;
import java.util.StringJoiner;
import java.util.ArrayList;
import java.util.Stack;

public class ArithmeticParser2 {
    private static final HashSet<Character> NUMERIC_CHARS = new HashSet<>() {{
        this.add('1'); this.add('2'); this.add('3'); this.add('4'); this.add('5'); this.add('6');
        this.add('7'); this.add('8'); this.add('9'); this.add('0'); this.add('.');
    }};

    private static final HashMap<String, Integer> operatorPrecedence = new HashMap<>() {{
        this.put("+", 0); this.put("−", 0); this.put("×", 1);
        this.put("÷", 1); this.put("^", 2); this.put("√", 2);
    }};

    private static final HashMap<String, Integer> operatorNumeracy = new HashMap<>() {{
        this.put("+", 2); this.put("×", 2); this.put("÷", 2);
        this.put("^", 2); this.put("√", 1);
    }};

    private static final HashMap<String, Boolean> operatorIsLeftAssoc = new HashMap<>() {{
        this.put("+", true); this.put("×", true); this.put("÷", true);
        this.put("^", false); this.put("−", true);
    }};
    public record ParseTreeNode(String leftChildStr, ParseTreeNode leftChildNode,
                                Character centerOperator,
                                ParseTreeNode rightChildNode, String rightChildStr) {
        public String recursiveToString() {
            StringJoiner strJoin = new StringJoiner(", ", "{ ", " }");
            if (leftChildStr != null)   { strJoin.add("{ " + leftChildStr + " }"); }
            if (leftChildNode != null)  { strJoin.add(leftChildNode.recursiveToString()); }
            if (centerOperator != null) { strJoin.add("'" + centerOperator + "'"); }
            if (rightChildNode != null) { strJoin.add(rightChildNode.recursiveToString()); }
            if (rightChildStr != null)  { strJoin.add("{ " + rightChildStr + " }"); }
            return strJoin.toString();
        }
    }

    public record StackElement(String token, ParseTreeNode node) { }

    private final ArrayList<String> tokensList;
    private final Stack<String> operatorStack;
    private final Stack<StackElement> operandStack;
    private ParseTreeNode parseTreeResult;

    public ArithmeticParser2(final String expression) {
        tokensList = tokenizeExpression(expression);
        operatorStack = new Stack<>();
        operandStack = new Stack<>();
        parseTreeResult = null;
    }

    /* Shunting-Yard Algorithm Pseudocode:
        while tokens to be read
            read token
            if token is operand then push onto operand stack
            if token is unary prefix operator then push onto operator stack
            if token is binary operator, o1, then
                while operator token, o2, at top of operator stack, and
                        either o1 is left associative and its precedence is <= to that of o2
                        or o1 is right associative and its precedence < that of o2
                    reduce expression
                        pop operator off operator stack
                        pop operands off operand stack
                        process expression
                        push result onto operand stack
                push o1 onto the operator stack
            if token is left paren then push it onto operator stack
            if token is right paren
                until token at top of operator stack is left paren
                    reduce expression
                        pop operator off operator stack
                        pop operands off operand stack
                        process expression
                        push result onto operand stack
                if operator stack runs out without finding left paren then mismatched parens
                pop left paren from stack
        when no more tokens to read and
            while still tokens on operator stack
                if operator token on top of stack is paren then mismatched parens
                reduce expression
                    pop operator off operator stack
                    pop operands off operand stack
                    process expression
                    push result onto operand stack
            pop result of operand stack */

    public ParseTreeNode parseExpression() {
        if (parseTreeResult != null) {
            return parseTreeResult;
        }

        boolean prevWasOperatorLeftParenOrStart = true;

        while (tokensList.size() > 0) {
            String firstToken = tokensList.remove(0);
            if (firstToken.matches("^[0-9.]+$")) {
                operandStack.push(new StackElement(firstToken, null));
                prevWasOperatorLeftParenOrStart = false;
            } else if (operatorNumeracy.containsKey(firstToken) && operatorNumeracy.get(firstToken) == 1
                           || firstToken.equals("−") && prevWasOperatorLeftParenOrStart) {
                operatorStack.push(firstToken);
                // FIXME: unary and binary minus need to be distinguished so that in reduceExpression the correct number of operands are popped based on which minus it is
                prevWasOperatorLeftParenOrStart = true;
            } else if (operatorNumeracy.containsKey(firstToken) && operatorNumeracy.get(firstToken) == 2 || firstToken.equals("−")) {
                while (true) {
                    if (operatorStack.empty() || operatorStack.peek().equals("(")) {
                        break;
                    }
                    String secondToken = operatorStack.peek();
                    int firstPrecedence = operatorPrecedence.get(firstToken);
                    if (!operatorPrecedence.containsKey(secondToken)) {
                        throw new IllegalStateException("no precedence weight available for operator '" + secondToken +"'");
                    }
                    int secondPrecedence = operatorPrecedence.get(secondToken);
                    boolean firstLeftAssoc = operatorIsLeftAssoc.get(firstToken);
                    if (firstLeftAssoc ? firstPrecedence > secondPrecedence : firstPrecedence >= secondPrecedence) {
                        break;
                    }
                    operandStack.push(reduceExpression());
                }
                operatorStack.push(firstToken);
                prevWasOperatorLeftParenOrStart = false;
            } else if (firstToken.equals("(")) {
                operatorStack.push(firstToken);
                prevWasOperatorLeftParenOrStart = true;
            } else if (firstToken.equals(")")) {
                while (!operatorStack.empty() && !operatorStack.peek().equals("(")) {
                    operandStack.push(reduceExpression());
                }
                prevWasOperatorLeftParenOrStart = false;
            }
        }

        while (!operatorStack.empty()) {
            operandStack.push(reduceExpression());
        }

        parseTreeResult = operandStack.pop().node();

        return parseTreeResult;
    }

    private StackElement reduceExpression() {
        String operator = operatorStack.pop();
        StackElement rightOperand = operandStack.pop();
        StackElement leftOperand = operandStack.pop();
        return instanceStackElement(leftOperand, operator.charAt(0), rightOperand);
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
                while (exprChars.size() > 0 && NUMERIC_CHARS.contains(exprChars.get(0))) {
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

    private StackElement instanceStackElement(final StackElement leftChild, final Character operator,
                                        final StackElement rightChild) {
        StackElement newStackElement;
        if (leftChild.token() != null && rightChild.token() != null) {
            newStackElement =  new StackElement(null, instanceNode(leftChild.token(), operator, rightChild.token()));
        } else if (leftChild.node() != null && rightChild.token() != null) {
            newStackElement =  new StackElement(null, instanceNode(leftChild.node(), operator, rightChild.token()));
        } else if (leftChild.token() != null && rightChild.node() != null) {
            newStackElement =  new StackElement(null, instanceNode(leftChild.token(), operator, rightChild.node()));
        } else {
            newStackElement =  new StackElement(null, instanceNode(leftChild.node(), operator, rightChild.node()));
        }
        assert (newStackElement.token() == null && newStackElement.node() != null
                || newStackElement.token() != null && newStackElement.node() == null);
        return newStackElement;
    }

    private ParseTreeNode instanceNode(final String leftChildStr, final Character centerOperator,
                                       final ParseTreeNode rightChildNode) {
        assert leftChildStr != null && centerOperator != null && rightChildNode != null;
        return new ParseTreeNode(leftChildStr, null, centerOperator, rightChildNode, null);
    }

    private ParseTreeNode instanceNode(final ParseTreeNode leftChildNode, final Character centerOperator,
                                       final ParseTreeNode rightChildNode) {
        assert leftChildNode != null && centerOperator != null && rightChildNode != null;
        return new ParseTreeNode(null, leftChildNode, centerOperator, rightChildNode, null);
    }

    private ParseTreeNode instanceNode(final String leftChildStr, final Character centerOperator,
                                       final String rightChildStr) {
        assert leftChildStr != null && centerOperator != null && rightChildStr != null;
        return new ParseTreeNode(leftChildStr, null, centerOperator, null, rightChildStr);
    }

    private ParseTreeNode instanceNode(final ParseTreeNode leftChildNode, final Character centerOperator,
                                       final String rightChildStr) {
        assert leftChildNode != null && centerOperator != null && rightChildStr != null;
        return new ParseTreeNode(null, leftChildNode, centerOperator, null, rightChildStr);
    }
}
