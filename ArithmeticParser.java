package org.magentatobe.jcalculator;

import java.util.HashSet;
import java.util.HashMap;
import java.util.StringJoiner;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ArithmeticParser {
    private static final HashSet<Character> NUMERIC_CHARS = new HashSet<>() {{
        this.add('1'); this.add('2'); this.add('3'); this.add('4'); this.add('5'); this.add('6');
        this.add('7'); this.add('8'); this.add('9'); this.add('0'); this.add('.');
    }};

    private static final HashMap<String, Integer> operatorPrecedence = new HashMap<>() {{
        this.put("^", 3); this.put("√", 3); this.put("−u", 2); this.put("×", 1);
        this.put("÷", 1); this.put("+", 0); this.put("−b", 0);
    }};

    private static final HashMap<String, Integer> operatorNumeracy = new HashMap<>() {{
        this.put("√", 1); this.put("+", 2); this.put("^", 2); this.put("×", 2);
        this.put("÷", 2); this.put("−b", 2); this.put("−u", 1);
    }};

    private static final HashMap<String, Boolean> operatorIsLeftAssoc = new HashMap<>() {{
        this.put("+", true); this.put("×", true); this.put("÷", true); this.put("−u", true);
        this.put("^", false); this.put("−b", true);
    }};

    public record StackElement(String token, ParseTreeNode node) { }

    private final ArrayList<String> tokensList;
    private final Stack<String> operatorStack;
    private final Stack<StackElement> operandStack;
    private ParseTreeNode parseTreeResult;

    public ArithmeticParser(final String expression) throws IllegalArgumentException {
        if (!expression.matches("^[0-9.()+−×÷√^]+$")) {
            throw new IllegalArgumentException("unrecognized characters in expression; expression must consist of "
                                               + "only the characters 0123456789.()+−×÷√^");
        }
        tokensList = tokenizeExpression(expression);
        operatorStack = new Stack<>();
        operandStack = new Stack<>();
        parseTreeResult = null;
    }

    public ParseTreeNode parseExpression() {
        if (parseTreeResult != null) {
            return parseTreeResult;
        }

        while (tokensList.size() > 0) {
            String firstToken = tokensList.remove(0);
            // 1. if token is operand then push onto operand stack
            if (firstToken.matches("^[0-9.]+$")) {
                operandStack.push(new StackElement(firstToken, null));
            // 2. if token is unary prefix operator then push onto operator stack
            } else if (operatorNumeracy.containsKey(firstToken) && operatorNumeracy.get(firstToken) == 1) {
                operatorStack.push(firstToken);
            // 3. if token is binary operator, o1, then
            } else if (operatorNumeracy.containsKey(firstToken) && operatorNumeracy.get(firstToken) == 2) {
                // while operator token, o2, at top of operator stack, and
                while (true) {
                    String secondToken;
                    int firstPrecedence;
                    int secondPrecedence;
                    boolean firstLeftAssoc;
                    if (operatorStack.empty() || operatorStack.peek().equals("(")) {
                        break;
                    }
                    secondToken = operatorStack.peek();
                    firstPrecedence = operatorPrecedence.get(firstToken);
                    secondPrecedence = operatorPrecedence.get(secondToken);
                    try {
                        firstLeftAssoc = operatorIsLeftAssoc.get(firstToken);
                    } catch (NullPointerException exception) {
                        System.out.println("token " + firstToken + " not in associativity table");
                        exception.printStackTrace();
                        System.exit(1);
                        return null;
                    }
                    // and either o1 is left associative and its precedence is
                    // <= to that of o2, or o1 is right associative and its
                    // precedence < that of o2
                    if (firstLeftAssoc && !(firstPrecedence <= secondPrecedence)) {
                        break;
                    } else if (!firstLeftAssoc && !(firstPrecedence < secondPrecedence)) {
                        break;
                    }
                    // reduce expression
                    operandStack.push(reduceExpression());
                }
                operatorStack.push(firstToken);
            // 4. if token is left paren then push it onto operator stack
            } else if (firstToken.equals("(")) {
                operatorStack.push(firstToken);
            // 5. if token is right paren
            } else if (firstToken.equals(")")) {
                // until token at top of operator stack is left paren
                while (!operatorStack.empty() && !operatorStack.peek().equals("(")) {
                    // reduce expression
                    operandStack.push(reduceExpression());
                }
                // pop left paren from stack
                operatorStack.pop();
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
        if (!operatorNumeracy.containsKey(operator)) {
            throw new IllegalStateException("operator '" + operator +"' not in unary/binary table");
        } else if (operatorNumeracy.get(operator) == 2) {
            StackElement rightOperand = operandStack.pop();
            StackElement leftOperand = operandStack.pop();
            return instanceStackElement(leftOperand, operator.charAt(0), rightOperand);
        } else {
            StackElement soleOperand = operandStack.pop();
            return instanceStackElement(operator.charAt(0), soleOperand);
        }
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
                String thisToken = String.valueOf(exprChars.remove(0));
                if (thisToken.equals("−")) {
                    String prevToken = (tokensList.size() == 0) ? null : tokensList.get(tokensList.size() - 1);
                    if (prevToken == null || prevToken.equals("(") || operatorPrecedence.containsKey(prevToken)) {
                        tokensList.add("−u");   // Distinguishing between unary minus and binary minus. If at expr 
                    } else {                    // start, or if prev token was a left paren or an operator, then "−u" 
                        tokensList.add("−b");   // is added for unary minus, otherwise "−b" for binary minus.
                    }
                } else {
                    tokensList.add(thisToken);
                }
            }
        }

        return tokensList;
    }

    private StackElement instanceStackElement(final Character operator, final StackElement soleChild) {
        StackElement newStackElement;
        if (soleChild.token() != null) {
            return new StackElement(null, new ParseTreeNode(operator, soleChild.token()));
        } else {
            return new StackElement(null, new ParseTreeNode(operator, soleChild.node()));
        }
    }

    private StackElement instanceStackElement(final StackElement leftChild, final Character operator,
                                        final StackElement rightChild) {
        StackElement newStackElement;
        if (leftChild.token() != null && rightChild.token() != null) {
            return new StackElement(null, new ParseTreeNode(leftChild.token(), operator, rightChild.token()));
        } else if (leftChild.node() != null && rightChild.token() != null) {
            return new StackElement(null, new ParseTreeNode(leftChild.node(), operator, rightChild.token()));
        } else if (leftChild.token() != null && rightChild.node() != null) {
            return new StackElement(null, new ParseTreeNode(leftChild.token(), operator, rightChild.node()));
        } else {
            return new StackElement(null, new ParseTreeNode(leftChild.node(), operator, rightChild.node()));
        }
    }

}
