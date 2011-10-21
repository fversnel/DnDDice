import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel
import javax.swing.JFrame
import javax.swing.JLabel

class DiceView(val dice: Dice) extends JPanel with ActionListener {

    val diceButton = new JButton(dice.toString)
    diceButton.addActionListener(this)
    //getContentPane().add(diceButton)

    val diceLabel = new JLabel("")
    //getContentPane().add(diceLabel)

    this.setPreferredSize(new Dimension(50, 200));


    override def actionPerformed(actionEvent: ActionEvent) {
        val diceOutcome = dice.rollOnce
        diceLabel.setText(diceOutcome.total.toString)
    }
}

class DnDDiceFrame extends JFrame {
    //this.super("DnDDice");
    this.setLayout(new BorderLayout)
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    this.pack()
    this.setVisible(true)
    this.getContentPane().add(new DiceView(Dice.d20), BorderLayout.NORTH)
}
