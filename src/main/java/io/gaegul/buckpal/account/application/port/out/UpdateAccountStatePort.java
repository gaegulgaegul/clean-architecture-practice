package io.gaegul.buckpal.account.application.port.out;

import io.gaegul.buckpal.account.domain.Account;

/**
 * 계좌 상태를 갱신하는 역할
 */
public interface UpdateAccountStatePort {

	/**
	 * 계좌의 거래 활동 내역 갱신
	 * @param account 계좌
	 */
	void updateActivities(Account account);
}
