package io.gaegul.buckpal.account.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import io.gaegul.buckpal.account.application.port.in.GetAccountBalanceQuery;
import io.gaegul.buckpal.account.application.port.out.LoadAccountPort;
import io.gaegul.buckpal.account.domain.Account;
import io.gaegul.buckpal.account.domain.Money;
import lombok.RequiredArgsConstructor;

/**
 * 계좌 잔고 보여주기 동작 구현체
 */
@Service
@RequiredArgsConstructor
public class GetAccountBalanceService implements GetAccountBalanceQuery {
	private final LoadAccountPort loadAccountPort;

	@Override
	public Money getAccountBalance(Account.AccountId accountId) {
		return loadAccountPort.loadAccount(accountId, LocalDateTime.now())
			.calculateBalance();
	}
}
