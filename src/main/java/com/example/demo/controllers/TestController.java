package com.example.demo.controllers;

import com.example.demo.services.Client1Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author huangrenhao
 * @date 2019/2/27
 */
@RestController
public class TestController {

    @Value("${server.port}")
    private String port;
    @Value("${spring.application.name}")
    private String appName;
    @Qualifier("simpleDiscoveryClient")
    @Autowired
    private DiscoveryClient simpleDiscoveryClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Client1Service client1Service;

    @GetMapping("test")
    public String test() throws InterruptedException {
        return appName + ":" + port + "\t" + System.currentTimeMillis();
    }

    /**
     *  异常 Hystrix 熔断器
     * @return
     */

    @GetMapping("hiError")
    public String hiService() {

        return appName + ": " + port + "[ " + client1Service.testHystrixCommand("456789") + " ] : " + System.currentTimeMillis();
//        return appName + ": " + port + "[ " + client1Service.testHystrixCommand() + " ] : " + System.currentTimeMillis();
    }


    @GetMapping("test-client2")
    public String sendMessage(String appName) {

        List<ServiceInstance> serviceInstanceList = simpleDiscoveryClient.getInstances(appName);
        if (!serviceInstanceList.isEmpty()) {
            ServiceInstance serviceInstance = serviceInstanceList.get(0);

            System.out.println(serviceInstance);
            String msg = restTemplate.getForObject("http://spring-cloud-client2/test", String.class);
            return appName + "[ " + msg + " ] : " + System.currentTimeMillis();
        }

//        String msg = restTemplate.getForObject("SPRING-CLOUD-CLIENT1/test", String.class);
//        String msg = restTemplate.getForObject("http://SPRING-CLOUD-CLIENT1/test", String.class);
        String msg = restTemplate.getForObject("http://SPRING-CLOUD-CLIENT2/test", String.class);
//        String msg = restTemplate.getForObject("http://localhost:8083/test", String.class);
        return appName + "[ " + msg + " ] : " + System.currentTimeMillis();

//        return "没有找到服务";
    }
}
