

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tokenizeUtil {
    static List<String>  methodName;
    static List<String>  methodBody;
    public static void main(String[] args) throws Exception {
        //获取method name
        //获取mejthod body
        //从Parser中获取变量名和方法名
        Map<String, List> map=new HashMap<>();
        map= JavaParserUtils.getData();
        methodName=map.get("method_name");
        methodBody=map.get("method_body");
        //对method tokenizon
        List<List<String>> methodName_token=tokenizeName(methodName);


        //存到文档里，使用paragraph vector
        //先假设只有一个java文件
        File file = new File("D:\\kon_data\\JAVA_DATA\\deeplearning4j-examples\\dl4j-examples\\input\\raw_methodName.txt");

        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        for(List<String> list:methodName_token){
            for(String s:list){
                bw.write(s+" ");
            }
            bw.write("\n");;
        }

        bw.close();

        System.out.println("methodName Done");

        //把methodBody word2vec化
        //存到文档里，使用paragraph vector
        //先假设只有一个java文件
        File file_body = new File("D:\\kon_data\\JAVA_DATA\\deeplearning4j-examples\\dl4j-examples\\input\\raw_methodBody.txt");

        // if file doesnt exists, then create it
        if (!file_body.exists()) {
            file_body.createNewFile();
        }

        FileWriter fw_body = new FileWriter(file_body.getAbsoluteFile());
        BufferedWriter bw_body = new BufferedWriter(fw_body);
        for(String s:methodBody){

            bw_body.write(s);
            bw_body.write("\n");;
        }

        bw_body.close();

        System.out.println("methodBody Done");

        
    }

    public static List<List<String>> tokenizeName(List<String> methodName) {
        /*基于camel大小写和下划线方法名被分成子标记序列，转换为小写形式。
        方法名称findField和find_field被标记为相同的序列[find，field]
         */
        List<List<String>> result=new ArrayList<>();

       String regEx="(?=[_|[A-Z]])";
       // String regEx="(?=[_])";

        Pattern p =Pattern.compile(regEx);
        for(String name:methodName){
            //1.分割
            //2.转化为全小写，加入序列种


            //根据下划线和大写字母分割
            /*正则表达式：句子结束符*/

           //Matcher m = p.matcher(name);

            /*按照句子结束符分割句子*/
            String[] data = p.split(name);


            List<String> seq=new ArrayList<>();
            for(String s:data){

                if(!s.equals("_")) {
                    seq.add(s.toLowerCase());
                }
            }
           // System.out.println(notInclude(result,seq));
            if(notInclude(result,seq)){
                result.add(seq);
            }



        }

        return result;

    }

    public static boolean notInclude(List<List<String>> result, List<String> seq) {
        if(result.size()==0) return true;

        //假设原来就包含

        for(List<String> res:result){
            //假设该字符串是重复的
            boolean ishas=true;

            for(String s:seq){
                //只要发掘有一个词不重复则假设是错的
               // System.out.println(s);
                if(!res.contains(s)){
                    ishas=false;

                }

            }
            if(ishas){
                return false;
            }
        }
        return true;
    }


}
