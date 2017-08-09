package me.mymilkbottles.weixinqing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class WeixinqingApplication {

	public static void main(String[] args) {

		SpringApplication.run(WeixinqingApplication.class, args);
	}
}

//@SpringBootApplication
////@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
//public class WeixinqingApplication extends SpringBootServletInitializer{
//
////	public static void main(String[] args) {
////		SpringApplication.run(WeixinqingApplication.class, args);
////	}
//
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		builder.sources(this.getClass());
//		return super.configure(builder);
//	}
//}
