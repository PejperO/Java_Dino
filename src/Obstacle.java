import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Obstacle {

    public static int ObstacleSizeX = 23;
    public static int ObstacleSizeY = 43;

    public int posX, posY;

    private int swapIMG = 0;
    private float speed = 5;

    private final Rectangle rect;
    private BufferedImage image;

    public Obstacle() throws IOException {
        image = ImageIO.read(new File("data/images/Obstacle.png"));

        posX = GameWindow.windowSizeX;
        posY = GameScreen.GROUND - ObstacleSizeY;

        rect = new Rectangle();
        rect.intersects(rect);
    }

    public void draw(Graphics g) throws IOException {
        if(swapIMG%20 == 0)
            image = ImageIO.read(new File("data/images/Obstacle.png"));

        else if(swapIMG%10 == 0)
            image = ImageIO.read(new File("data/images/Obstacle2.png"));

        g.drawImage(image, posX, posY, null);
        ++swapIMG;

        if(swapIMG == 100)
            swapIMG = 0;
    }

    public void update(){
        if(posX < (-ObstacleSizeX))
            posX = GameWindow.windowSizeX;

        posX -= speed;
        rect.x = posX;
        rect.width = image.getWidth();
        rect.height = image.getHeight();

        if(speed < 50)
            speed +=0.02f;
    }
}
