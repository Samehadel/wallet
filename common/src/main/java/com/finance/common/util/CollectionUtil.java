package com.finance.common.util;

import java.util.Collection;

public class CollectionUtil {
	public static boolean isNullOrEmpty(Collection array) {
		return array == null || array.size() == 0;
	}
}
