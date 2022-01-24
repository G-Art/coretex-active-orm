package com.coretex.build.parsers;

import org.apache.commons.lang3.StringUtils;

public class ParserUtils {

	public static String unquoteString(String originString){
		if(StringUtils.isBlank(originString)){
			return originString;
		}
		return originString.replaceAll("^'|'$|^\"|\"$", "");
	}
}
