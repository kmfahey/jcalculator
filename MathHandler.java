package org.magentatobe.jcalculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class MathHandler implements ActionListener {

    public MathHandler() { }

    public void actionPerformed(ActionEvent event) {
        System.out.println(((JButton) event.getSource()).getText());
    }
}
