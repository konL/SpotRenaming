import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class JavaParserUtils {
    static List<String> methodName=new ArrayList<String>();
    static List<String> fieldsName=new ArrayList<String>();
    static List<String> methodBody=new ArrayList<String>();
    static List<String> varibleName=new ArrayList<String>();
    public static Map<String,List> getData() throws Exception{
        FileInputStream in = new FileInputStream("D:\\kon_data\\JAVA_DATA\\SpotRenaming\\ast-javaParser\\src\\main\\resources\\Srccode.java");

        // parse the file
        CompilationUnit cu = JavaParser.parse(in);


        // prints the resulting compilation unit to default system output
//        System.out.println(cu.toString());

        cu.accept(new Visitor(), null);
//        //查看list
//        for(String mn:methodBody){
//            System.out.println(mn);
//        }
        Map<String,List> map=new HashMap<>();
        map.put("fields_name",fieldsName);
        map.put("method_name",methodName);
        map.put("method_body",methodBody);
        map.put("variable_name",varibleName);
        return map;


    }


    private static class Visitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(final FieldDeclaration n, Void arg) {
            System.out.println("Fields:"+n.getVariables());
            fieldsName.add(n.getVariables().toString());

            super.visit(n, arg);
        }
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */

            methodName.add(n.getNameAsString());
            System.out.println("methodName:"+n.getName());

           // System.out.println("methodParams:"+n.getBody().);
//            System.out.println("----------------------------------------------------------------------------");
//            System.out.println("methodBody:"+n.getBody());
//            System.out.println("----------------------------------------------------------------------------");
            methodBody.add(n.getBody().toString());



            super.visit(n, arg);
        }

        @Override
        public void visit(VariableDeclarationExpr n, Void arg) {
            //System.out.println(n.getVariables());
            String data=n.getVariables().toString();
            String[] set=data.substring(1,data.length()-1).split("=");
            varibleName.add(set[0].trim());


            super.visit(n, arg);
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            System.out.println("class:"+n.getName());
            System.out.println("extends:"+n.getExtendedTypes());
            System.out.println("implements:"+n.getImplementedTypes());

            super.visit(n, arg);
        }

        @Override
        public void visit(PackageDeclaration n, Void arg) {
            System.out.println("package:"+n.getName());
            super.visit(n, arg);
        }


    }
}
