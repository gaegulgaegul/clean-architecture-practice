package io.gaegul.buckpal.account.application.port.in;

import javax.validation.constraints.NotNull;

import io.gaegul.buckpal.account.domain.Account.AccountId;
import io.gaegul.buckpal.account.domain.Money;
import io.gaegul.buckpal.support.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 송금 입력 모델
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class SendMoneyCommand extends SelfValidating<SendMoneyCommand> {
	@NotNull
	private final AccountId sourceAccountId;
	@NotNull
	private final AccountId targetAccountId;
	@NotNull
	private final Money money;

	public SendMoneyCommand(AccountId sourceAccountId, AccountId targetAccountId, Money money) {
		this.sourceAccountId = sourceAccountId;
		this.targetAccountId = targetAccountId;
		this.money = money;

		/*
		 * 데이터 입력 전에 유효성 검증을 수행하면 모두 빈 값으로 확인된다.
		 * 따라서 데이터 바인딩 후 유효성 검증을 수행한다.
		 */
		this.validateSelf();
	}
}
