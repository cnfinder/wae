package cn.finder.wae.common.thread;

import java.util.TimerTask;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class MainExecutor extends TimerTask {  
    
    //注入ThreadPoolTaskExecutor 到主线程中  
private ThreadPoolTaskExecutor threadPool;  
 
public void setThreadPool(ThreadPoolTaskExecutor threadPool){  
   this.threadPool = threadPool;  
}  


    //在主线程中执行任务线程.....    
@Override  
public void run() {  
   threadPool.execute(new Task1());  

}  
}
