package eFrame.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActionBean{
	/** bean Name */
	public String name();
	
	/** 
	 * 返回类型:
	 * 1:页面；
	 * 2:json;
	 * 3:xml
	 * 4:文件(流)
	 * */
	public ActionType resultType() default ActionType.page;
	
	public String responseCharset() default "UTF-8";
}
