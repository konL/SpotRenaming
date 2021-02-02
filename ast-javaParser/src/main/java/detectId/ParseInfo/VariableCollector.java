package detectId.ParseInfo;

 
import java.util.Hashtable;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class VariableCollector extends VoidVisitorAdapter<Hashtable<String,Integer>> {
   
	public void visit(VariableDeclarationExpr md, Hashtable<String, Integer> variables) 
	{
		
//		System.out.println(md.getModifiers().toString());
		
	    String location=md.getBegin().toString();
    	
    	Integer loc=Integer.parseInt(location.substring(location.indexOf("line")+4, location.indexOf(",")).trim());  	
    	variables.put(md.getParentNodeForChildren().toString(), loc);
        super.visit(md, variables);
        
    }
	
	
	 
 
}


 
 
 
