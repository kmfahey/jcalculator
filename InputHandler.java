package org.magentatobe.jcalculator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class InputHandler implements ActionListener {

    private final JTextPane calculatorField;

    public InputHandler(final JTextPane calcField) {
        calculatorField = calcField;
    }

    public void actionPerformed(final ActionEvent event) {
        JButton sourceButton = (JButton) event.getSource();
        String buttonText = sourceButton.getText();
        String fieldText = calculatorField.getText();
        String allButLastChar = fieldText.substring(0, fieldText.length() - 1);
        String newText = fieldText;
        switch (buttonText) {
            case "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> {
                if (fieldText.equals("0")) {
                    newText = buttonText;
                } else if (fieldText.length() == 1) {
                    newText = fieldText + buttonText;
                } else if (fieldText.matches("^.*[^0-9.]0$")) {
                    newText = allButLastChar + buttonText;
                } else {
                    newText = fieldText + buttonText;
                }
            }
            case "." -> {
                if (fieldText.equals("0") || fieldText.matches("^[0-9]+$") || fieldText.matches("^.*[^0-9.][0-9]+$")) {
                    newText = fieldText + buttonText;
                }
            }
            case "+", "−", "×", "÷" -> {
                if (fieldText.matches("^.*[+−×÷^√]$")) {
                    newText = allButLastChar + buttonText;
                } else {
                    newText = fieldText + buttonText;
                }
            }
            case "𝘹ʸ" -> {
                if (fieldText.matches("^.*[+−×÷^√]$")) {
                    newText = fieldText.substring(0, fieldText.length() - 1) + "^";
                } else {
                    newText = fieldText + "^";
                }
            }
            case "√𝘹" -> {
                if (!fieldText.matches("^.*[0-9.√]$")) {
                    newText = fieldText + "√";
                }
            }
            case "(" -> {
                if (fieldText.equals("0")) {
                    newText = buttonText;
                } else if (!fieldText.matches("^.*[0-9.]$")) {
                    newText = fieldText + buttonText;
                }
            }
            case ")" -> {
                if (!fieldText.matches("^.*[+−×÷^√]$")) {
                    int leftParenCount = 0;
                    int rightParenCount = 0;
                    for (int index = 0; index < fieldText.length(); index++) {
                        char charAtIndex = fieldText.charAt(index);
                        if (charAtIndex == '(') {
                            leftParenCount++;
                        } else if (charAtIndex == ')') {
                            rightParenCount++;
                        }
                    }
                    if (leftParenCount > rightParenCount) {
                        newText = fieldText + buttonText;
                    }
                }
            }
            case "C" -> {
                newText = "0";
            }
            case "⌫" -> {
                if (fieldText.length() > 1) {
                    newText = fieldText.substring(0, fieldText.length() - 1);
                } else {
                    newText = "0";
                }
            }
            case "±" -> {
                if (fieldText.matches("^−.*$")) {
                    newText = fieldText.substring(1);
                } else {
                    newText = "−" + fieldText;
                }
            }
            case "1/𝘹" -> {
                if (fieldText.matches("^1÷(.*)$")) {
                    newText = fieldText.substring(2);
                } else {
                    newText = "1÷(" + fieldText + ")";
                }
            }
            case "=" -> {
                try {
                    ArithmeticParser arithmeticParser = new ArithmeticParser(fieldText);
                    float expressionValue = arithmeticParser.parseExpression().evaluate();
                    newText = String.valueOf((Math.floor(expressionValue) == expressionValue)
                                             ? (int) expressionValue
                                             : expressionValue);
                } catch (ParseException | NullPointerException exception) {
                    assert true;
                }
            }
            default -> { }
        }
        calculatorField.setText(newText);
    }
}
