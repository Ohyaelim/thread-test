package com.test.thread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

	// index만큼 보내서 연결시키기
	@GetMapping("/send")
    public String connectHang(@RequestParam(value = "index") String index) {
        RestTemplate restTemplate = new RestTemplateBuilder()
			.setConnectTimeout(Duration.ofMillis(20 * 60 * 1000)) // 20분
            .setReadTimeout(Duration.ofMillis(20 * 60 * 1000)) // 20분
            .build();

        System.out.println("index: " +index);

        return restTemplate.getForObject("http://35.91.116.116:8080/receive?index="+index, String.class);
		// return restTemplate.getForObject("http://localhost:8080/receive?index="+index, String.class);
    }

	//받기
	@GetMapping("/receive")
    public String receiveHang(@RequestParam(value = "index") String index) throws InterruptedException {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))+" index: "+index);
        Thread.sleep(12000000); // 1200초 == 20분

        return "receiveHang";
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
