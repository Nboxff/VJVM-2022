package vjvm.classloader;


import lombok.Getter;
import lombok.SneakyThrows;
import lombok.var;
import vjvm.classloader.searchpath.ClassSearchPath;
import vjvm.runtime.JClass;
import vjvm.vm.VMContext;
import vjvm.utils.UnimplementedError;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.HashMap;

public class JClassLoader implements Closeable {
  private final JClassLoader parent;
  private final ClassSearchPath[] searchPaths;
  private final HashMap<String, JClass> definedClass = new HashMap<>();
  @Getter
  private final VMContext context;

  public JClassLoader(JClassLoader parent, ClassSearchPath[] searchPaths, VMContext context) {
    this.context = context;
    this.parent = parent;
    this.searchPaths = searchPaths;
  }

  /**
   * Load class
   * <p>
   * If a class is found, construct it using the data returned by ClassSearchPath.findClass and return it.
   * <p>
   * Otherwise, return null.
   */
  public JClass loadClass(String descriptor) {
    // throw new UnimplementedError("TODO: load class");
    String className = descriptor.substring(1, descriptor.length() - 1);
    if (definedClass.containsKey(className)) {
      return definedClass.get(className);
    } else if (parent != null) {
      JClass jClass = parent.loadClass(descriptor);
      if (jClass != null) {
        return jClass;
      }
    }
    for (ClassSearchPath path : searchPaths) {
      InputStream streamInputData = null;
      streamInputData = path.findClass(className);
      if (streamInputData != null) {
        JClass jClass = new JClass(new DataInputStream(streamInputData), this);
        definedClass.put(className, jClass);
        return jClass;
      }
    }
    return null;

    // To construct a JClass, use the following constructor
    // return new JClass(new DataInputStream(istream_from_file), this);
  }

  @Override
  @SneakyThrows
  public void close() {
    for (var s : searchPaths)
      s.close();
  }
}
