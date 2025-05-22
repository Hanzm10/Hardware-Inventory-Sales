package com.github.hanzm_10.murico.swingapp.lib.comparators;

import java.math.BigDecimal;
import java.util.Comparator;

import com.github.hanzm_10.murico.swingapp.lib.table_renderers.ProgressLevelRenderer;

public class NumberWithSymbolsComparator implements Comparator<Object> {
	@Override
	public int compare(Object o1, Object o2) {

		BigDecimal num1 = toBigDecimal(o1);
		BigDecimal num2 = toBigDecimal(o2);

		return num1.compareTo(num2);
	}

	@Override
	public boolean equals(Object obj) {
		return this.equals(obj);
	}

	private BigDecimal toBigDecimal(Object obj) {
		if (obj == null) {
			return BigDecimal.ZERO;
		}

		if (obj instanceof Number) {
			return new BigDecimal(obj.toString());
		}

		if (obj instanceof String) {
			String str = (String) obj;
			str = str.trim();

			// Handle optional leading negative sign
			boolean isNegative = str.startsWith("-");
			str = str.replaceAll("[^\\d.]", "");

			if (isNegative) {
				str = "-" + str;
			}

			try {
				return new BigDecimal(str);
			} catch (NumberFormatException e) {
				return BigDecimal.ZERO;
			}
		}

		if (obj instanceof ProgressLevelRenderer.ProgressLevel) {
			ProgressLevelRenderer.ProgressLevel progressLevel = (ProgressLevelRenderer.ProgressLevel) obj;
			return new BigDecimal(progressLevel.currentProgressLevel());
		}

		return BigDecimal.ZERO;
	}

}
