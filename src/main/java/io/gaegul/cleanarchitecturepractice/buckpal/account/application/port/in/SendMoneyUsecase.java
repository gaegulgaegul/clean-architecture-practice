package io.gaegul.cleanarchitecturepractice.buckpal.account.application.port.in;

/**
 * 송금하기
 */
public interface SendMoneyUsecase {

	/**
	 * 송금
	 * @param command
	 * @return
	 */
	boolean sendMoney(SendMoneyCommand command);
}
