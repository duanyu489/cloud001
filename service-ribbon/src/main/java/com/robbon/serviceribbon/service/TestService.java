package com.robbon.serviceribbon.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestService {

    @Autowired
    RestTemplate restTemplate;

    //在hiService方法上加上@HystrixCommand注解。该注解对该方法创建了熔断器的功能，并指定了fallbackMethod熔断方法，
    // 熔断方法直接返回了一个字符串，字符串为"hi,"+name+",sorry,error!"
    @HystrixCommand(fallbackMethod = "hiError")
    public String hiService(String name) {
        return restTemplate.getForObject("http://SERVICE-HI/hi?name="+name,String.class);
    }

    //当 service-hi 工程不可用的时候，service-ribbon调用 service-hi的API接口时，会执行快速失败，
    // 直接返回一组字符串，而不是等待响应超时，这很好的控制了容器的线程阻塞。
    public String hiError(String name) {
        return "hi,"+name+",sorry,error!";
    }

}
