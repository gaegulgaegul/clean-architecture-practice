package io.gaegul.buckpal.account.application.service;

import javax.transaction.Transactional;

import io.gaegul.buckpal.account.application.port.in.SendMoneyCommand;
import io.gaegul.buckpal.account.application.port.in.SendMoneyUsecase;
import io.gaegul.buckpal.account.application.port.out.LoadAccountPort;
import io.gaegul.buckpal.account.application.port.out.UpdateAccountStatePort;
import io.gaegul.buckpal.account.domain.Account.AccountId;
import lombok.RequiredArgsConstructor;

/**
 * 송금 동작 구현체
 */
@RequiredArgsConstructor
@Transactional
public class SendMoneyService implements SendMoneyUsecase {
	private final LoadAccountPort loadAccountPort;
	private final UpdateAccountStatePort updateAccountStatePort;

	@Override
	public boolean sendMoney(SendMoneyCommand command) {
		// TODO: 비즈니스 규칙 검증
		requireAccountExists(command.getSourceAccountId());
		requireAccountExists(command.getTargetAccountId());

		// TODO: 모델 상태 조작
		// TODO: 출력 값 변환
		return false;
	}

	/**
	 * 존재하는 계좌인가?
	 * @param accountId
	 */
	private void requireAccountExists(AccountId accountId) {
		// 계좌가 DB에 존재하는지 확인하는 비즈니스 유효성 검사
	}
}
