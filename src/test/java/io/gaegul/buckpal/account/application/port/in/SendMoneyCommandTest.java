package io.gaegul.buckpal.account.application.port.in;

import io.gaegul.buckpal.account.domain.Account.AccountId;
import io.gaegul.buckpal.account.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SendMoneyCommand 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SendMoneyCommandTest {

    @Nested
    class 생성자는 {

        @Nested
        class 빈_값을_입력하면 {

            @Test
            void 유효성_검증_규칙에_의해_예외가_발생한다() {
                assertThatThrownBy(() -> new SendMoneyCommand(null, null, null))
                        .isInstanceOf(ConstraintViolationException.class);
                assertThatThrownBy(() -> new SendMoneyCommand(new AccountId(1L), new AccountId(2L), null))
                        .isInstanceOf(ConstraintViolationException.class);
            }
        }

        @Nested
        class 모든_값을_입력하면 {

            @Test
            void 객체가_생성되어_성공한다() {
                final SendMoneyCommand command = new SendMoneyCommand(
                        new AccountId(1L),
                        new AccountId(2L),
                        new Money(BigInteger.ONE)
                );
                assertThat(command).isNotNull();
            }
        }
    }

}