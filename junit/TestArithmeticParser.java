package org.magentatobe.jcalculator.junit;

import junit.framework.TestCase;

import org.magentatobe.jcalculator.ArithmeticParser;
import org.magentatobe.jcalculator.ParseTreeNode;

public final class TestArithmeticParser extends TestCase {

    public void testParseExpression1() {
        ArithmeticParser arithmeticParser = new ArithmeticParser("1+1");
        ParseTreeNode parseTree = arithmeticParser.parseExpression();
        String parseTreeStr = parseTree.toString();
        assertEquals("{ { 1 }, '+', { 1 } }", parseTreeStr);
        assertEquals(2F, parseTree.evaluate());
    }

    public void testParseExpression2() {
        ArithmeticParser arithmeticParser = new ArithmeticParser("1+2+3+(4+5)");
        ParseTreeNode parseTree = arithmeticParser.parseExpression();
        String parseTreeStr = parseTree.toString();
        assertEquals("{ { { { 1 }, '+', { 2 } }, '+', { 3 } }, '+', { { 4 }, '+', { 5 } } }", parseTreeStr);
        assertEquals(15F, parseTree.evaluate());
    }

    public void testParseExpression3() {
        ArithmeticParser arithmeticParser = new ArithmeticParser("1+2×3+(4×5+6)");
        ParseTreeNode parseTree = arithmeticParser.parseExpression();
        String parseTreeStr = parseTree.toString();
        assertEquals("{ { { 1 }, '+', { { 2 }, '×', { 3 } } }, '+', { { { 4 }, '×', { 5 } }, '+', { 6 } } }",
                     parseTreeStr);
        assertEquals(33F, parseTree.evaluate());
    }

    public void testParseExpression4() {
        ArithmeticParser arithmeticParser = new ArithmeticParser("−1+2×3+(4×5−√4)+2^2^2");
        ParseTreeNode parseTree = arithmeticParser.parseExpression();
        String parseTreeStr = parseTree.toString();
        assertEquals("{ { { { '−', { 1 } }, '+', { { 2 }, '×', { 3 } } }, '+', { { { 4 }, '×', { 5 } }, '−', { '√', "
                     + "{ 4 } } } }, '+', { { 2 }, '^', { { 2 }, '^', { 2 } } } }", parseTreeStr);
        assertEquals(39F, parseTree.evaluate());
    }
}
