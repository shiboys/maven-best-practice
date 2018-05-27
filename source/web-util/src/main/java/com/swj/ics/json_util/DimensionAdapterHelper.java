package com.swj.ics.json_util;

import com.google.common.base.Throwables;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by swj on 2017/2/9.
 */
public class DimensionAdapterHelper {

    private final static ObjectMapper objectMapper=new ObjectMapper();

    static
    {
        AnnotationIntrospector annotationIntrospector=new DimensionFieldSerializer();
        objectMapper.setAnnotationIntrospector(annotationIntrospector);
    }

    public static String beanToJson(Object object)
    {
        StringWriter stringWriter=new StringWriter();
        try {
            objectMapper.writeValue(stringWriter,object);
            return stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw Throwables.propagate(e);
        }
    }

    public static  <T> T jsonToBean(String json,Class<T> cls)
    {
        try {
            return (T)objectMapper.readValue(json,cls);
        } catch (IOException e) {
            e.printStackTrace();
            throw Throwables.propagate(e);
        }
    }


    public static class Type {

        private String code;
        @Dimension(valueType = "id")
        private String description;

        @Dimension(valueType = "code")
        private String value;

        public Type() {
        }

        public Type(String code, String description, String value) {
            this.code = code;
            this.description = description;
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.getClass().getName()+"@"+hashCode()+"[code="+
                    code+",description="+description+",value="+value
                    +"]";
        }
    }

    public static void main(String[] args)
    {
        Type type=new Type("a","b","c");
        String serializedValue=beanToJson(type);
        System.out.println(serializedValue);
        Type tt=jsonToBean(serializedValue,Type.class);
        System.out.println(tt);
    }



}

