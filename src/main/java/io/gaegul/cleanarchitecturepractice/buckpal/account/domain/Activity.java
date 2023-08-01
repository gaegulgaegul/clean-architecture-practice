package io.gaegul.cleanarchitecturepractice.buckpal.account.domain;

import java.time.LocalDateTime;

import io.gaegul.cleanarchitecturepractice.buckpal.account.domain.Account.AccountId;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 한 계좌의 모든 입금/출금 관리
 */
@Value
@Getter
@RequiredArgsConstructor
public class Activity {


	private ActivityId id;

	/**
	 * 거래 주체 계좌 ID
	 */
	@NonNull
	private final AccountId ownerAccountId;

	/**
	 * 송금 계좌 ID
	 */
	@NonNull
	private final AccountId sourceAccountId;

	/**
	 * 수신 계좌 ID
	 */
	@NonNull
	private final AccountId targetAccountId;

	/**
	 * 금융 거래 활동 일시
	 */
	@NonNull
	private final LocalDateTime timestamp;

	/**
	 * 금융 거래 금액
	 */
	@NonNull
	private final Money money;

	public Activity(
		@NonNull AccountId ownerAccountId,
		@NonNull AccountId sourceAccountId,
		@NonNull AccountId targetAccountId,
		@NonNull LocalDateTime timestamp,
		@NonNull Money money) {
		this.id = null;
		this.ownerAccountId = ownerAccountId;
		this.sourceAccountId = sourceAccountId;
		this.targetAccountId = targetAccountId;
		this.timestamp = timestamp;
		this.money = money;
	}

	@Value
	public static class ActivityId {
		private final Long value;
	}
}
