package io.gaegul.buckpal.account.domain;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

/**
 * 계좌의 현재 스냅샷
 */
@AllArgsConstructor
public class Account {
	private final AccountId id;							/* 계좌 ID */
	@Getter
	private final Money baselineBalance;				/* 현재 계좌의 잔고 */
	@Getter
	private final ActivityWindow activityWindow;		/* 계좌의 모든 활동 정보 */

	/**
	 * 계좌 ID를 제외한 객체 생성 팩토리 메서드
	 */
	public static Account withoutId(Money baselineBalance, ActivityWindow activityWindow) {
		return new Account(null, baselineBalance, activityWindow);
	}

	/**
	 * 계좌 ID를 포함한 객체 생성 팩토리 메서드
	 */
	public static Account withId(AccountId accountId, Money baselineBalance, ActivityWindow activityWindow) {
		return new Account(accountId, baselineBalance, activityWindow);
	}

	public Optional<AccountId> getId(){
		return Optional.ofNullable(this.id);
	}

	/**
	 * 계좌 잔여 금액 반환
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
	public boolean withdraw(Money money, AccountId targetAccountId) {
		if (!mayWithdraw(money)) {
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
	private boolean mayWithdraw(Money money) {
		return Money.add(this.baselineBalance, money.negate())
			.isPositiveOrZero();
	}

	@Value
	public static class AccountId {
		private Long value;
	}
}
