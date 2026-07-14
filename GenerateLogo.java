import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GenerateLogo {
    public static void main(String[] args) throws Exception {
        int width = 1000;
        int height = 400;
        BufferedImage logo = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = logo.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String text = "PENTAGO";
        Font font = new Font("SansSerif", Font.BOLD | Font.ITALIC, 160);
        g.setFont(font);
        
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int x = (width - textWidth) / 2;
        int y = height / 2 + 40;

        // Glowing cyan aura behind text
        int glowSize = 25;
        for (int i = glowSize; i > 0; i -= 2) {
            g.setColor(new Color(0, 200, 255, 10));
            g.drawString(text, x - i, y);
            g.drawString(text, x + i, y);
            g.drawString(text, x, y - i);
            g.drawString(text, x, y + i);
            g.drawString(text, x - i, y - i);
            g.drawString(text, x + i, y + i);
        }

        // Draw main metallic text
        GradientPaint gp = new GradientPaint(x, y - 120, new Color(255, 255, 255), x, y, new Color(50, 100, 200));
        g.setPaint(gp);
        g.drawString(text, x, y);

        // Draw white outline
        g.setStroke(new BasicStroke(4));
        g.setColor(new Color(255, 255, 255, 100));
        // Drawing outline for text is complex in basic Graphics2D without TextLayout, so we'll just skip outline
        // and rely on the strong contrast.

        // Subtitle
        String subText = "ARENA EDITION";
        Font subFont = new Font("SansSerif", Font.BOLD, 45);
        g.setFont(subFont);
        FontMetrics subFm = g.getFontMetrics();
        int subX = (width - subFm.stringWidth(subText)) / 2;
        int subY = y + 60;

        // Subtitle glow
        g.setColor(new Color(0, 255, 255, 100));
        g.drawString(subText, subX+2, subY+2);
        
        g.setColor(Color.WHITE);
        g.drawString(subText, subX, subY);

        g.dispose();
        ImageIO.write(logo, "png", new File("logo.png"));
        System.out.println("Generated modern logo.png");
    }
}
