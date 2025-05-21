package com.github.hanzm_10.murico.swingapp.lib.database.entity.sales;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;

public record MonthlyGross(@NotNull Month month, @NotNull BigDecimal gross) {

	public enum Month {
		JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER;

		public static Month fromString(String month) {
			return switch (month.toUpperCase()) {
			case "JANUARY" -> Month.JANUARY;
			case "FEBRUARY" -> Month.FEBRUARY;
			case "MARCH" -> Month.MARCH;
			case "APRIL" -> Month.APRIL;
			case "MAY" -> Month.MAY;
			case "JUNE" -> Month.JUNE;
			case "JULY" -> Month.JULY;
			case "AUGUST" -> Month.AUGUST;
			case "SEPTEMBER" -> Month.SEPTEMBER;
			case "OCTOBER" -> Month.OCTOBER;
			case "NOVEMBER" -> Month.NOVEMBER;
			case "DECEMBER" -> Month.DECEMBER;
			default -> throw new IllegalArgumentException("Invalid month: " + month);
			};
		}

		@Override
		public String toString() {
			return switch (this) {
			case JANUARY -> "January";
			case FEBRUARY -> "February";
			case MARCH -> "March";
			case APRIL -> "April";
			case MAY -> "May";
			case JUNE -> "June";
			case JULY -> "July";
			case AUGUST -> "August";
			case SEPTEMBER -> "September";
			case OCTOBER -> "October";
			case NOVEMBER -> "November";
			case DECEMBER -> "December";
			};
		}
	}
}
