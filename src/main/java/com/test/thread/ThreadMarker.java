package com.test.thread;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class ThreadMarker {

    @Async
    public void create() throws InterruptedException{  
     
        System.out.println(Thread.currentThread());
        
        try{
            // thread 유지 시키기 (20분)
            Thread.sleep(12000000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

    }

    // @Async
    // public void hang(){
    //     System.out.println("hang: " + Thread.currentThread());

    //     try {
    //         Thread.sleep(12000000); //1200초 == 20분
    //     } catch (InterruptedException e) {
    //         e.printStackTrace();
    //     }
    // }
    
}
