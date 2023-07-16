package vjvm.classloader.searchpath;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class JarSearchPath extends ClassSearchPath {
  private final JarFile jarFile;

  public JarSearchPath(String jarName) {
    JarFile jar;
    try {
      jar = new JarFile(jarName);
    } catch (IOException e) {
      jar = null;
    }
    this.jarFile = jar;
  }

  @Override
  public InputStream findClass(String name) {
    if (jarFile == null) return null;
    ZipEntry entry = jarFile.getEntry(name + ".class");
    try {
      // Returns a stream containing the binary data if such class is found, or null if not.
      if (entry == null) return null;
      return jarFile.getInputStream(entry);
    } catch (IOException e) {
      return null;
    }
  }

  @Override
  public void close() throws IOException {
    if (jarFile != null) {
      jarFile.close();
    }
  }
}
