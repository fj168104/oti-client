package com.mr.sac.oti;

import lombok.Getter;

@Getter
public enum DataType {
	STRING(1, "string"),
	INT(2, "int"),
	DOUBLE(3, "double"),
	OBJECT(4, "object"),
	ARRAY(5, "array"),
	BOOL(6, "bool");

	public int code;
	public String name;

	DataType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getNameByCode(int sCode) {
		for (DataType dataType : DataType.values()) {
			if (sCode == dataType.code)
				return dataType.name;
		}
		return DataType.STRING.name;
	}

	public static int getCodeByName(String name) {
		for (DataType dataType : DataType.values()) {
			if (dataType.name.equals(name)) {
				return dataType.code;
			}
		}
		return 1;
	}

	@Override
	public String toString() {
		return this.name;
	}
}