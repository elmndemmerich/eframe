package eFrame.db;

import eFrame.annotations.db.ColumnType;

/**
 * 用于实体类或者tableBean相关。
 * <br>
 * 标注一个字段的属性。说明字段的：名字，值，类型（int，long？等类型）其中类型用枚举。
 * @date 2013-1-4
 * @author LiangRL
 * @alias E.E.
 */
public class Param {

	private String fieldName;
	private Object value;
	private ColumnType columnType;	
	
	public Param(Object value, ColumnType fieldType) {
		super();
		this.value = value;
		this.columnType = fieldType;
	}
	public Param(String fieldName, Object value, ColumnType fieldtype) {
		super();
		this.fieldName = fieldName;
		this.value = value;
		this.columnType = fieldtype;
	}	
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}	
	public Object getValue() {
		return value;
	}
	public ColumnType getColumnType() {
		return columnType;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public void setColumnType(ColumnType fieldtype) {
		this.columnType = fieldtype;
	}
}
