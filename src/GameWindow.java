import javax.swing.*;
import java.io.IOException;

public class GameWindow extends JFrame{

    public static int windowSizeX = 1_000;
    public static int windowSizeY = 300;

    public GameWindow() throws IOException {
        super("SUPER JAVA GAME");
        setSize(windowSizeX, windowSizeY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameScreen gameScreen = new GameScreen();
        add(gameScreen);
        addKeyListener(gameScreen);
        gameScreen.startGame();
    }

    public static void main(String[] args) throws IOException {
        GameWindow gw = new GameWindow();
        gw.setVisible(true);
    }
}
