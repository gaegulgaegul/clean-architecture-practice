package io.gaegul.buckpal.account.application.port.out;

import java.time.LocalDateTime;

import io.gaegul.buckpal.account.domain.Account;

/**
 * 계좌 정보를 조회하는 역할
 */
public interface LoadAccountPort {

	/**
	 * 계좌 조회
	 * @param accountId
	 * @param now
	 * @return
	 */
	Account loadAccount(Account.AccountId accountId, LocalDateTime now);
}
