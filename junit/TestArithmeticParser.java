package org.kmfahey.jcalculator.junit;

import junit.framework.TestCase;
import org.kmfahey.jcalculator.ArithmeticParser;
import org.kmfahey.jcalculator.ParseTreeNode;

import java.text.ParseException;

public final class TestArithmeticParser extends TestCase {

    public void testParseExpression1() throws ParseException, IllegalArgumentException {
        ParseTreeNode parseTree = ArithmeticParser.parseExpression("1+1");
        String parseTreeStr = parseTree.toString();
        assertEquals("{ { 1 }, '+', { 1 } }", parseTreeStr);
        assertEquals(2F, parseTree.evaluate());
    }

    public void testParseExpression2() throws ParseException, IllegalArgumentException {
        ParseTreeNode parseTree = ArithmeticParser.parseExpression("1+2+3+(4+5)");
        String parseTreeStr = parseTree.toString();
        assertEquals("{ { { { 1 }, '+', { 2 } }, '+', { 3 } }, '+', { { 4 }, '+', { 5 } } }", parseTreeStr);
        assertEquals(15F, parseTree.evaluate());
    }

    public void testParseExpression3() throws ParseException, IllegalArgumentException {
        ParseTreeNode parseTree = ArithmeticParser.parseExpression("1+2×3+(4×5+6)");
        String parseTreeStr = parseTree.toString();
        assertEquals("{ { { 1 }, '+', { { 2 }, '×', { 3 } } }, '+', { { { 4 }, '×', { 5 } }, '+', { 6 } } }",
                     parseTreeStr);
        assertEquals(33F, parseTree.evaluate());
    }

    public void testParseExpression4() throws ParseException, IllegalArgumentException {
        ParseTreeNode parseTree = ArithmeticParser.parseExpression("-1+2×3+(4×5-√4)+2^2^2");
        String parseTreeStr = parseTree.toString();
        assertEquals("{ { { { '-', { 1 } }, '+', { { 2 }, '×', { 3 } } }, '+', { { { 4 }, '×', { 5 } }, '-', { '√', "
                     + "{ 4 } } } }, '+', { { 2 }, '^', { { 2 }, '^', { 2 } } } }", parseTreeStr);
        assertEquals(39F, parseTree.evaluate());
    }

    public void testInvalidExpression1() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = null;
        try {
            ArithmeticParser.parseExpression("×3+(4×5-√4)+2^2^2");
            fail("ArithmeticParser did not throw exception given invalid input");
        } catch (ParseException exception) {
            assertTrue(true);
        }
    }

    public void testInvalidExpression2() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = null;
        try {
            ArithmeticParser.parseExpression("(4×5-√4)+2^2^2+3×");
            fail("ArithmeticParser did not throw exception given invalid input");
        } catch (ParseException exception) {
            assertTrue(true);
        }
    }

    public void testInvalidExpression3() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = null;
        try {
            ArithmeticParser.parseExpression("(4×5-4√2)+2^2^2+3");
            fail("ArithmeticParser did not throw exception given invalid input");
        } catch (ParseException exception) {
            assertTrue(true);
        }
    }

    public void testInvalidExpression4() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = null;
        try {
            ArithmeticParser.parseExpression("(4×5-√2)(3+2^2^2+3)");
            fail("ArithmeticParser did not throw exception given invalid input");
        } catch (ParseException exception) {
            assertTrue(true);
        }
    }

    public void testInvalidExpression5() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = null;
        try {
            ArithmeticParser.parseExpression("(4×+-√2)(3+2^2^2+3)");
            fail("ArithmeticParser did not throw exception given invalid input");
        } catch (ParseException exception) {
            assertTrue(true);
        }
    }

    public void testInvalidExpression6() throws ParseException, IllegalArgumentException {
        ArithmeticParser arithmeticParser = null;
        try {
            ArithmeticParser.parseExpression("(4×5+-√2)(3+2^^2+3)");
            fail("ArithmeticParser did not throw exception given invalid input");
        } catch (ParseException exception) {
            assertTrue(true);
        }
    }
}
