import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileWindow extends JFrame implements Runnable, ActionListener {
    //第一步是实现布局
    //1.编译线程
    Thread compiler=null;
    //2.运行线程
    Thread run_prom=null;
    //bn
    boolean bn=true;
    //使用卡片布局
    CardLayout mycard;
    //保存文件
    File file_save=null;
    //按钮的定义：程序输入区，编译结果区，程序运行区，编译程序，运行程序
    JButton button_input_txt,
            button_compiler_text,
            button_run_prom,
            button_compiler,
            button_see_doswin;
    //三个文本域三个表示不同的切换区

    JPanel p=new JPanel();
    JTextArea input_text=new JTextArea();//程序输入区
    JTextArea compiler_text=new JTextArea();//编译错误显示
    JTextArea dos_out_text=new JTextArea();//程序输出信息

    //两个TextField用户输入
    //输入编译文件名
    JTextField input_file_name_text=new JTextField();
    //输入应用程序主类名
    JTextField run_file_name_text=new JTextField();

    public FileWindow(){
        super("JAVA语言编译器");
        //实例化
        mycard=new CardLayout();
        //实例化线程
        compiler=new Thread(this);
        run_prom=new Thread(this);
        button_input_txt=new JButton("程序输入区(白色）");
        button_compiler_text=new JButton("编译结果区（粉红色）");
        button_run_prom=new JButton("运行程序");
        button_compiler=new JButton("编译程序");
        button_see_doswin=new JButton("程序运行结果（浅蓝色）");

        p.setLayout(mycard);
        //使用卡片放置三个显示区
        p.add("input",input_text);//定义卡片名称
        p.add("compiler", compiler_text);
        p.add("dos",dos_out_text);
        add(p,"Center");
        //设置三个县市区的颜色
        compiler_text.setBackground(Color.pink);
        dos_out_text.setBackground(Color.cyan);

        //设置p1，放到最上面
        JPanel p1=new JPanel();

        p1.setLayout(new GridLayout(3, 3)); //设置表格布局
        //添加组件
        p1.add(button_input_txt);
        p1.add(button_compiler_text);
        p1.add(button_see_doswin);
        p1.add(new JLabel("输入编译文件名（.java）："));
        p1.add(input_file_name_text);
        p1.add(button_compiler);
        p1.add(new JLabel("输入应用程序主类名"));
        p1.add(run_file_name_text);
        p1.add(button_run_prom);
        add(p1,"North");

        //定义事件
        button_input_txt.addActionListener(this);
        button_compiler.addActionListener(this);
        button_compiler_text.addActionListener(this);
        button_run_prom.addActionListener(this);
        button_see_doswin.addActionListener(this);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //先处理点击事件
        if(e.getSource()==button_input_txt){
            mycard.show(p,"input");
        }
        else if(e.getSource()==button_compiler_text){
            mycard.show(p,"compiler");
        }
        else if(e.getSource()==button_see_doswin){
            mycard.show(p,"dos");
        }
        else if(e.getSource()==button_compiler){
            //执行编译文件方法后，显示编译输出信息区
            if(!(compiler.isAlive())){
                compiler=new Thread(this);
            }
            try {
                compiler.start();
            }catch (Exception e2){
                e2.printStackTrace();
            }
            mycard.show(p,"compiler");

        }
        else if(e.getSource()==button_run_prom)
        {    //如果是运行按钮，执行运行文件的方法
            if(!(run_prom.isAlive()))
            {
                run_prom=new Thread(this);
            }
            try {
                run_prom.start();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
            mycard.show(p,"dos");
        }

    }

    @Override
    public void run() {
        //这个方法要判断编译还是运行
        //如果当前Thread是编译，则程序输入区代码保存为java代码；javac编译生成class，并且编译信息输出到编译结果县市区
        //Thread是运行，java class文件，显示到程序运行结果区

        //判断线程是编译
        if(Thread.currentThread()==compiler){
            /*第一步：写入java文件*/
            //1.定义编译显示信息
            compiler_text.setText(null);
            //2.定义java文件名字=input_file_name
            String file_name=null;
            file_name=input_file_name_text.getText().trim();
            try {
                //3.定义保存文件file
                file_save=new File(file_name);
                //4.获取程序输入区的程序
                String temp=input_text.getText().trim();
                //5.读入数据的设置 buffer存程序 len(buffer)
                byte [] buffer=temp.getBytes();
                int b=buffer.length;
                //6.定义文件输出流，保存到上面定义的文件中
                FileOutputStream writefile=null;
                writefile=new FileOutputStream(file_save);
                //7.写入数据
                writefile.write(buffer,0,b);
                //8.关闭输出流
                writefile.close();
            }catch(Exception e){
                System.out.println("ERROR");

            }
            /*第二步：编译为class文件*/
            try {
                //9.编译,用Runtime类
                Runtime rt=Runtime.getRuntime();
                InputStream in=rt.exec("javac "+file_name).getErrorStream();
                //10.获取错误流，取编译信息显示到编译区。成功的标志是编译信息非空
                BufferedInputStream bufIn=new BufferedInputStream(in);
                byte[] shuzu=new byte[100];
                int n=0;
                boolean flag=true;

                //11.显示到编译区
                while((n=bufIn.read(shuzu,0,shuzu.length))!=-1){
                    String s=null;
                    s=new String(shuzu,0,n);
                    compiler_text.append(s);
                    if(s!=null){
                        flag=false;
                    }

                }
                if(flag){
                    compiler_text.append("Compiler succeed! ");                }
            }catch (Exception e){

            }
        }
        //线程是运行
        else if(Thread.currentThread()==run_prom){
            //1.定义运行信息输出区
            dos_out_text.setText(null);
            //2.class文件来自于输入的runtime_file
            String path=run_file_name_text.getText().trim();
            try {
                Runtime rt=Runtime.getRuntime();
                //3.Runtime运行 class文件
                Process stream=rt.exec("java "+path);
                //4.BufferInput包裹Input=过程流
                InputStream in=stream.getInputStream();
                BufferedInputStream bisErr=new BufferedInputStream(stream.getErrorStream());
                BufferedInputStream bisIn=new BufferedInputStream(in);
                //5。ERR输出错误信息 BUF输出运行信息
                byte[] buf=new byte[150];
                byte[] err_buf=new byte[150];
                int m=0;
                int n=0;
                String s=null;
                String err=null;
                while ((m=bisIn.read(buf,0,150))!=-1){
                    s=new String(buf,0,150);
                    dos_out_text.append(s);
                }
                while((n=bisErr.read(err_buf,0,150))!=-1){
                    err=new String(err_buf,0,150);
                    dos_out_text.append(err);
                }
            }catch(Exception e){

            }
        }
    }
}
