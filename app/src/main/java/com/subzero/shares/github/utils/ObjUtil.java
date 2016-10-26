package com.subzero.shares.github.utils;

import java.util.List;

public class ObjUtil
{
	/**List为空
	 * @param list 为null | 无元素，返回true*/
	public static boolean listIsEmpty(List<?> list){
		if(list == null || list.isEmpty()){
			return true;
		}
		return false;
	}
}
