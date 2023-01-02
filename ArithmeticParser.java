package org.kmfahey.jcalculator;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;
import java.text.ParseException;

/**
 * Implements the shunting-yard algorithm to parse the given arithmetic
 * expression and return a parse tree composed of ParseTreeNode objects. */
public class ArithmeticParser {

    /* A mapping that implements an operator precedence table. The value number
     * is higher the higher the operator's precedence. */
    private static final HashMap<String, Integer> OPERATOR_PRECEDENCE = new HashMap<>() {{
        this.put("^", 3); this.put("√", 3); this.put("-u", 2); this.put("×", 1);
        this.put("÷", 1); this.put("+", 0); this.put("-b", 0);
    }};

    /* A mapping that implements an operator numeracy table; value is 1 if the
     * operator is unary, 2 if the operator is binary. */
    private static final HashMap<String, Integer> OPERATOR_NUMERACY = new HashMap<>() {{
        this.put("√", 1); this.put("+", 2); this.put("^", 2); this.put("×", 2);
        this.put("÷", 2); this.put("-b", 2); this.put("-u", 1);
    }};

    /* A mapping that implements an operator associativity table; the value is
     * true if the operator is left-associative, false if right-associative. */
    private static final HashMap<String, Boolean> OPERATOR_IS_LEFT_ASSOC = new HashMap<>() {{
        this.put("+", true); this.put("×", true); this.put("÷", true); this.put("-u", true);
        this.put("^", false); this.put("-b", true);
    }};

    /* Serves the purpose of a C Union type: an operand stack element may be
     * either a String or a ParseTreeNode, so it's typed StackElement and the
     * object is instanced around either value as appropriate. */
    private static record StackElement(String token, ParseTreeNode node) { }

    private ArithmeticParser() { }

    /**
     * Parses the arithmetic expression this object was instanced around, using
     * Dijkstra's shunting-yard algorithm. Returns a ParseTreeNode object which
     * is the root of the resultant parse tree.
     *
     * @return a ParseTreeNode object which is the root of the generated parse
     *         tree
     */
    public static ParseTreeNode parseExpression(String expression) throws ParseException {
        ArrayList<String> tokensList = tokenizeExpression(expression);
        Stack<String> operatorStack = new Stack<>();
        Stack<StackElement> operandStack = new Stack<>();

        // Pop a token off the tokens list
        while (tokensList.size() > 0) {
            String firstToken = tokensList.remove(0);
            // If the token is an operand, then push onto operand stack.
            if (firstToken.matches("^[0-9.]+$")) {
                operandStack.push(new StackElement(firstToken, null));
            // If the token is an unary prefix operator, then push onto operator stack.
            } else if (OPERATOR_NUMERACY.containsKey(firstToken) && OPERATOR_NUMERACY.get(firstToken) == 1) {
                operatorStack.push(firstToken);
            // If the token is a binary operator, o1, then
            } else if (OPERATOR_NUMERACY.containsKey(firstToken) && OPERATOR_NUMERACY.get(firstToken) == 2) {
                String secondToken;
                boolean firstLeftAssoc;
                int firstPrecedence;
                int secondPrecedence;
                // While there's an operator token, o2, at top of operator stack;
                while (true) {
                    if (operatorStack.empty() || operatorStack.peek().equals("(")) {
                        break;
                    }
                    secondToken = operatorStack.peek();
                    firstLeftAssoc = OPERATOR_IS_LEFT_ASSOC.get(firstToken);
                    firstPrecedence = OPERATOR_PRECEDENCE.get(firstToken);
                    secondPrecedence = OPERATOR_PRECEDENCE.get(secondToken);
                    // And either o1 is left associative and its precedence is
                    // less-than-or-equal-to the precedence of o2, or o1 is
                    // right associative and its precedence is less than the
                    // precedence of o2,
                    if (firstLeftAssoc && !(firstPrecedence <= secondPrecedence)) {
                        break;
                    } else if (!firstLeftAssoc && !(firstPrecedence < secondPrecedence)) {
                        break;
                    }
                    // Reduce an expression from the operator and operand stacks
                    // onto the operand stack.
                    operandStack.push(reduceExpression(operatorStack, operandStack));
                }
                operatorStack.push(firstToken);
            // If the token is a left paren, then push it onto operator stack.
            } else if (firstToken.equals("(")) {
                operatorStack.push(firstToken);
            // If the token is a right paren,
            } else if (firstToken.equals(")")) {
                // Until the token at top of operator stack is a left paren,
                while (!operatorStack.empty() && !operatorStack.peek().equals("(")) {
                    // Reduce an expression from the operator and operand stacks
                    // onto the operand stack.
                    operandStack.push(reduceExpression(operatorStack, operandStack));
                }
                // Then remove the left paren from the operator stack and
                // discard it; and discard the right paren as well.
                operatorStack.pop();
            }
        }

        // When the token list is empty, iteratively reduce expressions onto the
        // operand stack until the operator stack is empty.
        while (!operatorStack.empty()) {
            operandStack.push(reduceExpression(operatorStack, operandStack));
        }

        return operandStack.pop().node();
    }

    
    /* Executes part of the shunting-yard algorithm. Pops an operator off the
     * operator stack; if it's a binary operator, pops two operands off the
     * operand stack, otherwise pops just one. Builds a new ParseTreeNode object
     * from them embedded in a StackElement object and returns the StackElement
     * object. */
    private static StackElement reduceExpression(final Stack<String> operatorStack, final Stack<StackElement> operandStack) {
        String operator = operatorStack.pop();
        if (!OPERATOR_NUMERACY.containsKey(operator)) {
            throw new IllegalStateException("operator '" + operator + "' not in unary/binary table");
        } else if (OPERATOR_NUMERACY.get(operator) == 2) {
            StackElement rightOperand = operandStack.pop();
            StackElement leftOperand = operandStack.pop();
            return instanceNodeInStackElem(leftOperand, operator.charAt(0), rightOperand);
        } else {
            StackElement soleOperand = operandStack.pop();
            return instanceNodeInStackElem(operator.charAt(0), soleOperand);
        }
    }

