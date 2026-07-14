import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GenerateMarbles {
    public static void main(String[] args) throws Exception {
        int size = 256;
        generateMarble("red_marble.png", size, new Color(150, 0, 0), new Color(255, 120, 120), new Color(30, 0, 0), false, null);
        generateMarble("blue_marble.png", size, new Color(0, 0, 150), new Color(120, 120, 255), new Color(0, 0, 30), false, null);
        generateMarble("green_marble.png", size, new Color(0, 150, 0), new Color(120, 255, 120), new Color(0, 30, 0), false, null);
        generateMarble("yellow_marble.png", size, new Color(180, 150, 0), new Color(255, 255, 120), new Color(40, 30, 0), false, null);
        
        // Glowing versions
        generateMarble("red_marble_glow.png", size, new Color(220, 50, 50), new Color(255, 200, 200), new Color(100, 0, 0), true, new Color(255, 50, 50));
        generateMarble("blue_marble_glow.png", size, new Color(50, 50, 220), new Color(200, 200, 255), new Color(0, 0, 100), true, new Color(50, 200, 255));
        generateMarble("green_marble_glow.png", size, new Color(50, 220, 50), new Color(200, 255, 200), new Color(0, 100, 0), true, new Color(50, 255, 50));
        generateMarble("yellow_marble_glow.png", size, new Color(230, 200, 50), new Color(255, 255, 200), new Color(120, 100, 0), true, new Color(255, 230, 50));
    }

    static void generateMarble(String filename, int size, Color baseColor, Color highlight, Color shadow, boolean glow, Color glowColor) throws Exception {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        
        // High quality rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // Marble bounds
        int pad = 20; // Increased padding to fit glow
        int r = size / 2 - pad;
        int cx = size / 2;
        int cy = size / 2;

        if (glow) {
            float glowRadius = size / 2.0f;
            float[] glowDist = {0.0f, 0.5f, 1.0f};
            Color[] glowColors = {new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), 200), 
                                  new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), 50),
                                  new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), 0)};
            RadialGradientPaint glowPaint = new RadialGradientPaint(new Point2D.Float(cx, cy), glowRadius, glowDist, glowColors);
            g2.setPaint(glowPaint);
            g2.fillOval(0, 0, size, size);
            
            // Draw a subtle bright ring
            g2.setStroke(new BasicStroke(6));
            g2.setColor(new Color(255, 255, 255, 100));
            g2.drawOval(cx - r, cy - r, r*2, r*2);
        }

        // Base gradient (3D effect)
        Point2D center = new Point2D.Float(cx - r * 0.3f, cy - r * 0.3f);
        float radius = r * 1.5f;
        float[] dist = {0.0f, 0.7f, 1.0f};
        Color[] colors = {highlight, baseColor, shadow};
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);

        g2.setPaint(p);
        g2.fillOval(pad, pad, r * 2, r * 2);

        // Top glossy highlight
        Point2D hlCenter = new Point2D.Float(cx - r * 0.3f, cy - r * 0.3f);
        float hlRadius = r * 0.8f;
        float[] hlDist = {0.0f, 1.0f};
        Color[] hlColors = {new Color(255, 255, 255, 220), new Color(255, 255, 255, 0)};
        RadialGradientPaint hlPaint = new RadialGradientPaint(hlCenter, hlRadius, hlDist, hlColors);

        g2.setPaint(hlPaint);
        g2.fillOval((int)(cx - r * 0.7), (int)(cy - r * 0.8), (int)(r * 1.3), (int)(r * 1.0));

        // Bottom ambient reflection
        Point2D amCenter = new Point2D.Float(cx + r * 0.3f, cy + r * 0.5f);
        float amRadius = r * 0.6f;
        float[] amDist = {0.0f, 1.0f};
        Color[] amColors = {new Color(255, 255, 255, 80), new Color(255, 255, 255, 0)};
        RadialGradientPaint amPaint = new RadialGradientPaint(amCenter, amRadius, amDist, amColors);

        g2.setPaint(amPaint);
        g2.fillOval((int)(cx - r * 0.6), (int)(cy + r * 0.1), (int)(r * 1.2), (int)(r * 0.7));

        g2.dispose();
        ImageIO.write(img, "png", new File(filename));
        System.out.println("Generated " + filename);
    }
}
