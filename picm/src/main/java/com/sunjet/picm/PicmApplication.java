package com.sunjet.picm;

import com.sunjet.hessian.HelloService;
import com.sunjet.hessian.PictureUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.remoting.caucho.HessianServiceExporter;

@SpringBootApplication
public class PicmApplication {

	public static void main(String[] args) {
		SpringApplication.run(PicmApplication.class, args);
	}

	@Autowired
	private HelloService helloService;

	@Bean(name = "/helloService")
	public HessianServiceExporter exportHelloService() {
		HessianServiceExporter exporter = new HessianServiceExporter();
		exporter.setService(helloService);
		exporter.setServiceInterface(HelloService.class);
		return exporter;
	}


}
