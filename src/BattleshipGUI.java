import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class BattleshipGUI implements ActionListener, MouseListener {

    private CardLayout cards;
    private Container c;
    private boolean easy = true;
    private final int NUMCOL = 10;
    private final int NUMROW = 10;
    private final int PIECESIZE = 30;
    private JLabel[][] shipSlots;
    private JLabel[][] shotSlots;
    MenuPanel menuPanel;
    GamePanel gamePanel;
    ShipPlacementPanel shipPlacementPanel;

    public static void main(String[] args) {
        new BattleshipGUI();
    }

    public BattleshipGUI() {
        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);
        shipPlacementPanel = new ShipPlacementPanel(this);
        JFrame frame = new JFrame("Battleship");
        frame.setSize(new Dimension(1000, 800));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cards = new CardLayout();
        frame.setLayout(cards);
        c = frame.getContentPane();
        
        frame.add(menuPanel, "menu");
        frame.add(gamePanel, "game");
        frame.add(shipPlacementPanel, "shipPlace");
        
        frame.setVisible(true);

    }

    public static void loadGame(String filename) {
        System.out.println(filename);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String[] difficulties = { "Easy", "Normal" };
        if (e.getActionCommand().equals("exit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("instructions")) {
            JOptionPane.showMessageDialog(null, "In this game of Battleship against a computer, the first to sink all opponent ships is victorious. "
            + "\nThe player can choose between an easy and normal difficulty. In Easy, the AI will " 
            + "\nalways attack random squares on the board. In Normal, once the AI lands a random hit, "
            + "\nit will begin to attack the squares around the hit. To start the game the player will "
            + "\nplace 5 ships of varying sizes on a 10x10 board. An aircraft carrier is 5 squares long, "
            + "\na battleship is 4 squares long, both a submarine and cruiser are 3 squares long and a "
            + "\ndestroyer is 2 squares long. These can be placed horizontally or vertically. Once in play "
            + "\nthe player will choose to attack a square which the game will respond with hit or miss. "
            + "\nThe player and AI will continue to alternate turns until the player or AI's ships have "
            + "\nall been sunk.", "Instructions", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getActionCommand().equals("load")) {
            String filename = JOptionPane.showInputDialog(null, "Enter save name");
            loadGame(filename);
        } else if (e.getActionCommand().equals("new")) {
            var selection = JOptionPane.showOptionDialog(null, "Select one:", "Choose difficulty",
                    0, 3, null, difficulties, difficulties[0]);
            if (selection == 0) {
                easy = true;
            }
            if (selection == 1) {
                easy = false;
            }
            cards.show(c, "shipPlace");
        } else if (e.getActionCommand().equals("mainMenu")) {
            cards.show(c, "menu");
        } else if (e.getActionCommand().equals("done")) {
            cards.show(c, "game");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel label = (JLabel) e.getComponent();
        int row = gamePanel.getRow(label);
        int col = gamePanel.getColumn(label);
        System.out.println(row + " " + col);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
