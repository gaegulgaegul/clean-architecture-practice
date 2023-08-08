package io.gaegul.buckpal.account.application.service;

import io.gaegul.buckpal.account.domain.Money;

/**
 * 금액 임계값 초과 예외
 */
public class ThresholdExceededException extends RuntimeException {

	public ThresholdExceededException(final Money threshold, final Money actual) {
		super(String.format("Maximum threshold for transferring money exceeded: tried to transfer %s but threshold is %s!", actual, threshold));
	}
}
