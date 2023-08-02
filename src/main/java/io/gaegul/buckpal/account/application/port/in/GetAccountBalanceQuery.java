package io.gaegul.buckpal.account.application.port.in;

import io.gaegul.buckpal.account.domain.Account.AccountId;
import io.gaegul.buckpal.account.domain.Money;

/**
 * 계좌 잔고 보여주기
 */
public interface GetAccountBalanceQuery {

	/**
	 * 계좌 잔고 조회
	 * @param accountId
	 * @return
	 */
	Money getAccountBalance(AccountId accountId);
}
