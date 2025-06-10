import javax.swing.*;
import java.awt.*;

public class ShipPlacementPanel extends JPanel {
    
    public ShipPlacementPanel(BattleshipGUI gui) {
        JLabel title = new JLabel("Ship placement");
        this.add(title);

        JButton doneButton = new JButton("Done");
        doneButton.setActionCommand("done");
        doneButton.addActionListener(gui);
        this.setLayout(new FlowLayout());
        JPanel board = new JPanel(new GridLayout(10, 10));

        Color lightSquare = Color.WHITE;
        Color darkSquare = Color.BLACK;

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JLabel square = new JLabel();
                square.setPreferredSize(new Dimension(40, 40));
                if ((row + col) % 2 == 0) {
                    square.setBackground(new Color(100, 20, 70));
                } else {
                    square.setBackground(new Color(100, 20, 10));
                }
                board.add(square);
            }
        }
        this.add(board);
        this.add(doneButton);
    }
}
