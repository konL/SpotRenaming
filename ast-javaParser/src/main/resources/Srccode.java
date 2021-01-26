import javax.swing.*;
import java.awt.*;

public class Srccode extends JPanel {
  protected Instance m_BaseInstance;
  protected Panel m_BaseInstPanel;
    //...
    public void setBaseInstance(Instance inst){
        m_BaseInstance=inst;
        m_BaseInstPanel.setInstance(m_BaseInstance);
        //.......
    }
    public void setBaseInstanceFromFileQ(){
      int as=0;
      int as2=1;
        //....
    }
    public void setBaseInstancesFromDBQ(){
      int test=0;
        //......
    }
}