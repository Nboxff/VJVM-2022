package vjvm.classfiledefs;

import lombok.var;

import static vjvm.classfiledefs.Descriptors.DESC_array;
import static vjvm.classfiledefs.Descriptors.DESC_reference;

public class MethodDescriptors {

//  public static int argc(String descriptor) {
//    assert descriptor.startsWith("(");
//    int args = 0;
//    int iBegin = descriptor.indexOf('(') + 1;
//    int iEnd = descriptor.lastIndexOf(')');
//    while(iBegin < iEnd){
//      switch (descriptor.charAt(iBegin++)){
//        case 'J':
//        case 'D':
//          args += 2;
//          break;
//        case 'I':
//        case 'F':
//        case 'C':
//        case 'Z':
//        case 'S':
//        case 'B':
//          args++;
//          break;
//        case '[':
//          break;
//        case 'L':
//          args++;
//          while (iBegin < iEnd && descriptor.charAt(iBegin) != ';')
//            iBegin++;
//          break;
//        default:
//          throw new UnsupportedOperationException("Wrong descriptor!");
//      }
//    }
//    return args;
//  }

  public static int argc(String descriptor) {
    assert descriptor.startsWith("(");

    int argc = 0;
    for (int i = 1; i < descriptor.length(); ) {
      if (descriptor.charAt(i) == ')') break;
      argc += Descriptors.size(descriptor.charAt(i));

      while(descriptor.charAt(i) == DESC_array) ++i;
      if (descriptor.charAt(i) == DESC_reference)
        i = descriptor.indexOf(';', i) + 1;
      else ++i;
    }
    return argc;
  }

  public static char returnType(String descriptor) {
    assert descriptor.startsWith("(");

    var i = descriptor.indexOf(')') + 1;
    assert i < descriptor.length();
    return descriptor.charAt(i);
  }
}
