package io.gaegul.buckpal.account.application.port.out;

import io.gaegul.buckpal.account.domain.Account.AccountId;

/**
 * 계좌 잠금
 */
public interface AccountLock {

	/**
	 * 계좌 잠금 동작
	 * @param accountId 계좌 ID
	 */
	void lockAccount(AccountId accountId);

	/**
	 * 계좌 잠금 해제 동작
	 * @param accountId
	 */
	void releaseAccount(AccountId accountId);
}
