package lamrongol.javatools;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * This may affect other parts, too, MakeClassSerializable is recommended
 */
public class MakeClassSerializableUsingJavaParser {
    public static void main(String[] args) throws Exception {
        String TARGET_DIRECTORY = "C:\\workspaces\\GitHub\\kuromoji\\kuromoji-core\\src\\main\\java\\com\\atilika\\kuromoji\\";
        String ENCODING = "UTF-8";

        Collection<File> files = FileUtils.listFiles(new File(TARGET_DIRECTORY), new String[]{"java"}, true);
        for (File file : files) {
            System.out.println(file);
            // creates an input stream for the file to be parsed

            CompilationUnit cu;
            // parse the file
            cu = JavaParser.parse(file, ENCODING);

            List<TypeDeclaration> types = cu.getTypes();
            rootLoop:
            for (TypeDeclaration type : types) {
                if (!(type instanceof ClassOrInterfaceDeclaration)) continue;

                ClassOrInterfaceDeclaration c = (ClassOrInterfaceDeclaration) type;
                List<ClassOrInterfaceType> impls = c.getImplements();
                for (ClassOrInterfaceType impl : impls) {
                    if (impl.getName().equals("Serializable")) continue rootLoop;
                }

                impls.add(new ClassOrInterfaceType("Serializable"));
                c.setImplements(impls);
            }

            FileUtils.writeStringToFile(file, cu.toString(), ENCODING);
        }
    }

}
