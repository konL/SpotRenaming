import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class ASTParserTree {
    private static final String filePath = "D:\\kon_data\\JAVA_DATA\\SpotRenaming\\ast-javaParser\\src\\main\\resources\\FileWindow.java";

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        // 读取源文件信息
        File file = new File(filePath);
        byte[] b = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(b);
        String content = new String(b);

        // 创建解析器
        ASTParser parsert = ASTParser.newParser(AST.JLS3);

        // 设定解析器的源代码字符
        parsert.setSource(content.toCharArray());

        // 使用解析器进行解析并返回AST上下文结果(CompilationUnit为根节点)
        CompilationUnit result = (CompilationUnit) parsert.createAST(null);

        // 获取类型
        List types = result.types();

        // 取得类型声明
        TypeDeclaration typeDec = (TypeDeclaration) types.get(0);

        // 获取文件的包名并打印
        PackageDeclaration packetDec = result.getPackage();
        if(packetDec!=null) {
            System.out.println("包名:" + packetDec.getName());
        }
        // 获取类名并打印
        String className = typeDec.getName().toString();
        System.out.println("类名:" + className);

        // 变量列表
        FieldDeclaration fieldDec[] = typeDec.getFields();
        Modifier modify = null;
        System.out.println("\r\n----------------变量信息列表----------------");
        for (FieldDeclaration fieldDecEle : fieldDec) {
            String fieldModifier = "";
            for (Object modifiObj : fieldDecEle.modifiers()) {
                modify = (Modifier) modifiObj;
                fieldModifier += modify + "  ";
            }
            for (Object object : fieldDecEle.fragments()) {
                VariableDeclarationFragment frag = (VariableDeclarationFragment) object;
                System.out.println("名称：" + frag.getName() + "\t类型：" + fieldDecEle.getType() + "\t修饰符：" + fieldModifier + "\t内容："
                        + frag.getInitializer());
            }
        }

        // 获取import内容并打印
        List<ImportDeclaration> importList = result.imports();
        System.out.println("\r\n----------------import信息列表----------------");
        for (ImportDeclaration importDeclaration : importList) {
            System.out.println(importDeclaration.getName());
        }

        // 获取类中的方法
        MethodDeclaration methodDec[] = typeDec.getMethods();
        System.out.println("\r\n----------------方法信息列表----------------");
        for (MethodDeclaration declaration : methodDec) {
            String methodName = declaration.getName().toString();
            System.out.println("\r\n----------------方法名：" + methodName + "----------------");
            System.out.println("\r\n方法注释列表：");
            // 获取方法注释
            if (declaration.getJavadoc() == null) {
                System.out.println("该方法没有注释！");
            } else {
                List<TagElement> methodRemark = declaration.getJavadoc().tags();
                String remarkContent = "";
                for (TagElement element : methodRemark) {
                    // 获取注释种类
                    remarkContent = "类型:" + element.getTagName();
                    // 获取注释条目的类型
                    remarkContent += "\t名称：" + (element.fragments().size() > 0 ? element.fragments().get(0) : null);
                    // 获取注释条目中的注释
                    if (element.fragments().size() > 1) {
                        remarkContent += "\t注释：" + element.fragments().get(1);
                    }
                    System.out.println(remarkContent);
                }
            }

            // 获取参数列表
            List<ASTNode> parameters = declaration.parameters();
            System.out.println("\r\n方法参数列表：");
            SingleVariableDeclaration svdParameter = null;
            for (ASTNode parameter : parameters) {
                svdParameter = (SingleVariableDeclaration) parameter;
                System.out.println("名称：" + svdParameter.getName() + "\t类型：" + svdParameter.getType());
            }

            // 获取异常列表
            List<ASTNode> exceptions = declaration.thrownExceptions();
            System.out.println("\r\n方法异常列表：");
            for (ASTNode exception : exceptions) {
                System.out.println("类型：" + exception.toString());
            }

            // System.out.println("方法体：");
            // System.out.println(declaration.getBody().toString());
            System.out.println("\r\n----------------方法体信息分析----------------");
            Block body = declaration.getBody();
            // 获取方法体集合
            List<ASTNode> statements = body.statements();
            VariableDeclarationStatement svdNode = null;
            TryStatement tsNode = null;
            for (ASTNode node : statements) {
                // 如果是普通类型的代码快
                if (node instanceof VariableDeclarationStatement) {
                    svdNode = (VariableDeclarationStatement) node;
                    System.out.println("声明类型：" + svdNode.getType() + "\t代码内容：" + svdNode.fragments());
                } else if (node instanceof TryStatement) {// 如果是异常类型的代码块
                    tsNode = (TryStatement) node;
                    // 获取try内容
                    System.out.println("try方法体内容：\r\n" + tsNode.getBody());
                    // 获取catch内容列表
                    System.out.println("catch方法体内容：");
                    List<ASTNode> catchs = tsNode.catchClauses();
                    for (ASTNode node2 : catchs) {
                        CatchClause cc = (CatchClause) node2;
                        System.out.println(cc.getBody());
                    }
                    // 获取finally内容
                    System.out.println("finally方法体内容：\r\n" + tsNode.getFinally());
                }
            }
        }
    }
}
