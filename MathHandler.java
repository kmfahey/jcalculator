package org.magentatobe.jcalculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

public class MathHandler implements ActionListener {

    JTextPane calculatorField;

    HashSet<String> operatorChars = new HashSet<String>() {{
        this.add("+"); this.add("×"); this.add("÷"); this.add("−");
    }};

    HashSet<String> charsCantFollowThemselves = new HashSet<String>() {{
        this.add("+"); this.add("×"); this.add("÷"); this.add("−"); this.add("."); this.add("^");
    }};

    HashSet<String> numericChars = new HashSet<String>() {{
        this.add("0"); this.add("1"); this.add("2"); this.add("3"); this.add("4");
        this.add("5"); this.add("6"); this.add("7"); this.add("8"); this.add("9");
    }};

    Pattern reciprocalRe = Pattern.compile("^1/\\((.*)\\)$");

    public MathHandler(JTextPane calcField) {
        calculatorField = calcField;
    }

    public void actionPerformed(ActionEvent event) {
        JButton sourceButton = (JButton) event.getSource();
        String buttonText = sourceButton.getText();
        String fieldText = calculatorField.getText();
        String penultimateChar = fieldText.length() >= 2 ? fieldText.substring(fieldText.length() - 2, fieldText.length() - 1) : null;
        String lastChar = fieldText.substring(fieldText.length() - 1, fieldText.length());
        String newText = fieldText;
        switch (buttonText) {
            case "1", "2", "3", "4", "5", "6", "7", "8", "9" -> {
                if (fieldText.equals("0")) {
                    newText = buttonText;
                } else if (lastChar.equals("0") && !numericChars.contains(penultimateChar)) {
                    newText = fieldText.substring(0, fieldText.length() - 1) + buttonText;
                } else {
                    newText = fieldText + buttonText;
                }
            }
            case "0" -> {
                if (!fieldText.equals("0")) {
                    newText = fieldText + buttonText;
                }
            }
            case "." -> {
                int index = fieldText.length() - 1;
                String charAtIndex = fieldText.substring(index, index + 1);
                while (numericChars.contains(charAtIndex)) {
                    index -= 1;
                    if (index == -1) {
                        break;
                    }
                    charAtIndex = fieldText.substring(index, index + 1);
                }
                if (!charAtIndex.equals(".")) {
                    newText = fieldText + buttonText;
                }
            }
            case "+", "−" -> {
                if (!fieldText.equals("0") && !lastChar.equals("×") && !lastChar.equals("÷")) {
                    if (lastChar.equals("+") || lastChar.equals("−" )) {
                        newText = fieldText.substring(0, fieldText.length() - 2) + buttonText;
                    } else {
                        newText = fieldText + buttonText;
                    }
                } else if (fieldText.equals("0")) {
                    newText = buttonText;
                }
            }
            case "×", "÷" -> {
                if (!fieldText.equals("0") && !lastChar.equals("×") && !lastChar.equals("÷") && !lastChar.equals("(")) {
                    newText = fieldText + buttonText;
                }
            }
            case "𝘹ʸ" -> {
                if (!fieldText.equals("0") && !charsCantFollowThemselves.contains(lastChar) && !lastChar.equals("(")) {
                    newText = fieldText + ")^";
                }
            }
            case "√𝘹" -> {
                if (!fieldText.equals("0") && !charsCantFollowThemselves.contains(lastChar) && !lastChar.equals("(")) {
                    newText = "√(" + fieldText + ")";
                }
            }
            case "(" -> {
                newText = fieldText + buttonText;
            }
            case ")" -> {
                if (!charsCantFollowThemselves.contains(lastChar)) {
                    int leftParenCount = 0;
                    int rightParenCount = 0;
                    for (int index = 0; index < fieldText.length(); index++) {
                        String charAtIndex = fieldText.substring(index, index + 1);
                        if (charAtIndex.equals("(")) {
                            leftParenCount++;
                        } else if (charAtIndex.equals(")")) {
                            rightParenCount++;
                        }
                        if (leftParenCount > rightParenCount) {
                            newText = fieldText + buttonText;
                        }
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
                String firstChar = fieldText.substring(0, 1);
                if (fieldText.substring(0, 1).equals("−")) {
                    newText = fieldText.substring(1);
                } else if (numericChars.contains(firstChar) || firstChar.equals("(") || firstChar.equals("√")) {
                    newText = "−" + fieldText;
                }
            }
            case "=" -> {
            }
            case "1/𝘹" -> {
                Matcher reciprocalMatcher = reciprocalRe.matcher(fieldText);
                if (reciprocalMatcher.matches()) {
                    newText = reciprocalMatcher.group(1);
                } else {
                    newText = "1/(" + fieldText + ")";
                }
            }
        }
        calculatorField.setText(newText);
    }
}
