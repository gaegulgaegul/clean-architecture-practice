package io.gaegul.buckpal.account.application.port.out;

import io.gaegul.buckpal.account.domain.Account.AccountId;

/**
 * 계좌 비관적 락 (Pessimistic Lock)
 */
public interface AccountLock {

	/**
	 * 계좌 락 동작 수행
	 * @param accountId 계좌 ID
	 */
	void lockAccount(AccountId accountId);

	/**
	 * 계좌 락 해제 동작 수행
	 * @param accountId 계좌 ID
	 */
	void releaseAccount(AccountId accountId);
}
