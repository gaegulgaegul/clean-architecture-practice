package io.gaegul.buckpal.account.application.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import io.gaegul.buckpal.account.application.port.in.SendMoneyCommand;
import io.gaegul.buckpal.account.application.port.in.SendMoneyUsecase;
import io.gaegul.buckpal.account.application.port.out.AccountLock;
import io.gaegul.buckpal.account.application.port.out.LoadAccountPort;
import io.gaegul.buckpal.account.application.port.out.UpdateAccountStatePort;
import io.gaegul.buckpal.account.domain.Account;
import io.gaegul.buckpal.account.domain.Account.AccountId;
import lombok.RequiredArgsConstructor;

/**
 * 송금 동작 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SendMoneyService implements SendMoneyUsecase {
	private final LoadAccountPort loadAccountPort;
	private final UpdateAccountStatePort updateAccountStatePort;
	private final AccountLock accountLock;
	private final MoneyTransferProperties moneyTransferProperties;

	@Override
	public boolean sendMoney(SendMoneyCommand command) {
		/* 비즈니스 규칙 검증: 송금 금액 임계값 초과 여부 확인 */
		checkThreshold(command);

		/* 모델 상태 조작 */
		final LocalDateTime baselineDate = LocalDateTime.now().minusDays(10L);

		final Account sourceAccount = loadAccountPort.loadAccount(command.getSourceAccountId(), baselineDate);
		final Account targetAccount = loadAccountPort.loadAccount(command.getTargetAccountId(), baselineDate);

		final AccountId sourceAccountId = sourceAccount.getId()
			.orElseThrow(() -> new IllegalArgumentException("expected source account ID not to be empty"));
		final AccountId targetAccountId = targetAccount.getId()
			.orElseThrow(() -> new IllegalArgumentException("expected target account ID not to be empty"));

		/* 송신 계좌에 락을 건다. */
		accountLock.lockAccount(sourceAccountId);

		/* 송금 출금을 수행하고 성공 여부를 확인한다. */
		if (!sourceAccount.withdraw(command.getMoney(), targetAccountId)) {

			/* 출금을 실패하면, 송신 계좌에 락을 해제한다. */
			accountLock.releaseAccount(sourceAccountId);

			/* 실패 결과 값을 반환한다. */
			return false;
		}

		/* 수신 계좌에 락을 건다. */
		accountLock.lockAccount(targetAccountId);

		/* 송금 입금을 수행하고 성공 여부를 확인한다. */
		if (!targetAccount.deposit(command.getMoney(), sourceAccountId)) {

			/* 입금을 실패하면, 송신 계좌/수신 계좌에 락을 해제한다. */
			accountLock.releaseAccount(sourceAccountId);
			accountLock.releaseAccount(targetAccountId);

			/* 실패 결과 값을 반환한다. */
			return false;
		}

		/* 송금 입금/출금 정보가 반영된 계좌 정보를 갱신한다. */
		updateAccountStatePort.updateActivities(sourceAccount);
		updateAccountStatePort.updateActivities(targetAccount);

		/* 송신 계좌/수신 계좌에 락을 해제한다. */
		accountLock.releaseAccount(sourceAccountId);
		accountLock.releaseAccount(targetAccountId);

		/* 성공 결과 값을 반환한다. */
		return true;
	}

	/**
	 * 송금 금액이 임계값을 초과하는가?
	 * @param command 송금 입력 모델
	 */
	private void checkThreshold(final SendMoneyCommand command) {
		if (command.getMoney().isGreaterThan(moneyTransferProperties.getMaximumTransferThreshold())) {
			throw new ThresholdExceededException(moneyTransferProperties.getMaximumTransferThreshold(), command.getMoney());
		}
	}

}
