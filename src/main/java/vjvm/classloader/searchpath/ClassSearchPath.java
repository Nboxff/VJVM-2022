package vjvm.classloader.searchpath;

import vjvm.utils.UnimplementedError;

import java.io.Closeable;
import java.io.InputStream;

/**
 * Represents a path to search class files in.
 * A search path may hold resources, such as jar files, so it must implement the Closeable interface.
 * If a subclass doesn't hold any resources, then just do nothing.
 */
public abstract class ClassSearchPath implements Closeable {
  /**
   * Construct search path objects with a given path.
   */
  public static ClassSearchPath[] constructSearchPath(String path) {
    String sep = System.getProperty("path.separator");
    String[] searchPaths = path.split(sep);
    int length = searchPaths.length;
    ClassSearchPath[] classSearchPaths = new ClassSearchPath[length];
    for (int i = 0; i < length; i++) {
      if (searchPaths[i]. endsWith(".jar") || searchPaths[i]. endsWith(".JAR") ) {
        classSearchPaths[i] = new JarSearchPath(searchPaths[i]);
      }else {
        classSearchPaths[i] = new DirSearchPath(searchPaths[i]);
      }
    }
    return classSearchPaths;
    //throw new UnimplementedError("TODO: parse path and return an array of search paths");
  }

  /**
   * Find a class with specified name.
   *
   * @param name name of the class to find.
   * @return Returns a stream containing the binary data if such class is found, or null if not.
   */
  public abstract InputStream findClass(String name);

}
