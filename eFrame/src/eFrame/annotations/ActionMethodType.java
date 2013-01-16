package eFrame.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明action类某个方法的返回类型
 * <br>
 * @date 2013-1-16
 * @author LiangRL
 * @alias E.E.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionMethodType {
	String template();
}
