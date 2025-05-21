package com.github.hanzm_10.murico.swingapp.lib.database.entity.sales;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;

public record WeeklyGross(@NotNull WeekDays weekDay, @NotNull BigDecimal gross) {
	public enum WeekDays {
		MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

		public static WeekDays fromString(String weekDay) {
			return switch (weekDay.toUpperCase()) {
			case "MONDAY" -> WeekDays.MONDAY;
			case "TUESDAY" -> WeekDays.TUESDAY;
			case "WEDNESDAY" -> WeekDays.WEDNESDAY;
			case "THURSDAY" -> WeekDays.THURSDAY;
			case "FRIDAY" -> WeekDays.FRIDAY;
			case "SATURDAY" -> WeekDays.SATURDAY;
			case "SUNDAY" -> WeekDays.SUNDAY;
			default -> throw new IllegalArgumentException("Invalid week day: " + weekDay);
			};
		}

		@Override
		public String toString() {
			return switch (this) {
			case MONDAY -> "Monday";
			case TUESDAY -> "Tuesday";
			case WEDNESDAY -> "Wednesday";
			case THURSDAY -> "Thursday";
			case FRIDAY -> "Friday";
			case SATURDAY -> "Saturday";
			case SUNDAY -> "Sunday";
			};
		}
	}
}
