import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class GameScreen extends JPanel implements Runnable, KeyListener {

    public static final int GAME_FIRST_STATE = 0;
    public static final int GAME_PLAY_STATE = 1;
    public static final int GAME_OVER_STATE = 2;
    public static final int GROUND = GameWindow.windowSizeY - 100;
    public static int GameSpeed = 20;
    public static float points = 0;
    public static Boolean T = false;
    public static boolean crouch = false;

    private int gameState = GAME_FIRST_STATE;
    private int score;

    private Thread thread;
    private final Obstacle obstacle;
    private final Player player;
    private AudioClip SOUND;

    public  GameScreen() throws IOException {
        thread = new Thread(this);
        obstacle = new Obstacle();
        player = new Player();
    }

    public void startGame(){
        thread = new Thread(this);
        thread.start();
    }

    public void update() {
        if (gameState == GAME_PLAY_STATE) {
            obstacle.update();
        }
        if(obstacle.posX <= 50 + Player.PlayerSizeX && obstacle.posX >= 50 - Obstacle.ObstacleSizeX)
            if(player.PlayerY >= GROUND - Player.PlayerSizeY - Obstacle.ObstacleSizeY)
                gameState = GAME_OVER_STATE;
    }

    public void drawScore(Graphics g){
        g.drawString("HIGH SCORE: " + score, 20, 20);
        g.drawString("score: " + (int)points, 20, 40);
    }

    public void gameOver(){
        if (score < points) {
            PrintWriter out = null;
            try {
                out = new PrintWriter("GameSaves/scores.txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            assert out != null;
            out.println((int) points);
            out.close();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameState == GAME_PLAY_STATE)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (player.PlayerY >= GROUND - Player.PlayerSizeY) {
                        player.speedY = -10;
                        player.PlayerY += player.speedY;

                        try {
                            SOUND = Applet.newAudioClip(new URL("file","","data/audio/jump.wav"));
                        } catch (MalformedURLException malformedURLException) {
                            malformedURLException.printStackTrace();
                        }
                        SOUND.play();
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (player.PlayerY >= GROUND - Player.PlayerSizeY)
                        crouch = true;
                    player.speedY = 10;
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_T)
            if (gameState == GAME_FIRST_STATE)
                T = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            if (gameState == GAME_FIRST_STATE)
                gameState = GAME_PLAY_STATE;

        if (e.getKeyCode() == KeyEvent.VK_P) {
            try {
                new GameScreen();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
            GameScreen.crouch = false;
    }

    @Override
    public void run() {
        File file = new File("GameSaves/scores.txt");
        Scanner in = null;
        try {
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert in != null;
        score = in.nextInt();
        in.close();

        while(true){
                update();
                repaint();
                player.update();

                if(gameState == GAME_OVER_STATE) {
                    gameOver();
                    break;
                }

                if(gameState == GAME_PLAY_STATE)
                    points +=0.1f;

            try {
                Thread.sleep(GameSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void  paint(Graphics g){
        g.setColor( new Color(250, 250, 250) );
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(new Color(50, 170, 255));
        g.drawLine(0, GROUND, getWidth(), GROUND);

        switch (gameState) {
            case GAME_FIRST_STATE -> {
                drawScore(g);
                try {
                    player.draw(g);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            case GAME_PLAY_STATE -> {
                drawScore(g);

                try {
                    player.draw(g);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    obstacle.draw(g);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case GAME_OVER_STATE -> {
                drawScore(g);

                try {
                    player.draw(g);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    obstacle.draw(g);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                g.setColor(Color.red);
                g.drawString("GAME OVER", GameWindow.windowSizeX/2 - 50, GameWindow.windowSizeY/2 - 40);

                try {
                    SOUND =  Applet.newAudioClip(new URL("file","","data/audio/winXP.wav"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                SOUND.play();
            }
        }
    }
}
