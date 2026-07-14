import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GenerateStatementImages {
    public static void main(String[] args) throws Exception {
        generateImage(6, "coords_6x6.png");
        generateImage(9, "coords_9x9.png");
    }

    static void generateImage(int size, String filename) throws Exception {
        int cellSize = 50;
        int gap = 15;
        int blocks = size == 6 ? 2 : 3;
        
        int width = size * cellSize + (blocks - 1) * gap + 100;
        int height = size * cellSize + (blocks - 1) * gap + 100;
        
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Draw axes
        g2.setColor(Color.BLACK);
        for(int x=0; x<size; x++) {
            int blockX = x / 3;
            int px = 50 + x * cellSize + blockX * gap + cellSize/2 - 5;
            g2.drawString(String.valueOf(x), px, 30);
        }
        for(int y=0; y<size; y++) {
            int blockY = y / 3;
            int py = 50 + y * cellSize + blockY * gap + cellSize/2 + 5;
            g2.drawString(String.valueOf(y), 20, py);
        }
        
        // Draw grid
        for(int y=0; y<size; y++) {
            int blockY = y / 3;
            for(int x=0; x<size; x++) {
                int blockX = x / 3;
                int px = 50 + x * cellSize + blockX * gap;
                int py = 50 + y * cellSize + blockY * gap;
                g2.setColor(new Color(240, 240, 240));
                g2.fillRect(px, py, cellSize-2, cellSize-2);
                g2.setColor(new Color(180, 180, 180));
                g2.drawRect(px, py, cellSize-2, cellSize-2);
            }
        }
        
        // Draw blocks
        g2.setFont(new Font("Arial", Font.BOLD, 50));
        for(int by=0; by<blocks; by++) {
            for(int bx=0; bx<blocks; bx++) {
                int blockId = by * blocks + bx;
                int px = 50 + bx * (cellSize * 3 + gap);
                int py = 50 + by * (cellSize * 3 + gap);
                
                g2.setColor(new Color(200, 50, 50, 255));
                g2.setStroke(new BasicStroke(4));
                g2.drawRect(px-2, py-2, cellSize*3+2, cellSize*3+2);
                
                g2.setColor(new Color(200, 50, 50, 100));
                g2.drawString("B" + blockId, px + cellSize - 10, py + cellSize*2 - 10);
            }
        }
        
        g2.dispose();
        ImageIO.write(img, "png", new File(filename));
        System.out.println("Generated " + filename);
    }
}
