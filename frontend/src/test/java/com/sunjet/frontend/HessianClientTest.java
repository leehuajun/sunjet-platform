package com.sunjet.frontend;

import com.caucho.hessian.client.HessianProxyFactory;
import com.sunjet.frontend.service.system.UserService;
import com.sunjet.hessian.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;

/**
 * @author: lhj
 * @create: 2017-10-27 09:18
 * @description: 说明
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class HessianClientTest {

//    @Autowired
//    private HelloService helloService;


    @Test
    public void testHessian() throws MalformedURLException {
//        System.out.println(helloService.hello("lhj"));

        String url = "http://localhost:8080/picm/helloService";
        HessianProxyFactory factory = new HessianProxyFactory();
        HelloService helloService = (HelloService) factory.create(HelloService.class, url);
        log.info(helloService.hello("lhj"));
    }

}
