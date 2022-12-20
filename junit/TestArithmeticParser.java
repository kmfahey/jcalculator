package org.magentatobe.jcalculator.junit;

import junit.framework.TestCase;
import org.magentatobe.jcalculator.ArithmeticParser;
import org.magentatobe.jcalculator.ParseTreeNode;

import java.text.ParseException;

public final class TestArithmeticParser extends TestCase {

    public void testParseExpression1() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = new ArithmeticParser("1+1");
        ParseTreeNode parseTree = arithmeticParser.parseExpression();
        String parseTreeStr = parseTree.toString();
        assertEquals("{ { 1 }, '+', { 1 } }", parseTreeStr);
        assertEquals(2F, parseTree.evaluate());
    }

    public void testParseExpression2() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = new ArithmeticParser("1+2+3+(4+5)");
        ParseTreeNode parseTree = arithmeticParser.parseExpression();
        String parseTreeStr = parseTree.toString();
        assertEquals("{ { { { 1 }, '+', { 2 } }, '+', { 3 } }, '+', { { 4 }, '+', { 5 } } }", parseTreeStr);
        assertEquals(15F, parseTree.evaluate());
    }

    public void testParseExpression3() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = new ArithmeticParser("1+2×3+(4×5+6)");
        ParseTreeNode parseTree = arithmeticParser.parseExpression();
        String parseTreeStr = parseTree.toString();
        assertEquals("{ { { 1 }, '+', { { 2 }, '×', { 3 } } }, '+', { { { 4 }, '×', { 5 } }, '+', { 6 } } }",
                     parseTreeStr);
        assertEquals(33F, parseTree.evaluate());
    }

    public void testParseExpression4() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = new ArithmeticParser("−1+2×3+(4×5−√4)+2^2^2");
        ParseTreeNode parseTree = arithmeticParser.parseExpression();
        String parseTreeStr = parseTree.toString();
        assertEquals("{ { { { '−', { 1 } }, '+', { { 2 }, '×', { 3 } } }, '+', { { { 4 }, '×', { 5 } }, '−', { '√', "
                     + "{ 4 } } } }, '+', { { 2 }, '^', { { 2 }, '^', { 2 } } } }", parseTreeStr);
        assertEquals(39F, parseTree.evaluate());
    }

    public void testInvalidExpression1() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = null;
        try {
            arithmeticParser = new ArithmeticParser("×3+(4×5−√4)+2^2^2");
            fail("ArithmeticParser did not throw exception given invalid input");
        } catch (ParseException exception) {
            assertNull(arithmeticParser);
        }
    }

    public void testInvalidExpression2() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = null;
        try {
            arithmeticParser = new ArithmeticParser("(4×5−√4)+2^2^2+3×");
            fail("ArithmeticParser did not throw exception given invalid input");
        } catch (ParseException exception) {
            assertNull(arithmeticParser);
        }
    }

    public void testInvalidExpression3() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = null;
        try {
            arithmeticParser = new ArithmeticParser("(4×5−4√2)+2^2^2+3");
            fail("ArithmeticParser did not throw exception given invalid input");
        } catch (ParseException exception) {
            assertNull(arithmeticParser);
        }
    }

    public void testInvalidExpression4() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = null;
        try {
            arithmeticParser = new ArithmeticParser("(4×5−√2)(3+2^2^2+3)");
            fail("ArithmeticParser did not throw exception given invalid input");
        } catch (ParseException exception) {
            assertNull(arithmeticParser);
        }
    }

    public void testInvalidExpression5() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = null;
        try {
            arithmeticParser = new ArithmeticParser("(4×+−√2)(3+2^2^2+3)");
            fail("ArithmeticParser did not throw exception given invalid input");
        } catch (ParseException exception) {
            assertNull(arithmeticParser);
        }
    }

    public void testInvalidExpression6() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = null;
        try {
            arithmeticParser = new ArithmeticParser("(4×5+−√2)(3+2^^2+3)");
            fail("ArithmeticParser did not throw exception given invalid input");
        } catch (ParseException exception) {
            assertNull(arithmeticParser);
        }
    }
}
