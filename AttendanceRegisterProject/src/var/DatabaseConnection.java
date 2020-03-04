/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package var;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author GP62 
 */
public class DatabaseConnection {
    
    static Connection conn;
    static ResultSet kaushal;
    public static void ConnectorDB()
    {
        try
        {
            Class.forName("oracle.jdbc.OracleDriver");
            conn= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:oracle","system","Admin123");
//            return conn;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
//        return null;
    }
    
    public static void insertionTeacher(String tid, String tname, String password)
    {   
        int flag=0;
        try
        {
            PreparedStatement cstmt=conn.prepareStatement("select * from teacher");
            ResultSet rs=cstmt.executeQuery();
            while(rs.next())
            {
                if(rs.getString(1).equals(tid))
                {
                    flag=1;
                }
            }
            
            if(flag==0)
            {
                PreparedStatement istmt = conn.prepareStatement("insert into teacher values(?,?,?)");
                istmt.setString(1,tid);
                istmt.setString(2,tname);
                istmt.setString(3,password);
                int i=istmt.executeUpdate();
                conn.close();
               
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Teacher Already Signed Up");
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
    }
    
    /**
     *
     * @param tid
     * @param pass
     * @return
     */
    public static boolean LogInCheck(String tid, String pass)
    {
        boolean flag=false;
        try
        {
//            String temptid=tid.toUpperCase();
            PreparedStatement lginc= conn.prepareStatement("select * from teacher where tid='"+tid+"'");
            ResultSet rs=lginc.executeQuery();
            while(rs.next())
            {
                if(rs.getString(1).equals(tid))
                {
                    if(rs.getString(3).equals(pass))
                    {
                        flag=true;
                    }
                }
            }  
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
        return flag;
    }
    
    public static void getStudentDetails()
    {
        try
        {
            PreparedStatement gst=conn.prepareStatement("select * from student where section='A'");
            ResultSet rs=gst.executeQuery();
            while(rs.next())
            {
                
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
    }
    
    
    public static String[] SelectSubject()
    {
        String Sub[]=new String[5];
        int i=0;
        
        try
        {
            PreparedStatement ss=conn.prepareStatement("select * from subject");
            ResultSet rs=ss.executeQuery();
            while(rs.next())
            {
                Sub[i]=rs.getString(2);
                i++;
            }
            
            
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
        return Sub;
    }
    
    
    
//    public static String SubjectSelected()
//    {
//        String subject=new String();
//        try
//        {
//            PreparedStatement subsel=conn.prepareStatement("");
//        }
//        catch(Exception e)
//        {
//            JOptionPane.showMessageDialog(null,e);
//        }
//        return subject;
//    }
    
    public static void fetchStudents(String Sub, char sec)
    {
        String sid=new String();
        String sname=new String();
        char studsec=sec;

        try
        {
            PreparedStatement fs=conn.prepareStatement("select * from student where section='"+studsec+"' order by sid asc");
            kaushal=fs.executeQuery();

            kaushal.next();
            sid=kaushal.getString(1);
            sname=kaushal.getString(2);
            StudentPresentAbsent spa=new StudentPresentAbsent(Sub,sid,sname,studsec);
            spa.setVisible(true);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
       
    }
    
    
    
    
    public static void newfetchStudents(String Sub, char sec)
    {
        String sid=new String();
        String sname=new String();
        char studsec=sec;
//        String
        try
        {
            
            
//            while(.next())
//            {
            if(kaushal.next()){
//                StudentPresentAbsent.beizzati=false;
            sid=kaushal.getString(1);
            sname=kaushal.getString(2);
            StudentPresentAbsent spa=new StudentPresentAbsent(Sub,sid,sname,studsec);
            spa.setVisible(true);
            }
            
//                if(!StudentPresentAbsent.beizzati)
//                {
//                    Thread.holdsLock(spa);
//                }
//            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
       
    }
    
    
    
    
    public static void studentPresent(String Subject,String USN,String Name,char section)
    {
        String subid;
        String Attendance;
        Attendance = "P";
        try
        {
            PreparedStatement getStudentUsn=conn.prepareStatement("select sub_id from subject where sub_name='"+Subject+"'");
            ResultSet gsu=getStudentUsn.executeQuery();
            gsu.next();
            subid=gsu.getString(1);
            PreparedStatement spres=conn.prepareStatement("insert into attendance values(?,?,?)");
            spres.setString(1,USN);
            spres.setString(2,subid);
            spres.setString(3,Attendance);
            int i=spres.executeUpdate();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
    }
    
    public static void studentAbsent(String Subject,String USN, String Name,char section)
    {
         String subid;
        String Attendance;
        Attendance = "A";
        try
        {
            PreparedStatement getStudentUsn=conn.prepareStatement("select sub_id from subject where sub_name='"+Subject+"'");
            ResultSet gsu=getStudentUsn.executeQuery();
            gsu.next();
            subid=gsu.getString(1);
            PreparedStatement spres=conn.prepareStatement("insert into attendance values(?,?,?)");
            spres.setString(1,USN);
            spres.setString(2,subid);
            spres.setString(3,Attendance);
            int i=spres.executeUpdate();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
    }
    
    public static float subjectWiseAttendanceCalculation(String SubjectName,String USN)
    {
        String SubId;
        float subjectPresent,subjectTotal;
        float subjectPercentage=0;
        try
        {
            PreparedStatement fsi=conn.prepareStatement("select sub_id from subject where sub_name='"+SubjectName+"'");
            ResultSet rs=fsi.executeQuery();
            rs.next();
            SubId=rs.getString(1);
            PreparedStatement getPresent=conn.prepareStatement("select count(*) from attendance where sid='"+USN+"' and sub_id='"+SubId+"' and attendance='P'");
            ResultSet rrpp=getPresent.executeQuery();
            rrpp.next();
            subjectPresent=rrpp.getInt(1);
            PreparedStatement getTotal=conn.prepareStatement("select count(*) from attendance where sid='"+USN+"' and sub_id='"+SubId+"'");
            ResultSet rrtt=getTotal.executeQuery();
            rrtt.next();
            subjectTotal=rrtt.getInt(1);
            subjectPercentage=((subjectPresent/subjectTotal)*100);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
        return subjectPercentage;
    }
    
    public static float totalAttendanceCalculations(String USN)
    {
        float totalPresent,totalTotal;
        float totalPercentage=0;
        try
        {
            PreparedStatement tac=conn.prepareStatement("select count(*) from attendance where sid='"+USN+"' and attendance='P'");
            ResultSet tp=tac.executeQuery();
            tp.next();
            totalPresent=tp.getInt(1);
            
            PreparedStatement taclc=conn.prepareStatement("select count(*) from attendance where sid='"+USN+"'");
            ResultSet tt=taclc.executeQuery();
            tt.next();
            totalTotal=tt.getInt(1);
            
            totalPercentage=((totalPresent/totalTotal)*100);
            
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }    
        return totalPercentage;
    }
    
    public static String getStudentName(String USN)
    {
        String studentName="";
        try
        {
            PreparedStatement gsnn=conn.prepareStatement("select sname from student where sid='"+USN+"'");
            ResultSet gsnr=gsnn.executeQuery();
            gsnr.next();
            studentName=gsnr.getString(1);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
        return studentName;
    }
    
    
}
