package io.gaegul.buckpal.account.domain;

import static io.gaegul.buckpal.stub.ActivityStub.defaultActivity;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.gaegul.buckpal.account.domain.Account.AccountId;

@DisplayName("ActivityWindow 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ActivityWindowTest {

	@Nested
	class getStartTimestamp_메서드는 {

		@Nested
		class 특정_날짜의_거래활동을_전달하면 {

			@Test
			void 첫_거래일시를_반환() {
				final ActivityWindow window = new ActivityWindow(
					defaultActivity().withTimestamp(startDate()).build(),
					defaultActivity().withTimestamp(inBetweenDate()).build(),
					defaultActivity().withTimestamp(endDate()).build()
				);

				assertThat(window.getStartTimestamp()).isEqualTo(startDate());
			}
		}
	}

	@Nested
	class getEndTimestamp_메서드는 {

		@Nested
		class 특정_날짜의_거래활동을_전달하면 {

			@Test
			void 마지막_거래일시를_반환() {
				final ActivityWindow window = new ActivityWindow(
					defaultActivity().withTimestamp(startDate()).build(),
					defaultActivity().withTimestamp(inBetweenDate()).build(),
					defaultActivity().withTimestamp(endDate()).build()
				);

				assertThat(window.getEndTimestamp()).isEqualTo(endDate());
			}
		}
	}

	@Nested
	class calculateBalance_메서드는 {
		@Nested
		class 계좌_간_거래_활동을_전달하면 {

			@Test
			void 계좌별_잔액을_계산() {

				AccountId account1 = new AccountId(1L);
				AccountId account2 = new AccountId(2L);

				ActivityWindow window = new ActivityWindow(
					defaultActivity()
						.withSourceAccount(account1)
						.withTargetAccount(account2)
						.withMoney(Money.of(999)).build(),
					defaultActivity()
						.withSourceAccount(account1)
						.withTargetAccount(account2)
						.withMoney(Money.of(1)).build(),
					defaultActivity()
						.withSourceAccount(account2)
						.withTargetAccount(account1)
						.withMoney(Money.of(500)).build());

				assertThat(window.calculateBalance(account1)).isEqualTo(Money.of(-500));
				assertThat(window.calculateBalance(account2)).isEqualTo(Money.of(500));
			}
		}
	}


	private LocalDateTime startDate() {
		return LocalDateTime.of(2023, 8, 1, 0, 0);
	}

	private LocalDateTime inBetweenDate() {
		return LocalDateTime.of(2023, 8, 2, 0, 0);
	}

	private LocalDateTime endDate() {
		return LocalDateTime.of(2023, 8, 3, 0, 0);
	}

}