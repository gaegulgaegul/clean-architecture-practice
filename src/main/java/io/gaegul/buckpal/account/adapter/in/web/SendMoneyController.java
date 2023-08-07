package io.gaegul.buckpal.account.adapter.in.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gaegul.buckpal.account.application.port.in.SendMoneyCommand;
import io.gaegul.buckpal.account.application.port.in.SendMoneyUsecase;
import io.gaegul.buckpal.account.domain.Account.AccountId;
import io.gaegul.buckpal.account.domain.Money;
import lombok.RequiredArgsConstructor;

/**
 * SendMoneyController
 *  - 송금 API
 * @author lim myeongseop
 */
@RestController
@RequiredArgsConstructor
class SendMoneyController {

    private final SendMoneyUsecase sendMoneyUsecase;

    /**
     * 송금
     * @param sourceAccountId 송신 계좌 ID
     * @param targetAccountId 수신 계좌 ID
     * @param amount 금액
     */
    @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    void sendMoney(@PathVariable("sourceAccountId") Long sourceAccountId,
                   @PathVariable("targetAccountId") Long targetAccountId,
                   @PathVariable("amount") Long amount) {
        final SendMoneyCommand command = new SendMoneyCommand(
                new AccountId(sourceAccountId),
                new AccountId(targetAccountId),
                Money.of(amount)
        );
        sendMoneyUsecase.sendMoney(command);
    }
}
