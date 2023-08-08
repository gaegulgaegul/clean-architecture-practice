package io.gaegul.buckpal.account.application.service;

import org.springframework.stereotype.Component;

import io.gaegul.buckpal.account.application.port.out.AccountLock;
import io.gaegul.buckpal.account.domain.Account;

/**
 * 계좌 잠금 명령
 */
@Component
public class NoOpAccountLock implements AccountLock {

	@Override
	public void lockAccount(Account.AccountId accountId) {
		// do noting
	}

	@Override
	public void releaseAccount(Account.AccountId accountId) {
		// do noting
	}
}
