import javax.swing.*;

public class ShipPlacementPanel extends JPanel {
    
    public ShipPlacementPanel(BattleshipGUI gui) {
        JLabel title = new JLabel("Ship placement");
        this.add(title);

        JButton doneButton = new JButton("Done");
        doneButton.setActionCommand("done");
        doneButton.addActionListener(gui);
        this.add(doneButton);
    }
}
