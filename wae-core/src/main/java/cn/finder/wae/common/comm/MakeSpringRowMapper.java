package cn.finder.wae.common.comm;

import java.lang.reflect.Field;

import cn.finder.wae.business.domain.Log;

public class MakeSpringRowMapper {

    public static void main(String[] args) {
        printResult(Log.class);
    }
   
    private static void printResult(Class m_class) {
        Field propertys[]=m_class.getDeclaredFields();
        String className=m_class.getSimpleName();
        System.out.println("private RowMapper<"+className+"> rowMapper=new RowMapper<"+className+">(){");
       
        System.out.println("    public "+className+" mapRow(ResultSet rs, int index) throws SQLException {");
       
        System.out.println("        "+className+" entitybean= new "+className+"();");
       
        for(Field property:propertys){
            String propertyname=property.getName();
            String m_type=property.getType().toString();
            printSeterFuction(propertyname,m_type);
        }
        System.out.println("return entitybean;");
        System.out.println("    }");
       
        System.out.println("};");
       
    }
   
   
    private static void printSeterFuction(String propertyname,String m_type){
        //entitybean.setStrRoleName(rs.get
        System.out.print("        "+"entitybean.set"+propertyname.substring(0,1).toUpperCase()+propertyname.substring(1)+"(rs.get");
        if(m_type.indexOf("String")!=-1){
            //String
            System.out.print("String");
        }else if(m_type.indexOf("int")!=-1){
            System.out.print("Int");
        }else if(m_type.indexOf("Integer")!=-1){
            System.out.print("Integer");
        }else if(m_type.indexOf("double")!=-1){
            System.out.print("Double");
        }else if(m_type.indexOf("Long")!=-1){
            System.out.print("Long");
        }else if(m_type.indexOf("Timestamp")!=-1){
            System.out.print("Timestamp");
        }else if(m_type.indexOf("Date")!=-1){
            System.out.print("Date");
        }else{
            System.out.print("String");
        }
        //("strRoleName"));
        System.out.println("(\""+propertyname+"\"));");
       
    }

}
