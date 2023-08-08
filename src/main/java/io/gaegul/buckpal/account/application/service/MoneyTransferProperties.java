package io.gaegul.buckpal.account.application.service;

import io.gaegul.buckpal.account.domain.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 금액 변환 설정 정보
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferProperties {

	private Money maximumTransferThreshold = Money.of(1_000_000);
}