    /* Parses an arithmetic expression in a String into an ArrayList of tokens.
     * Throws a ParseException if invalid syntax was used.
     *
     * @param  expression the arithmetic expression to parse
     * @return a list of tokens the expression was parsed into
     */
    private static ArrayList<String> tokenizeExpression(final String expression) throws ParseException {
        ArrayList<Character> exprChars = new ArrayList<>(expression.chars()
                                                                   .mapToObj(elem -> (char) elem)
                                                                   .collect(Collectors.toList()));

        int parsingOffset = 0;
        String lastToken = null;
        ArrayList<String> tokensList = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();

        while (exprChars.size() > 0) {
            String thisToken = String.valueOf(exprChars.remove(0));

            /* Checking for syntax errors in the expression being parsed, based
             * on the previous token, the current token, and whether there are
             * characters remaining to parse. */
            checkForSyntaxError(lastToken, thisToken, parsingOffset, exprChars.size() != 0);

            /* Found a numeric character, so keep appending numeric characters
             * to a StringBuilder until a non-numeric character is encountered;
             * append the accumulated numeric token to the tokensList. */
            if (thisToken.matches("^[0-9.]+$")) {
                currentToken.append(thisToken);
                parsingOffset++;
                while (exprChars.size() > 0 && String.valueOf(exprChars.get(0)).matches("^[0-9.]+$")) {
                    currentToken.append(exprChars.remove(0));
                    parsingOffset++;
                }
                tokensList.add(currentToken.toString());
                currentToken = new StringBuilder();
            /* Found a non-numeric character; if it's a minus, distinguish
             * between unary and binary minus and append a signal value to the
             * tokensList; otherwise append the character to the tokensList as
             * normal. */
            } else {
                if (thisToken.equals("-")) {
                    String prevToken = (tokensList.size() == 0) ? null : tokensList.get(tokensList.size() - 1);
                    if (prevToken == null || prevToken.equals("(") || OPERATOR_PRECEDENCE.containsKey(prevToken)) {
                        tokensList.add("-u");   // Distinguishing between unary minus and binary minus. If at expr
                    } else {                    // start, or if prev thisToken was a left paren or an operator, then
                        tokensList.add("-b");   // "-u" is added for unary minus, otherwise "-b" for binary minus.
                    }
                } else {
                    tokensList.add(thisToken);
                }
            }
            /* Tracking the number of characters parsed; used as an argument to
             * the ParseException constructor if needed. */
            parsingOffset++;
            lastToken = thisToken;
        }

        return tokensList;
    }

