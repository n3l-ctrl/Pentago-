import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;

public class MakeOrange {
    public static void main(String[] args) throws Exception {
        process("src/main/resources/view/assets/yellow_marble.png", "src/main/resources/view/assets/orange_marble.png");
        process("src/main/resources/view/assets/yellow_marble_glow.png", "src/main/resources/view/assets/orange_marble_glow.png");
        System.out.println("Converted to orange.");
    }

    private static void process(String inPath, String outPath) throws Exception {
        BufferedImage img = ImageIO.read(new File(inPath));
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int rgba = img.getRGB(x, y);
                Color c = new Color(rgba, true);
                if (c.getAlpha() > 0) {
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    // Shift yellow to orange by reducing green
                    // But don't reduce it if it's already white/gray
                    if (r > 100 && g > 100 && b < 150) {
                        g = (int)(g * 0.55);
                    } else if (r > 50 && g > 50 && b < 100) {
                        g = (int)(g * 0.65);
                    }
                    Color out = new Color(r, g, b, c.getAlpha());
                    img.setRGB(x, y, out.getRGB());
                }
            }
        }
        ImageIO.write(img, "png", new File(outPath));
    }
}
