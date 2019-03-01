package com.example.demo.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author huangrenhao
 * @date 2019/2/27
 */
@Service
public class Client1Service {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hiError")
    public String testHystrixCommand() {
        System.out.println("等待调用.............");
        return restTemplate.getForObject("http://SPRING-CLOUD-CLIENT2/exception", String.class);
    }

    @HystrixCommand(fallbackMethod = "hiError")
    public String testHystrixCommand(String message) {
        System.out.println("等待调用.............");
        return restTemplate.getForObject("http://SPRING-CLOUD-CLIENT2/exception", String.class);
    }


    /**
     * Hystrix 熔断器测试
     *
     * @return
     */
    public String hiError() {
        return "hi,,sorry,error!";
    }

    /**
     *  映射
     */
    public String hiError(String name) {
        return "hi," + name + ",sorry,error!";
    }
}
