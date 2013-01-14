package eFrame.annotations.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	/** 字段是主键 */
	public boolean isKey() default false;
	
	/** 字段的类型 */
	public ColumnType fieldType();
	
	/** 字段可否为空 */
	public boolean isNull() default false;
}
