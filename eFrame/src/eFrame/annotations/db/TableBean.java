package eFrame.annotations.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明这个数据表的名称
 * <br>
 * @date 2012-12-27
 * @author LiangRL
 * @alias E.E.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableBean {
	public String name();
}
