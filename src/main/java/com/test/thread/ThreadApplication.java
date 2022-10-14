package com.test.thread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


@EnableAsync
@RestController
@SpringBootApplication
public class ThreadApplication {

	String errorStr = null;

	private ThreadMarker threadMarker;

    public ThreadApplication(ThreadMarker threadMarker) {
        this.threadMarker = threadMarker;
    }

	

	@Bean
	public Executor taskExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(4000);
		executor.setMaxPoolSize(4000);
		executor.initialize();
		return executor;

	}

	@GetMapping("/thread4000")
    public synchronized String go4000() {
        for (int i = 0; i < 2000; i++) {
			// System.out.println("i: "+i);
			try {
				wait(10);
				threadMarker.create();
				// wait(4000);
			} catch (Exception e) {
				errorStr = getPrintStackTrace(e);
				System.out.println(errorStr);
			}

            
        }
        return "4000번 쓰레드 실행 궈궈링";
    }


	// 현재 쓰레드 찍히나 테스트
	@GetMapping("/test")
	public String test() {
		System.out.println(Thread.currentThread());

		return "test";
	}


	// 로그 string으로 변환해서 찍히게 하는 함수 
	public static String getPrintStackTrace(Exception e) {
         
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
         
        return errors.toString();
         
    }

	public static void main(String[] args) {
		SpringApplication.run(ThreadApplication.class, args);
	}

}
