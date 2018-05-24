package cn.finder.wae.common.thread;

import java.sql.Time;

public class Task1 implements Runnable {  
    public void run() {  
    Time time  = new Time(System.currentTimeMillis());  
    System.out.println("这是第一个工作,执行于："+time);  
}  

} 
