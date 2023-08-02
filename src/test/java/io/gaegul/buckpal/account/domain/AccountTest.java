package io.gaegul.buckpal.account.domain;

import static io.gaegul.buckpal.stub.AccountStub.defaultAccount;
import static io.gaegul.buckpal.stub.ActivityStub.defaultActivity;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.gaegul.buckpal.account.domain.Account.AccountId;
import io.gaegul.buckpal.stub.ActivityStub;

@DisplayName("Account 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AccountTest {
	private AccountId owner = new AccountId(1L);

	@Nested
	class calculateBalance_메서드는 {

		@Nested
		class 기본_잔액은_1000원이고{
			private Money baselineBalance = Money.of(1000L);

			@Nested
			class 출금_금액_1000원을_전달하면 {
				private ActivityWindow withdraw = new ActivityWindow(
					ActivityStub.defaultActivity()
						.withOwnerAccount(owner)
						.withSourceAccount(owner)
						.withMoney(Money.of(1000L))
						.build()
				);

				@Test
				void 잔액이_0원() {
					final Account account = defaultAccount()
						.withAccountId(owner)
						.withBaselineBalance(baselineBalance)
						.withActivityWindow(withdraw)
						.build();

					final Money balance = account.calculateBalance();

					assertThat(balance).isEqualTo(Money.of(0L));
				}
			}

			@Nested
			class 입금_금액_555원을_전달하면 {
				private ActivityWindow deposit = new ActivityWindow(
					defaultActivity()
						.withOwnerAccount(owner)
						.withTargetAccount(owner)
						.withMoney(Money.of(555L))
						.build()
				);

				@Test
				void 잔액이_1555원() {
					final Account account = defaultAccount()
						.withAccountId(owner)
						.withBaselineBalance(baselineBalance)
						.withActivityWindow(deposit)
						.build();

					final Money balance = account.calculateBalance();

					assertThat(balance).isEqualTo(Money.of(1555L));
				}

			}

		}
	}

	@Nested
	class withdraw_메서드는 {

		@Nested
		class 잔액에_초과되지_않은_금액을_출금하면 {

			@Test
			void 성공() {
				final Account account = getAccount();
				final boolean success = account.withdraw(Money.of(555L), new AccountId(99L));

				assertThat(success).isTrue();
				assertThat(account.getActivityWindow().getActivities()).hasSize(3);
				assertThat(account.calculateBalance()).isEqualTo(Money.of(1000L));
			}
		}

		@Nested
		class 잔액에_초과되는_금액을_출금하면 {

			@Test
			void 실패() {
				final Account account = getAccount();
				final boolean success = account.withdraw(Money.of(1556L), new AccountId(99L));

				assertThat(success).isFalse();
				assertThat(account.getActivityWindow().getActivities()).hasSize(2);
				assertThat(account.calculateBalance()).isEqualTo(Money.of(1555L));
			}
		}

		private Account getAccount() {
			return defaultAccount()
				.withAccountId(owner)
				.withBaselineBalance(Money.of(555L))
				.withActivityWindow(new ActivityWindow(
					defaultActivity()
						.withTargetAccount(owner)
						.withMoney(Money.of(999L)).build(),
					defaultActivity()
						.withTargetAccount(owner)
						.withMoney(Money.of(1L)).build()))
				.build();
		}
	}

	@Nested
	class deposit_메서드는 {

		@Nested
		class 잔액이_500원이고 {

			@Nested
			class 기존_입금_금액이_1000원일_때 {

				@Nested
				class 금액_500원을_입금하면 {

					@Test
					void 입금이_성공하고_총_잔액은_2000원() {
						Account account = defaultAccount()
							.withAccountId(owner)
							.withBaselineBalance(Money.of(500L))
							.withActivityWindow(new ActivityWindow(
								defaultActivity()
									.withTargetAccount(owner)
									.withMoney(Money.of(1000L)).build()))
							.build();

						boolean success = account.deposit(Money.of(500L), new AccountId(99L));

						assertThat(success).isTrue();
						assertThat(account.getActivityWindow().getActivities()).hasSize(2);
						assertThat(account.calculateBalance()).isEqualTo(Money.of(2000L));
					}
				}
			}
		}

	}
}