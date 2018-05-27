package com.swj.ics.json_util;


import com.google.common.base.Preconditions;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.util.VersionUtil;

import java.lang.annotation.Annotation;

import static com.google.common.base.Strings.isNullOrEmpty;
/**
 * Created by swj on 2017/2/8.
 */
    public class DimensionFieldSerializer extends JacksonAnnotationIntrospector
implements Versioned
{
    @Override
    public boolean isHandled(Annotation ann) {
        Class<?> clazz= ann.annotationType();
        if(clazz==Dimension.class)
        {
            return true;
        }
        return super.isHandled(ann);
    }

    @Override
    public String findSerializablePropertyName(AnnotatedField af) {
        return getPropertyName(af);
    }

    @Override
    public String findDeserializablePropertyName(AnnotatedField af) {
        return getPropertyName(af);
    }

    private String getPropertyName(AnnotatedField af)
    {
        Dimension dimension = af.getAnnotation(Dimension.class);
        if(dimension!=null)
        {
           String valueType= dimension.valueType();
            Preconditions.checkArgument(isNullOrEmpty(valueType),"@Dimension注解中的valueType不能为空");
            if(valueType.equalsIgnoreCase("id"))
            {
                return af.getName()+"_id";
            }
            else if(valueType.equalsIgnoreCase("code"))
            {
                return af.getName()+"_code";
            }
        }
        return af.getName();
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }
}
