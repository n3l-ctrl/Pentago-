import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.Method;

public class CheckGameRunner {
    public static void main(String[] args) throws Exception {
        File jarFile = new File("C:\\Users\\EmDev\\.m2\\repository\\com\\codingame\\gameengine\\runner\\4.2.1\\runner-4.2.1.jar");
        URL[] urls = { jarFile.toURI().toURL() };
        URLClassLoader cl = new URLClassLoader(urls);
        Class<?> gameRunnerClass = cl.loadClass("com.codingame.gameengine.runner.GameRunner");
        
        System.out.println("GameRunner methods:");
        for (Method m : gameRunnerClass.getDeclaredMethods()) {
            if (m.getName().equals("start")) {
                System.out.println(m);
            }
        }
    }
}
