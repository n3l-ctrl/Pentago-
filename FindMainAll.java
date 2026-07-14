import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FindMainAll {
    public static void main(String[] args) throws Exception {
        File[] jars = {
            new File("C:\\Users\\EmDev\\.m2\\repository\\com\\codingame\\gameengine\\runner\\4.2.1\\runner-4.2.1.jar"),
            new File("C:\\Users\\EmDev\\.m2\\repository\\com\\codingame\\gameengine\\core\\4.2.1\\core-4.2.1.jar")
        };
        for (File jarFile : jars) {
            URL[] urls = { jarFile.toURI().toURL() };
            URLClassLoader cl = new URLClassLoader(urls);
            JarFile jar = new JarFile(jarFile);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class") && !entry.getName().contains("$")) {
                    String className = entry.getName().replace("/", ".").substring(0, entry.getName().length() - 6);
                    try {
                        Class<?> c = cl.loadClass(className);
                        for (Method m : c.getDeclaredMethods()) {
                            if (m.getName().equals("main") && java.lang.reflect.Modifier.isStatic(m.getModifiers())) {
                                System.out.println("FOUND MAIN IN: " + className);
                            }
                        }
                    } catch (Throwable t) {
                    }
                }
            }
            jar.close();
        }
    }
}
