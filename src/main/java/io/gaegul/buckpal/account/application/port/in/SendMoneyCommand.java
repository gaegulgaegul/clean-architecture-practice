package io.gaegul.buckpal.account.application.port.in;

import io.gaegul.buckpal.account.domain.Money;
import io.gaegul.buckpal.support.SelfValidating;
import lombok.Getter;

import javax.validation.constraints.NotNull;

import static io.gaegul.buckpal.account.domain.Account.AccountId;

/**
 * 송금 입력 모델
 */
@Getter
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
         * SendMoneyCommand 클래스의 모든 필드는 final로 선언되어,
         * 먼저 값을 세팅해놓고 유효성 검증 규칙을 위반하는지 확인한다.
         */
        this.validateSelf();
    }
}