    /* Checks the token stream for a syntax error by comparsing the current
     * token with the previous token and the number of characters left, throwing
     * an error if the current token can't follow the previous one or can't
     * occur at the beginning or end of the tokens list.
     *
     * @param lastToken              the previous token parsed from the input;
     *                               is null if the first token was just parsed
     * @param thisToken              the token just parsed
     * @param parsingOffset          the number of characters of the expression
     *                               parsed so far, used as an argument to the
     *                               ParseException constructor
     * @param exprCharsRemaining     true if there are still characters left to
     *                               parse, false otherwise
     */
    private static void checkForSyntaxError(String lastToken, String thisToken, int parsingOffset,
                                            boolean exprCharsRemaining) throws ParseException {

        /* If lastToken is null then thisToken is the first token in the stream;
         * if it isn't one of [0-9(-], that's an invalid starting token. */
        if (lastToken == null) {
            if (!(thisToken.matches("^([0-9.]+|[(-])$"))) {
                throw new ParseException("math expression cannot begin with token '" + thisToken + "'",
                                         parsingOffset);
            }
        /* If there's no characters remaining to parse, then thisToken is the
         * last token in the stream; if it isn't one of [0-9.)], that's an
         * invalid ending token. */
        } else if (!exprCharsRemaining && !(thisToken.matches("^([0-9.]+|[)])$"))) {
            throw new ParseException("math expression cannot end with token '" + thisToken + "'", parsingOffset);
        /* If the last token was one of [0-9.], and the current token isn't one
         * of [0-9()+×÷^-], then that's an invalid sequence. */
        } else if (lastToken.matches("^[0-9.]+$") && !(thisToken.matches("^([0-9.]+|[()+×÷^-])$"))) {
            throw new ParseException("in math expression, token '" + thisToken + "' cannot follow token '"
                                     + lastToken + "'", parsingOffset);
        } else {
            switch (lastToken) {
                case "(":
                    /* If the last token was a "(" and the current token isn't
                     * one of [0-9(-], that's an invalid sequence. */
                    if (!(thisToken.matches("^([0-9.]+|[(-])$"))) {
                        throw new ParseException("in math expression, token '" + thisToken + "' cannot "
                                                 + "follow token '" + lastToken + "'", parsingOffset);
                    }
                    break;
                case ")":
                    /* If the last token was a ")" and the current token isn't one
                     * of [(+-×÷^], that's an invalid sequence. */
                    if (!thisToken.matches("^[+-×÷^]$")) {
                        throw new ParseException("in math expression, token '" + thisToken + "' cannot "
                                                 + "follow token '" + lastToken + "'", parsingOffset);
                    }
                    break;
                    /* If the last token is one of [+-×÷√], and the current
                     * token isn't one of [0-9.(√-], that's an invalid
                     * sequence. */
                case "+": case "-": case "×": case "÷": case "√":
                    if (!(thisToken.matches("^([0-9.]+|[(√-])$"))) {
                        throw new ParseException("in math expression, token '" + thisToken + "' cannot "
                                                 + "follow token '" + lastToken + "'", parsingOffset);
                    }
                    break;
                    /* If the last token is "^", and the current token isn't one
                     * of [0-9(-], that's an invalid sequence. */
                case "^":
                    if (!(thisToken.matches("^([0-9.]+|[(-])$"))) {
                        throw new ParseException("in math expression, token '" + thisToken + "' cannot "
                                                 + "follow token '" + lastToken + "'", parsingOffset);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /* Instances and returns a StackElement object with a null token() value and
     * a node() value of a new ParseTreeNode object instanced from the contents
     * of the StackElement object argument and the operator argument.
     *
     * @param operator   a arithmetic operator character
     * @param rightChild a StackElement object with either a String token value
     *                   or a ParseTreeNode node value
     * @return           a StackElement object with a null token and a new
     *                   ParseTreeNode object instanced from the method
     *                   arguments
     */
    private static StackElement instanceNodeInStackElem(final Character operator, final StackElement rightChild) {
        if (rightChild.token() != null) {
            return new StackElement(null, new ParseTreeNode(operator, Float.valueOf(rightChild.token())));
        } else {
            return new StackElement(null, new ParseTreeNode(operator, rightChild.node()));
        }
    }

    /* Instances and returns a StackElement object with a null token() value and
     * a node() value of a new ParseTreeNode object instanced from the contents
     * of the two StackElement object arguments and the operator argument.
     *
     * @param leftChild  a StackElement object with either a String token value
     *                   or a ParseTreeNode node value
     * @param operator   a arithmetic operator character
     * @param rightChild a StackElement object with either a String token value
     *                   or a ParseTreeNode node value
     * @return           a StackElement object with a null token and a new
     *                   ParseTreeNode object instanced from the method
     *                   arguments
     */
    private static StackElement instanceNodeInStackElem(final StackElement leftChild, final Character operator,
                                        final StackElement rightChild) {
        if (leftChild.token() != null && rightChild.token() != null) {
            return new StackElement(null, new ParseTreeNode(Float.valueOf(leftChild.token()), operator,
                                                            Float.valueOf(rightChild.token())));
        } else if (leftChild.node() != null && rightChild.token() != null) {
            return new StackElement(null, new ParseTreeNode(leftChild.node(), operator,
                                                            Float.valueOf(rightChild.token())));
        } else if (leftChild.token() != null && rightChild.node() != null) {
            return new StackElement(null, new ParseTreeNode(Float.valueOf(leftChild.token()), operator,
                                                            rightChild.node()));
        } else {
            return new StackElement(null, new ParseTreeNode(leftChild.node(), operator, rightChild.node()));
        }
    }

}
