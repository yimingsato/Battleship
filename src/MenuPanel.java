import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel{

    JPanel titlePanel;
    JPanel emptyPanelBottom;
    JPanel emptyPanelTop;
    JPanel newGamePanel;
    JPanel loadGamePanel;
    JPanel instructionsPanel;
    JPanel exitPanel;
    JButton newGame;
    JButton loadGame;
    JButton instructions;
    JButton exit;
    JLabel title;

    public MenuPanel(ActionListener listener) {
 
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titlePanel = new JPanel();
        title = new JLabel("Battleship", SwingConstants.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 100));
        titlePanel.add(title);
        this.add(titlePanel);

        emptyPanelBottom = new JPanel();
        emptyPanelBottom.setPreferredSize(new Dimension(800, 200));

        emptyPanelTop = new JPanel();
        emptyPanelTop.setPreferredSize(new Dimension(800, 200));

        newGamePanel = new JPanel();

        newGame = new JButton("New Game");
        newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGame.setActionCommand("new");
        newGame.addActionListener(listener);
        newGame.setFocusable(false);
        newGamePanel.add(newGame);

        loadGamePanel = new JPanel();
        loadGame = new JButton("Load Game");
        loadGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadGame.setActionCommand("load");
        loadGame.addActionListener(listener);
        loadGame.setFocusable(false);
        loadGamePanel.add(loadGame);

        instructionsPanel = new JPanel();
        instructions = new JButton("Instructions");
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions.setActionCommand("instructions");
        instructions.addActionListener(listener);
        instructions.setFocusable(false);
        instructionsPanel.add(instructions);

        exitPanel = new JPanel();
        exit = new JButton("Exit");
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setActionCommand("exit");
        exit.addActionListener(listener);
        exit.setFocusable(false);
        exitPanel.add(exit);

        this.add(emptyPanelTop);
        this.add(newGamePanel);
        this.add(loadGamePanel);
        this.add(instructionsPanel);
        this.add(exitPanel);
        this.add(emptyPanelBottom);
    }
}