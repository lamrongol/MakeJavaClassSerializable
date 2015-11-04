package lamrongol.javatools;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * known bugs:
 * Serializable will be duplicate if a class already has Serializable
 * doesn't work correctly for generics with extends
 */
public class MakeClassSerializable {
    public static void main(String[] args) throws Exception {
        String TARGET_DIRECTORY = "<Input directory containing java files you want to make Serializable>";
        String ENCODING = "UTF-8";

        Collection<File> files = FileUtils.listFiles(new File(TARGET_DIRECTORY), new String[]{"java"}, true);
        for (File file : files) {
            System.out.println(file);
            // creates an input stream for the file to be parsed

            List<String> lines = FileUtils.readLines(file, ENCODING);
            boolean classDeclarationStart = false;
            String implStr = null;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith("package ")) {
                    lines.add(i + 1, "");
                    lines.add(i + 2, "import java.io.Serializable;");
                    continue;
                }

                if (!classDeclarationStart) {
                    if (line.startsWith("public")) {
                        if (line.contains(" class ")) {
                            implStr = "implements";
                        } else if (line.contains(" interface ")) {
                            implStr = "extends";
                        } else continue;
                        classDeclarationStart = true;
                    }
                }

                if (classDeclarationStart) {
                    if (line.contains(implStr + " ")) {
                        line = line.replace(implStr + " ", implStr + " Serializable, ");
                        lines.set(i, line);
                        break;
                    } else if (line.contains("{")) {
                        line = line.replace("{", implStr + " Serializable {");
                        lines.set(i, line);
                        break;
                    }
                }
            }

            FileUtils.writeLines(file, ENCODING, lines);
        }
    }

}
