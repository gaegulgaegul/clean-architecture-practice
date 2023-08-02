package io.gaegul.buckpal.account.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * 계좌의 현재 스냅샷
 */
@AllArgsConstructor
public class Account {
	private final AccountId id;							/* 계좌 ID */
	private final Money baselineBalance;				/* 현재 계좌의 잔고 */
	private final ActivityWindow activityWindow;		/* 계좌의 모든 활동 정보 */

	/**
	 * 금액 합계 반환
	 * @return
	 */
	public Money calculateBalance() {
		return Money.add(
			this.baselineBalance,
			this.activityWindow.calculateBalance(this.id)
		);
	}

	/**
	 * 입금
	 * @param money
	 * @param sourceAccountId
	 * @return
	 */
	public boolean deposit(Money money, AccountId sourceAccountId) {
		Activity deposit = new Activity(
			this.id,
			sourceAccountId,
			this.id,
			LocalDateTime.now(),
			money
		);
		this.activityWindow.addActivity(deposit);
		return true;
	}

	/**
	 * 출금
	 * @param money
	 * @param targetAccountId
	 * @return
	 */
	public boolean withdrawal(Money money, AccountId targetAccountId) {
		if (!mayWithdrawal(money)) {
			return false;
		}

		Activity withdrawal = new Activity(
			this.id,
			this.id,
			targetAccountId,
			LocalDateTime.now(),
			money
		);
		this.activityWindow.addActivity(withdrawal);
		return true;
	}

	/**
	 * 출금 가능 여부 확인, 출금 계좌는 초과 인출되면 안 된다.
	 * @param money
	 * @return
	 */
	private boolean mayWithdrawal(Money money) {
		return Money.add(this.baselineBalance, money.negate())
			.isPositive();
	}

	@Value
	public static class AccountId {
		private Long value;
	}
}
