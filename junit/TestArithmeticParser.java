package org.magentatobe.jcalculator.junit;

import junit.framework.TestCase;

import org.magentatobe.jcalculator.ArithmeticParser;


public final class TestArithmeticParser extends TestCase {

    public void testParseExpression1() {
        ArithmeticParser arithmeticParser = new ArithmeticParser("1+1");
        ArithmeticParser.ParseTreeNode parseTree = arithmeticParser.parseExpression();
        String parseTreeStr = parseTree.recursiveToString();
        assertEquals("{ { 1 }, '+', { 1 } }", parseTreeStr);
    }

    public void testParseExpression2() {
        ArithmeticParser arithmeticParser = new ArithmeticParser("1+2+3+(4+5)");
        ArithmeticParser.ParseTreeNode parseTree = arithmeticParser.parseExpression();
        String parseTreeStr = parseTree.recursiveToString();
        assertEquals("{ { { { 1 }, '+', { 2 } }, '+', { 3 } }, '+', { '(', { { 4 }, '+', { 5 } }, ')' } }", parseTreeStr);
    }

    public void testParseExpression3() {
        ArithmeticParser arithmeticParser = new ArithmeticParser("1+2×3+(4×5+6)");
        ArithmeticParser.ParseTreeNode parseTree = arithmeticParser.parseExpression();
        String parseTreeStr = parseTree.recursiveToString();
        assertEquals("", parseTreeStr);
    }
}

