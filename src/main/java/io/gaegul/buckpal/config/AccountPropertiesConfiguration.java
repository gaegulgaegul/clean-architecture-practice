package io.gaegul.buckpal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.gaegul.buckpal.account.application.service.MoneyTransferProperties;

/**
 * 계좌 송금 프로퍼티 설정
 */
@Configuration
public class AccountPropertiesConfiguration {

	/**
	 * 금액 변환 설정
	 * @return
	 */
	@Bean
	public MoneyTransferProperties moneyTransferProperties() {
		return new MoneyTransferProperties();
	}
}
