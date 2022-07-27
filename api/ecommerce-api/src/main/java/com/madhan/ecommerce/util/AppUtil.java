package com.madhan.ecommerce.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class AppUtil {

	public static final String SUCCESS = "success";

	public static final String FAILURE = "failure";

	public static final String AUTHORIZATION = "Authorization";

	public static Predicate<String> validate = value -> value == null || value.isEmpty();

	public static LocalDateTime getDateTime() {
		Instant nowUtc = Instant.now();
		ZoneId india = ZoneId.of("Asia/Kolkata");
		ZonedDateTime zonetime = ZonedDateTime.ofInstant(nowUtc, india);
		return zonetime.toLocalDateTime();
	}

	public static final Pattern VALID_EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

}
