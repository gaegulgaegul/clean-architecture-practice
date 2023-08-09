package io.gaegul.buckpal.account.application.service;

import io.gaegul.buckpal.account.application.port.in.SendMoneyCommand;
import io.gaegul.buckpal.account.application.port.in.SendMoneyUsecase;
import io.gaegul.buckpal.account.application.port.out.AccountLock;
import io.gaegul.buckpal.account.application.port.out.LoadAccountPort;
import io.gaegul.buckpal.account.application.port.out.UpdateAccountStatePort;
import io.gaegul.buckpal.account.domain.Account;
import io.gaegul.buckpal.account.domain.Account.AccountId;
import io.gaegul.buckpal.account.domain.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@DisplayName("SendMoneyService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SendMoneyServiceTest {

    private final LoadAccountPort loadAccountPort = mock(LoadAccountPort.class);
    private final UpdateAccountStatePort updateAccountStatePort = mock(UpdateAccountStatePort.class);
    private final AccountLock accountLock = mock(AccountLock.class);

    private SendMoneyUsecase sendMoneyUsecase;

    @BeforeEach
    void setUp() {
        this.sendMoneyUsecase = new SendMoneyService(
                loadAccountPort,
                updateAccountStatePort,
                accountLock,
                new MoneyTransferProperties(Money.of(Long.MAX_VALUE))
        );
    }

    @Nested
    class sendMoney_메서드는 {

        @Nested
        class 송금_출금에_실패하면 {

            @Test
            void 송신계좌만_락_수행하고_수정없이_실패_결과값을_반환() {
                final Account sourceAccount = givenAnAccountWithId(41L);
                final Account targetAccount = givenAnAccountWithId(42L);

                givenWithdrawalWillFail(sourceAccount, targetAccount);

                SendMoneyCommand command = new SendMoneyCommand(
                        sourceAccount.getId().get(),
                        targetAccount.getId().get(),
                        Money.of(500L)
                );
                boolean result = sendMoneyUsecase.sendMoney(command);

                assertThat(result).isFalse();
                thenLockedOnlySourceAccount(sourceAccount, targetAccount);
                thenUpdateWillNot(sourceAccount, targetAccount);
            }

            private void thenLockedOnlySourceAccount(Account sourceAccount, Account targetAccount) {
                then(accountLock).should().lockAccount(eq(sourceAccount.getId().get()));
                then(accountLock).should().releaseAccount(eq(sourceAccount.getId().get()));
                then(accountLock).should(times(0)).lockAccount(eq(targetAccount.getId().get()));
            }

            private void thenUpdateWillNot(Account... accounts) {
                for (Account account : accounts) {
                    then(updateAccountStatePort).should(times(0))
                            .updateActivities(account);
                }
            }

            private void givenWithdrawalWillFail(Account sourceAccount, Account targetAccount) {
                given(sourceAccount.withdraw(any(Money.class), any(AccountId.class)))
                        .willReturn(false);
                given(targetAccount.deposit(any(Money.class), any(AccountId.class)))
                        .willReturn(true);
            }
        }

        @Nested
        class 송금_입금에_실패하면 {

            @Test
            void 송수신계좌에_락_수행하고_수정없이_실패_결과값을_반환() {
                final Account sourceAccount = givenAnAccountWithId(41L);
                final Account targetAccount = givenAnAccountWithId(42L);

                givenDepositWillFail(sourceAccount, targetAccount);

                SendMoneyCommand command = new SendMoneyCommand(
                        sourceAccount.getId().get(),
                        targetAccount.getId().get(),
                        Money.of(500L)
                );
                boolean result = sendMoneyUsecase.sendMoney(command);

                assertThat(result).isFalse();
                thenLockedSourceAndTarget(sourceAccount, targetAccount);
                thenUpdateWillNot(sourceAccount, targetAccount);
            }

            private void thenLockedSourceAndTarget(Account sourceAccount, Account targetAccount) {
                then(accountLock).should().lockAccount(eq(sourceAccount.getId().get()));
                then(accountLock).should().releaseAccount(eq(sourceAccount.getId().get()));
                then(accountLock).should().lockAccount(eq(targetAccount.getId().get()));
                then(accountLock).should().releaseAccount(eq(targetAccount.getId().get()));
            }

            private void thenUpdateWillNot(Account... accounts) {
                for (Account account : accounts) {
                    then(updateAccountStatePort).should(times(0))
                            .updateActivities(account);
                }
            }

            private void givenDepositWillFail(Account sourceAccount, Account targetAccount) {
                given(sourceAccount.withdraw(any(Money.class), any(AccountId.class)))
                        .willReturn(true);
                given(targetAccount.deposit(any(Money.class), any(AccountId.class)))
                        .willReturn(false);
            }
        }

        @Nested
        class 송금에_성공하면 {

            @Test
            void 송수신계좌에_락_수행하고_수정_후_성공_결과값을_반환() {
                final Account sourceAccount = givenAnAccountWithId(41L);
                final Account targetAccount = givenAnAccountWithId(42L);

                givenSendMoneyWillSuccess(sourceAccount, targetAccount);

                SendMoneyCommand command = new SendMoneyCommand(
                        sourceAccount.getId().get(),
                        targetAccount.getId().get(),
                        Money.of(500L)
                );
                boolean result = sendMoneyUsecase.sendMoney(command);

                assertThat(result).isTrue();
                thenLockedSourceAndTarget(sourceAccount, targetAccount);
                thenUpdateSourceAndTarget(sourceAccount, targetAccount);
            }

            private void thenLockedSourceAndTarget(Account sourceAccount, Account targetAccount) {
                then(accountLock).should().lockAccount(eq(sourceAccount.getId().get()));
                then(accountLock).should().releaseAccount(eq(sourceAccount.getId().get()));
                then(accountLock).should().lockAccount(eq(targetAccount.getId().get()));
                then(accountLock).should().releaseAccount(eq(targetAccount.getId().get()));
            }

            private void thenUpdateSourceAndTarget(Account... accounts) {
                final ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
                then(updateAccountStatePort).should(times(accounts.length))
                        .updateActivities(argumentCaptor.capture());

                final List<AccountId> updatedAccountIds = argumentCaptor.getAllValues().stream()
                        .map(Account::getId)
                        .map(Optional::get)
                        .collect(Collectors.toList());
                for (final Account account : accounts) {
                    assertThat(updatedAccountIds).contains(account.getId().get());
                }
            }

            private void givenSendMoneyWillSuccess(Account sourceAccount, Account targetAccount) {
                given(sourceAccount.withdraw(any(Money.class), any(AccountId.class)))
                        .willReturn(true);
                given(targetAccount.deposit(any(Money.class), any(AccountId.class)))
                        .willReturn(true);
            }
        }

        private Account givenAnAccountWithId(final Long id) {
            final Account account = mock(Account.class);
            given(account.getId()).willReturn(Optional.of(new AccountId(id)));
            given(loadAccountPort.loadAccount(eq(account.getId().get()), any(LocalDateTime.class)))
                    .willReturn(account);
            return account;
        }

    }
}