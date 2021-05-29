import javax.imageio.ImageIO;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Player {

    public static final float GRAVITY = 0.7f;
    public static int PlayerSizeX = 40;
    public static int PlayerSizeY = 64;
    public static int PlayerCrouchY = 35 - 1;
    public int PlayerY=0;
    public float speedY = 0;
    private boolean plr = true;

    private final BufferedImage image;
    private final BufferedImage image2;
    private final BufferedImage image3;
    private final BufferedImage image4;

    private AudioClip BOSS_SOUND;

    public Player() throws IOException {
        image = ImageIO.read(new File("data/images/Player.png"));
        image2 = ImageIO.read(new File("data/images/PlayerBoss.png"));
        image3 = ImageIO.read(new File("data/images/Player_squat.png"));
        image4 = ImageIO.read(new File("data/images/PlayerBoss_squat.png"));
    }

    public void draw(Graphics g) throws IOException {

        if(GameScreen.T || GameScreen.points > 1_000) {
            if(GameScreen.crouch)
                g.drawImage(image4, 50, PlayerY + PlayerCrouchY, null);
            else {
                if (plr) {
                    try {
                        BOSS_SOUND = Applet.newAudioClip(new URL("file", "", "data/audio/1up.wav"));

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    if (BOSS_SOUND != null)
                        BOSS_SOUND.play();

                    plr = false;
                }
                g.drawImage(image2, 50, PlayerY, null);
            }
        }
        else{
            if(GameScreen.crouch)
                g.drawImage(image3, 50, PlayerY + PlayerCrouchY, null);
            else
                g.drawImage(image, 50, PlayerY, null);
        }
    }
    public void update(){
        if(PlayerY >= GameScreen.GROUND - PlayerSizeY){
            speedY =0;
            PlayerY= GameScreen.GROUND - PlayerSizeY;
        }else {
            speedY += GRAVITY;
            PlayerY += speedY;
        }
    }
}
