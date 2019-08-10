package com.demo.withdrawal.util;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class ValidationUtilsTest {

	@Test
	void testValidateParams() {
		assertThrows(Throwable.class, () -> {
			ValidationUtils.validateParams(0, 0, "NA", "-1");
		});

		assertThrows(Throwable.class, () -> {
			ValidationUtils.validateParams(1, 0, "NA", "-1");
		});

		assertThrows(Throwable.class, () -> {
			ValidationUtils.validateParams(1, 1, "NA", "-1");
		});

		assertThrows(Throwable.class, () -> {
			ValidationUtils.validateParams(1, 1, "USD", "-1");
		});

		assertThrows(Throwable.class, () -> {
			ValidationUtils.validateParams(1, 1, "NA", "214.545564");
		});

		//TODO: more parameter permutations
	}

	@Test
	void testAssureAvailableBalance() {
		assertThrows(IllegalStateException.class, () -> {
			ValidationUtils.assureAvailableBalance(new BigDecimal("12.22"), new BigDecimal("12.23"));
		});

		assertDoesNotThrow(() -> {
			ValidationUtils.assureAvailableBalance(new BigDecimal("12.22"), new BigDecimal("12.22"));
		});

		assertDoesNotThrow(() -> {
			ValidationUtils.assureAvailableBalance(new BigDecimal("12.23"), new BigDecimal("12.22"));
		});

	}

}
