import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GamePanel extends JPanel {
    
    private final int NUMCOL = 10;
    private final int NUMROW = 10;
    private final int PIECESIZE = 30;
    private final int BOARDSIZE = PIECESIZE * NUMCOL;
    private JLabel[][] shipSlots = new JLabel[NUMROW][NUMCOL];
    private JLabel[][] shotSlots = new JLabel[NUMROW][NUMCOL];


    public GamePanel(BattleshipGUI gui) {
        this.setLayout(new BorderLayout());
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.X_AXIS));
        
        JPanel shipBoardPanel = new JPanel();
        shipBoardPanel.setLayout(new BorderLayout());

        JLabel shipLabel = new JLabel("Ship Board");
        shipBoardPanel.add(shipLabel, BorderLayout.NORTH);

        JPanel shipBoard = new JPanel();
        shipBoard.setLayout(new GridLayout(NUMROW, NUMCOL));
        shipBoard.setPreferredSize(new Dimension(100, 20));
        shipBoardPanel.add(shipBoard, BorderLayout.CENTER);

        gamePanel.add(shipBoardPanel);

        JPanel shotsBoardPanel = new JPanel();
        shotsBoardPanel.setLayout(new BorderLayout());

        JLabel shotsLabel = new JLabel("Shots Board");
        shotsBoardPanel.add(shotsLabel, BorderLayout.NORTH);

        JPanel shotsBoard = new JPanel();
        shotsBoard.setLayout(new GridLayout(NUMROW, NUMCOL));
        shotsBoard.setPreferredSize(new Dimension(BOARDSIZE, BOARDSIZE));
        shotsBoardPanel.add(shotsBoard);
        gamePanel.add(shotsBoardPanel);
        
        initSlots(shipSlots, shipBoard, gui);
        initSlots(shotSlots, shotsBoard, gui);

        JButton mainMenuButton = new JButton("Main menu");
        mainMenuButton.setActionCommand("mainMenu");
        mainMenuButton.addActionListener(gui);
        this.add(gamePanel, BorderLayout.CENTER);
        this.add(mainMenuButton, BorderLayout.SOUTH);

    }

    public void initSlots(JLabel[][] slots, JPanel panel, MouseListener listener) {
        ImageIcon water = new ImageIcon("images/water.png");
        for (int i = 0; i < NUMROW; i++) {
            for (int j = 0; j < NUMCOL; j++) {
                slots[i][j] = new JLabel();
                slots[i][j].setIcon(water);
                // slots[i][j].setFont(new Font("SansSerif", Font.BOLD, 18));

                slots[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                slots[i][j].addMouseListener(listener);
                panel.add(slots[i][j]);
            }
        }
    }

     /**
     * Returns the column number of where the given JLabel is on
     * 
     * @param label the label whose column number to be requested
     * @return the column number
     */
    public int getRow(JLabel label) {
        int result = -1;
        for (int i = 0; i < NUMROW && result == -1; i++) {
            for (int j = 0; j < NUMCOL && result == -1; j++) {
                if (shotSlots[i][j] == label) {
                    result = i;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Returns the column number of where the given JLabel is on
     * 
     * @param label the label whose column number to be requested
     * @return the column number
     */
    public int getColumn(JLabel label) {
        int result = -1;
        for (int i = 0; i < NUMROW && result == -1; i++) {
            for (int j = 0; j < NUMCOL && result == -1; j++) {
                if (shotSlots[i][j] == label) {
                    result = j;
                }
            }
        }
        return result;
    }
}
