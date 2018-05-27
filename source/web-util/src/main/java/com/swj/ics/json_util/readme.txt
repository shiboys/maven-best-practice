我们先来看看把bean转为json，一般没有特殊要求的话，我们都是:

/**
 * Object可以是POJO，也可以是Collection或数组。
 * 如果对象为Null, 返回"null".
 * 如果集合为空集合, 返回"[]".
 *
 * @param object the object to json
 * @return toJson result
 */
public String toJson(Object object) {
    try {
        return mapper.writeValueAsString(object);
    } catch (IOException e) {
        LOGGER.error("write to json string error:" + object, e);
        return null;
    }
}

这种是默认的情况，生成的json的key和对应的Bean的filed的name是一模一样的。

而Jackson也给我们提供了注解：@JsonProperty注解来帮助我们重命名生成的json的key。
但是他这个重命名并不是很灵活，因为他只能固定的重命名为某一个「确定的」值，
而不能容许我们做一些额外的操作。

所以在这种情况下，我打算自定义一个注解，因为业务场景相关，我们的注解定义如下：

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Dimension {
    String valueType();
}

假设我们的json的key的生成规则如下：

valueType()的值为“id”时，json key追加后缀“_id”

valueType()的值为”code”时，json key追加后缀“_code”

这个时候我们就可以使用Jackson提供给我们强大的JacksonAnnotationIntrospector类了。