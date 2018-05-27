package com.swj.ics.web_dao.domain.autodealer;

import com.swj.ics.web_dao.domain.autodealer.interfaces.Description2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by swj on 2017/5/21.
 */
public class TestAnnotation {
    public static void main(String[] args) {
        //testClassAnnotation();
       // testMesthodAnnotation();
        testMesthodAnnotation2();
    }

    static void testMesthodAnnotation()
    {
        try {
            Class cls=Class.forName("com.swj.ics.web_dao.domain.autodealer.AnnotationChild");
            Method[] methods=cls.getMethods();
            for(Method m : methods){
                boolean isAnnoExists = m.isAnnotationPresent(Description2.class);
                if(isAnnoExists){
                    Description2 descAnno=m.getAnnotation(Description2.class);
                    System.out.println( descAnno.value());
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void testMesthodAnnotation2()
    {
        try {
            Class cls=Class.forName("com.swj.ics.web_dao.domain.autodealer.AnnotationChild");
            Method[] methods=cls.getMethods();
            for(Method m : methods) {
                Annotation[] annotations = m.getAnnotations();
                for(Annotation annotation : annotations) {
                    if(annotation instanceof Description2) {
                        System.out.println(((Description2) annotation).value());
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void testClassAnnotation() {
        try {
            Class cls=Class.forName("com.swj.ics.web_dao.domain.autodealer.AnnotationChild");
            boolean isAnnoExists = cls.isAnnotationPresent(Description2.class);
            if(isAnnoExists) {
                //拿到注解实例，解析类上面的注解
                Description2 desc = (Description2)cls.getAnnotation(Description2.class);
                System.out.println(desc.value());
            } else {
                System.out.println("not exists");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
