import com.github.javaparser.JavaParser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.YamlPrinter;

import java.io.File;
import java.io.FileNotFoundException;

public class quickStart {
    public static void main(String[] args) throws FileNotFoundException {
        // Parse the code you want to inspect:
        File file=new File("D:\\kon_data\\JAVA_DATA\\SpotRenaming\\ast-javaParser\\src\\main\\resources\\quickStart.java");
        CompilationUnit cu = JavaParser.parse(file);

        // Now comes the inspection code:
        //System.out.println(cu);
// Now comes the inspection code:
        YamlPrinter printer = new YamlPrinter(true);
        System.out.println(printer.output(cu));

    }
}
