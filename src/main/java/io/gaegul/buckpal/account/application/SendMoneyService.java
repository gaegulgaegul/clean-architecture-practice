package io.gaegul.buckpal.account.application;

import javax.transaction.Transactional;

import io.gaegul.buckpal.account.application.port.in.SendMoneyCommand;
import io.gaegul.buckpal.account.application.port.in.SendMoneyUsecase;
import io.gaegul.buckpal.account.application.port.out.LoadAccountPort;
import io.gaegul.buckpal.account.application.port.out.UpdateAccountStatePort;
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
		// TODO: 모델 상태 조작
		// TODO: 출력 값 변환
		return false;
	}
}
