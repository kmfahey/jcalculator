package org.magentatobe.jcalculator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
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
        JButton powerOfTwoButton = new JButton("𝘹ʸ");
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
        calculatorPanel.add(powerOfTwoButton,       buildConstraints(2, 1, 1, 1));
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

        MathHandler mathHandler = new MathHandler(displayTextPane);

        plusOrMinusButton.addActionListener(mathHandler);
        zeroButton.addActionListener(mathHandler);
        decimalPointButton.addActionListener(mathHandler);
        equalsButton.addActionListener(mathHandler);

        oneButton.addActionListener(mathHandler);
        twoButton.addActionListener(mathHandler);
        threeButton.addActionListener(mathHandler);
        plusButton.addActionListener(mathHandler);

        fourButton.addActionListener(mathHandler);
        fiveButton.addActionListener(mathHandler);
        sixButton.addActionListener(mathHandler);
        minusButton.addActionListener(mathHandler);

        sevenButton.addActionListener(mathHandler);
        eightButton.addActionListener(mathHandler);
        nineButton.addActionListener(mathHandler);
        timesButton.addActionListener(mathHandler);

        reciprocalButton.addActionListener(mathHandler);
        powerOfTwoButton.addActionListener(mathHandler);
        squareRootButton.addActionListener(mathHandler);
        divisionButton.addActionListener(mathHandler);

        leftParenthesisButton.addActionListener(mathHandler);
        rightParenthesisButton.addActionListener(mathHandler);
        clearButton.addActionListener(mathHandler);
        backspaceButton.addActionListener(mathHandler);

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
        powerOfTwoButton.setFont(arial24);
        squareRootButton.setFont(arial24);
        divisionButton.setFont(arial24);

        leftParenthesisButton.setFont(arial24);
        rightParenthesisButton.setFont(arial24);
        clearButton.setFont(arial24);
        backspaceButton.setFont(arial24);

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
