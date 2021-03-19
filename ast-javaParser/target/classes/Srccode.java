
import javax.swing.*;
import java.awt.*;

public class Srccode extends JPanel {
  protected Instance m_BaseInstance;
  protected Panel m_BaseInstPanel;
    //...
    public static void setBaseInstance(Instance inst){
        m_BaseInstance=inst;
        m_BaseInstPanel.setInstance(m_BaseInstance);
        setBaseInstanceFromFileQ();
        //.......
    }
    public  static void setCaseInstance(){
      //..............
    }
    public void setBaseInstanceFromFileQ(){
      int as=0;
      int as2=1;
      Instance i=m_BaseInstance;
      setBaseInstances(new Instances(r));
        //....
    }
    public void setBaseInstancesFromDBQ(){
      int test=0;
        //......
    }
}