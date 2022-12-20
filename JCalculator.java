package org.magentatobe.jcalculator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class JCalculator extends JFrame /* implements ActionListener */ {

    public JCalculator() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();

        Dimension windowDims = new Dimension((int) (screenDims.getWidth() * 0.25), (int) (screenDims.getHeight() * 0.5));
        Dimension buttonDims = new Dimension((int) (windowDims.getWidth() * 0.25), (int) (windowDims.getHeight() / 7D));

        GridBagLayout calculatorLayout = new GridBagLayout();
        JPanel calculatorPanel = new JPanel(calculatorLayout);
        this.setContentPane(calculatorPanel);
        this.setSize(windowDims);

        calculatorLayout.columnWidths = new int[] {(int) buttonDims.getWidth(), (int) buttonDims.getWidth(),
                                                   (int) buttonDims.getWidth(), (int) buttonDims.getWidth()};
        calculatorLayout.rowHeights = new int[] {(int) buttonDims.getHeight(), (int) buttonDims.getHeight(),
                                                 (int) buttonDims.getHeight(), (int) buttonDims.getHeight(),
                                                 (int) buttonDims.getHeight(), (int) buttonDims.getHeight(),
                                                 (int) buttonDims.getHeight()};

        JButton plusOrMinusButton = new JButton("±");
        JButton zeroButton = new JButton("0");
        JButton decimalPointButton = new JButton(".");
        JButton equalsButton = new JButton("=");

        JButton oneButton = new JButton("1");
        JButton twoButton = new JButton("2");
        JButton threeButton = new JButton("3");
        JButton plusButton = new JButton("+");

        JButton fourButton = new JButton("4");
        JButton fiveButton = new JButton("5");
        JButton sixButton = new JButton("6");
        JButton minusButton = new JButton("−");

        JButton sevenButton = new JButton("7");
        JButton eightButton = new JButton("8");
        JButton nineButton = new JButton("9");
        JButton timesButton = new JButton("×");

        JButton reciprocalButton = new JButton("1/𝘹");
        JButton exponentiationButton = new JButton("𝘹ʸ");
        JButton squareRootButton = new JButton("√𝘹");
        JButton divisionButton = new JButton("÷");

        JButton leftParenthesisButton = new JButton("(");
        JButton rightParenthesisButton = new JButton(")");
        JButton clearButton = new JButton("C");
        JButton backspaceButton = new JButton("⌫");

        JTextPane displayTextPane = new JTextPane();
        displayTextPane.setEditable(false);
        displayTextPane.setText("0");

        SimpleAttributeSet attribs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setFontFamily(attribs, "Arial");
        StyleConstants.setFontSize(attribs, 36);
        displayTextPane.setParagraphAttributes(attribs, true);

        calculatorPanel.add(displayTextPane, buildConstraints(0, 0, 1, 4));

        calculatorPanel.add(leftParenthesisButton,  buildConstraints(1, 0, 1, 1));
        calculatorPanel.add(rightParenthesisButton, buildConstraints(1, 1, 1, 1));
        calculatorPanel.add(clearButton,            buildConstraints(1, 2, 1, 1));
        calculatorPanel.add(backspaceButton,        buildConstraints(1, 3, 1, 1));

        calculatorPanel.add(reciprocalButton,       buildConstraints(2, 0, 1, 1));
        calculatorPanel.add(exponentiationButton,       buildConstraints(2, 1, 1, 1));
        calculatorPanel.add(squareRootButton,       buildConstraints(2, 2, 1, 1));
        calculatorPanel.add(divisionButton,         buildConstraints(2, 3, 1, 1));

        calculatorPanel.add(sevenButton,            buildConstraints(3, 0, 1, 1));
        calculatorPanel.add(eightButton,            buildConstraints(3, 1, 1, 1));
        calculatorPanel.add(nineButton,             buildConstraints(3, 2, 1, 1));
        calculatorPanel.add(timesButton,            buildConstraints(3, 3, 1, 1));

        calculatorPanel.add(fourButton,             buildConstraints(4, 0, 1, 1));
        calculatorPanel.add(fiveButton,             buildConstraints(4, 1, 1, 1));
        calculatorPanel.add(sixButton,              buildConstraints(4, 2, 1, 1));
        calculatorPanel.add(minusButton,            buildConstraints(4, 3, 1, 1));

        calculatorPanel.add(oneButton,              buildConstraints(5, 0, 1, 1));
        calculatorPanel.add(twoButton,              buildConstraints(5, 1, 1, 1));
        calculatorPanel.add(threeButton,            buildConstraints(5, 2, 1, 1));
        calculatorPanel.add(plusButton,             buildConstraints(5, 3, 1, 1));

        calculatorPanel.add(plusOrMinusButton,      buildConstraints(6, 0, 1, 1));
        calculatorPanel.add(zeroButton,             buildConstraints(6, 1, 1, 1));
        calculatorPanel.add(decimalPointButton,     buildConstraints(6, 2, 1, 1));
        calculatorPanel.add(equalsButton,           buildConstraints(6, 3, 1, 1));

        InputHandler inputHandler = new InputHandler(displayTextPane);

        plusOrMinusButton.addActionListener(inputHandler);
        zeroButton.addActionListener(inputHandler);
        decimalPointButton.addActionListener(inputHandler);
        equalsButton.addActionListener(inputHandler);

        oneButton.addActionListener(inputHandler);
        twoButton.addActionListener(inputHandler);
        threeButton.addActionListener(inputHandler);
        plusButton.addActionListener(inputHandler);

        fourButton.addActionListener(inputHandler);
        fiveButton.addActionListener(inputHandler);
        sixButton.addActionListener(inputHandler);
        minusButton.addActionListener(inputHandler);

        sevenButton.addActionListener(inputHandler);
        eightButton.addActionListener(inputHandler);
        nineButton.addActionListener(inputHandler);
        timesButton.addActionListener(inputHandler);

        reciprocalButton.addActionListener(inputHandler);
        exponentiationButton.addActionListener(inputHandler);
        squareRootButton.addActionListener(inputHandler);
        divisionButton.addActionListener(inputHandler);

        leftParenthesisButton.addActionListener(inputHandler);
        rightParenthesisButton.addActionListener(inputHandler);
        clearButton.addActionListener(inputHandler);
        backspaceButton.addActionListener(inputHandler);

        Font arial24 = new Font("Arial", Font.PLAIN, 24);

        plusOrMinusButton.setFont(arial24);
        zeroButton.setFont(arial24);
        decimalPointButton.setFont(arial24);
        equalsButton.setFont(arial24);

        oneButton.setFont(arial24);
        twoButton.setFont(arial24);
        threeButton.setFont(arial24);
        plusButton.setFont(arial24);

        fourButton.setFont(arial24);
        fiveButton.setFont(arial24);
        sixButton.setFont(arial24);
        minusButton.setFont(arial24);

        sevenButton.setFont(arial24);
        eightButton.setFont(arial24);
        nineButton.setFont(arial24);
        timesButton.setFont(arial24);

        reciprocalButton.setFont(arial24);
        exponentiationButton.setFont(arial24);
        squareRootButton.setFont(arial24);
        divisionButton.setFont(arial24);

        leftParenthesisButton.setFont(arial24);
        rightParenthesisButton.setFont(arial24);
        clearButton.setFont(arial24);
        backspaceButton.setFont(arial24);

        JRootPane rootPane = getRootPane();

        /* rootPane.getInputMap().put(KeyStroke.getKeyStroke(""), "clickPlusOrMinusButton");
        rootPane.getActionMap().put("clickPlusOrMinusButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { plusOrMinusButton.doClick(); }
        }); */

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('0'), "clickZeroButton");
        rootPane.getActionMap().put("clickZeroButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { zeroButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('.'), "clickDecimalPointButton"); // missing
        rootPane.getActionMap().put("clickDecimalPointButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { decimalPointButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('='), "clickEqualsButton"); // missing
        rootPane.getActionMap().put("clickEqualsButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { equalsButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('1'), "clickOneButton");
        rootPane.getActionMap().put("clickOneButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { oneButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('2'), "clickTwoButton");
        rootPane.getActionMap().put("clickTwoButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { twoButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('3'), "clickThreeButton");
        rootPane.getActionMap().put("clickThreeButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { threeButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('+'), "clickPlusButton"); // missing
        rootPane.getActionMap().put("clickPlusButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { plusButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('4'), "clickFourButton");
        rootPane.getActionMap().put("clickFourButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { fourButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('5'), "clickFiveButton");
        rootPane.getActionMap().put("clickFiveButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { fiveButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('6'), "clickSixButton");
        rootPane.getActionMap().put("clickSixButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { sixButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('-'), "clickMinusButton");// missing
        rootPane.getActionMap().put("clickMinusButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { minusButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('7'), "clickSevenButton");
        rootPane.getActionMap().put("clickSevenButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { sevenButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('8'), "clickEightButton");
        rootPane.getActionMap().put("clickEightButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { eightButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('9'), "clickNineButton");
        rootPane.getActionMap().put("clickNineButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { nineButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('*'), "clickTimesButton");// missing
        rootPane.getActionMap().put("clickTimesButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { timesButton.doClick(); }
        });

        /* rootPane.getInputMap().put(KeyStroke.getKeyStroke(""), "clickReciprocalButton");
        rootPane.getActionMap().put("clickReciprocalButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { reciprocalButton.doClick(); }
        }); */

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('^'), "clickExponentiationButton");// missing
        rootPane.getActionMap().put("clickExponentiationButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { exponentiationButton.doClick(); }
        });

        /* rootPane.getInputMap().put(KeyStroke.getKeyStroke(""), "clickSquareRootButton");
        rootPane.getActionMap().put("clickSquareRootButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { squareRootButton.doClick(); }
        }); */

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('/'), "clickDivisionButton");// missing
        rootPane.getActionMap().put("clickDivisionButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { divisionButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke('('), "clickLeftParenthesisButton");// missing
        rootPane.getActionMap().put("clickLeftParenthesisButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { leftParenthesisButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke(')'), "clickRightParenthesisButton");// missing
        rootPane.getActionMap().put("clickRightParenthesisButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { rightParenthesisButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "clickClearButton");
        rootPane.getActionMap().put("clickClearButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { clearButton.doClick(); }
        });

        rootPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "clickBackspaceButton");
        rootPane.getActionMap().put("clickBackspaceButton", new AbstractAction() {
            public void actionPerformed(ActionEvent event) { backspaceButton.doClick(); }
        });

        this.validate();
        this.pack();
    }

    private GridBagConstraints buildConstraints(final int row, final int col, final int rowspan, final int colspan) {
        GridBagConstraints elementConstraints = new GridBagConstraints();
        elementConstraints.fill = GridBagConstraints.BOTH;
        elementConstraints.gridy = row;
        elementConstraints.gridx = col;
        elementConstraints.gridheight = rowspan;
        elementConstraints.gridwidth = colspan;
        elementConstraints.insets = new Insets(5, 5, 5, 5);
        return elementConstraints;
    }

    public static void main(final String[] args) {
        JCalculator jcalc = new JCalculator();
        jcalc.setVisible(true);
        jcalc.setLocationRelativeTo(null);
    }
}
