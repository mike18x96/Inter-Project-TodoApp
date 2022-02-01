package com.inetum.training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TodoAppApplication {

//    private static ApplicationContext applicationContext;


    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplication.class, args);
//        displayAllBeans();

    }

//    public static void displayAllBeans() {
//        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
//        for (String beanName : allBeanNames) {
//            System.out.println(beanName);
//        }
//    }
}
