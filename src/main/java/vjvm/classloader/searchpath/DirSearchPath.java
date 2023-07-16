package vjvm.classloader.searchpath;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.jar.JarFile;

public class DirSearchPath extends ClassSearchPath {
  private final Path path;

  public DirSearchPath(String pathName) {
    path = FileSystems.getDefault().getPath(pathName);
  }

  @Override
  public InputStream findClass(String name) {
    File file = path.resolve(name + ".class").toFile();
    try {
      return new FileInputStream(file);
    } catch (FileNotFoundException e) {
      return null;
    }
  }

  @Override
  public void close() throws IOException {

  }
}
