import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GenerateModernUI {
    public static void main(String[] args) throws Exception {
        int imgSize = 600; // Generate at 2x resolution
        BufferedImage block = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = block.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 1. Futuristic 3D Base
        // Outer metallic edge (shiny silver/blue)
        g.setPaint(new GradientPaint(0, 0, new Color(220, 230, 255), imgSize, imgSize, new Color(70, 80, 110)));
        g.fillRoundRect(4, 4, imgSize-8, imgSize-8, 60, 60);
        
        // Inner recessed metallic area
        g.setPaint(new GradientPaint(0, 0, new Color(40, 50, 70), imgSize, imgSize, new Color(100, 120, 150)));
        g.fillRoundRect(12, 12, imgSize-24, imgSize-24, 50, 50);
        
        // Bevel shadow inside
        g.setStroke(new BasicStroke(4));
        g.setColor(new Color(0, 0, 0, 180));
        g.drawRoundRect(14, 14, imgSize-28, imgSize-28, 48, 48);
        g.setColor(new Color(255, 255, 255, 100));
        g.drawRoundRect(10, 10, imgSize-20, imgSize-20, 52, 52);

        // Neon grid lines
        g.setStroke(new BasicStroke(2));
        g.setColor(new Color(0, 200, 255, 50));
        for (int i = 1; i <= 2; i++) {
            g.drawLine(i * 200, 12, i * 200, imgSize-12);
            g.drawLine(12, i * 200, imgSize-12, i * 200);
        }
        
        // Draw 9 beautifully recessed holes
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int cx = col * 200 + 100;
                int cy = row * 200 + 100;
                int radius = 80;
                
                // Hole background (very dark)
                g.setColor(new Color(10, 12, 15));
                g.fillOval(cx - radius, cy - radius, radius*2, radius*2);
                
                // Inner shadow (top-left dark, bottom-right light)
                g.setStroke(new BasicStroke(6));
                g.setPaint(new GradientPaint(cx - radius, cy - radius, new Color(0, 0, 0, 200), cx + radius, cy + radius, new Color(255, 255, 255, 100)));
                g.drawOval(cx - radius + 3, cy - radius + 3, radius*2 - 6, radius*2 - 6);
                
                // Outer glowing lip (cyan/blue)
                g.setStroke(new BasicStroke(2));
                g.setColor(new Color(0, 255, 255, 150));
                g.drawOval(cx - radius - 2, cy - radius - 2, radius*2 + 4, radius*2 + 4);
            }
        }
        
        // Outer glowing corners (neon)
        g.setStroke(new BasicStroke(6));
        g.setColor(new Color(0, 255, 255, 200));
        g.drawArc(12, 12, 60, 60, 90, 90);
        g.drawArc(imgSize-72, 12, 60, 60, 0, 90);
        g.drawArc(12, imgSize-72, 60, 60, 180, 90);
        g.drawArc(imgSize-72, imgSize-72, 60, 60, 270, 90);
        
        g.dispose();
        ImageIO.write(block, "png", new File("modern_block.png"));
        
        // Generate modern_bg.jpg (High-tech background)
        BufferedImage bg = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bg.createGraphics();
        
        Point2D center = new Point2D.Float(960, 540);
        float[] dist = {0.0f, 0.4f, 1.0f};
        Color[] colors = {new Color(30, 45, 70), new Color(15, 20, 35), new Color(0, 0, 5)};
        RadialGradientPaint rgp = new RadialGradientPaint(center, 1200, dist, colors);
        g2.setPaint(rgp);
        g2.fillRect(0, 0, 1920, 1080);
        
        // Cyberpunk/sci-fi grid
        g2.setStroke(new BasicStroke(1));
        g2.setColor(new Color(0, 150, 255, 20));
        for (int i = 0; i < 1920; i += 80) {
            g2.drawLine(i, 0, i, 1080);
        }
        for (int j = 0; j < 1080; j += 80) {
            g2.drawLine(0, j, 1920, j);
        }
        
        // Hexagon overlay effect (approximate with faint dots for starry/tech feel)
        g2.setColor(new Color(0, 255, 255, 50));
        for (int i = 40; i < 1920; i += 80) {
            for (int j = 40; j < 1080; j += 80) {
                g2.fillRect(i, j, 2, 2);
            }
        }
        
        g2.dispose();
        ImageIO.write(bg, "jpg", new File("modern_bg.jpg"));
        System.out.println("Generated high-end modern UI!");
    }
}
