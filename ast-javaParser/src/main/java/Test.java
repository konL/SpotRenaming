import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) throws Exception {
        String[] oldset=split("[m_BaseInstance]");
        String[] newset=split("[m_Instance]");
        String[] res={ "[m_BaseInstPanel]", "[setBaseInstanceFromFileQ]","[setBaseInstancesFromDBQ]"};
        for(String resName:res) {
            analysis(changedPart(oldset, newset), oldset, resName);
        }


    }
    public static  Map<String,List<List<String>>> changedPart(String[] s1,String[] s2){
        List<String> oldN=new ArrayList<>(Arrays.asList(s1));
        List<String> newN=new ArrayList<>(Arrays.asList(s2));
        Map<String,List<List<String>>> map=new HashMap<>();

        //检测是删除操作
        //输出value=d<i,k>,key="DELETION";ai~ak 从0开始
        /*思路：旧str中有，新的无则删除了*/
        //删除集合
        List<List<String>> d=new ArrayList<>();
        int i=-1,k=-1;
        for(int j=0;j<oldN.size();j++){

            if(!newN.contains(s1[j])){
                if(i==-1){
                    i=j;
                    k=j;


                }else if(j==i+1){
                    k=j;

                }
                if(j==s1.length-1){
                    d.add(new ArrayList<>(Arrays.asList(new String[]{String.valueOf(i), String.valueOf(k)})));
//                d.clear();
                    i = -1;
                    k = -1;

                }

            }else{
                if (i != -1 && j != -1) {
                    d.add(new ArrayList<>(Arrays.asList(new String[]{String.valueOf(i), String.valueOf(k)})));
//                d.clear();
                    i = -1;
                    k = -1;

                }
            }
        }
        //System.out.println(d.size());
        map.put("DELETION",d);


        //检测时增加操作
        /*同理，思路是newN有，oldN无则为插入*/
        //插入集合
        List<List<String>> sc=new ArrayList<>();

        for(int j=0;j<newN.size();j++){
            //oldName无则为插入术语
            if(!oldN.contains(s2[j])){


                }
            }



        //检测替换操作
        List<List<String>> rp=new ArrayList<>();

//        //测试
        List<List<String>> list=map.get("DELETION");
        //System.out.println(list.size());
        for(List<String> ds:list){
            System.out.println(ds.get(0)+' '+ds.get(1));
        }
        return map;



    }
    public static List<List<String>> analysis( Map<String,List<List<String>>>  changeScript,String[] oldName,String resName){
        List<List<String>> sps=new ArrayList<>();
        for(String key:changeScript.keySet()) {
            //如果是删除操作 d<i,k>---->sp<p,q,len>
            if(key=="DELETION") {
                List<List<String>> delS=changeScript.get("DELETION");
                //计算res集合中: <一个resName,oldNAME>的sps集合

                    for(List<String> d:delS) {
                        //生成分割点sp
                        //1.p=分割点k
                        int p = Integer.parseInt(d.get(1));
                        //2.q是相同字符串的下标（例如，删除base，在res【i】中找到base的下标）
                        List<String> resS = new ArrayList<>(Arrays.asList(split(resName)));
                        int q = resS.indexOf(oldName[p]);
                        //3.长度len，就是删除了几个术语
                        int len = (Integer.parseInt(d.get(1)) - Integer.parseInt(d.get(0))) + 1;
                        //System.out.println("sp1={"+p+" "+q+" "+len+"}");
                        sps.add(new ArrayList<>(Arrays.asList(String.valueOf(p),String.valueOf(q),String.valueOf(len))));



                    }

//                    System.out.println(resName+"-----sps.size()="+sps.size());
//                    for (List<String> spi:sps){
//                        System.out.println("spi={"+spi.get(0)+" "+spi.get(1)+" "+spi.get(2)+"}");
//                    }




            }
            else if(key=="INSERTION"){}
            else if(key=="REPLACE"){}

        }
        return sps;

    }
    public static void process() throws Exception {
        Map<String,List> map=new HashMap<>();
        map=JavaParserUtils.getData();
        List<String> fieldsName=map.get("fields_name");
        List<String> methodName=map.get("method_name");
        //假设已知修改为m_BaseInstance
        fieldsName.remove("[m_BaseInstance]");

    }
    public static  void calSim(){
        //方法一：直接计算
        double dis = StringUtils.getJaroWinklerDistance("m_BaseInstance".toLowerCase(),"setBaseInstance".toLowerCase());
        System.out.println(dis);
        //方法二：分开计算
        String[] o={"m_Base","Instance"};
        String[] c={"setBase","Instance"};
        double sum=0;
        for(int i=0;i<o.length;i++) {
            double dis2 = StringUtils.getJaroWinklerDistance(o[i].toLowerCase(), c[i].toLowerCase());
            sum+=dis2;
            System.out.println(dis2);

        }
        System.out.print(sum/(o.length));

    }
    private static String[] split(String old) {
        /*IdName被分解为由下划线和大写字母分隔的术语序列，假设名称遵循流行的驼峰式或蛇形式命名约定。
        分解不遵循这些约定的标识符名称的替代方法。
         */
        //1.先处理变量名
        String id=old.substring(1,old.length()-1);

        //根据下划线和大写字母分割
        /*正则表达式：句子结束符*/
        String regEx="(?=[_|[A-Z]])";
        Pattern p =Pattern.compile(regEx);
        Matcher m = p.matcher(id);

        /*按照句子结束符分割句子*/
        String[] fieldSeq = p.split(id);

        return fieldSeq;

    }

}
