package io.gaegul.buckpal.account.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import io.gaegul.buckpal.account.domain.Account;
import io.gaegul.buckpal.account.domain.Money;

@DisplayName("AccountPersistenceAdapter 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
@Import({AccountPersistenceAdapter.class, AccountMapper.class})
class AccountPersistenceAdapterTest {

	@Autowired
	private AccountPersistenceAdapter sut; // system under test

	@Autowired
	private ActivityRepository activityRepository;

	@Nested
	class loadAccount_메서드는 {

		@Nested
		class 올바른_파라미터를_전달하면 {

			@Test
			@Sql("AccountPersistenceAdapterTest.sql")
			void 성공한다() {
				final Account account = sut.loadAccount(
					new Account.AccountId(1L),
					LocalDateTime.of(2022, 8, 10, 0, 0)
				);

				assertThat(account.getActivityWindow().getActivities()).hasSize(2);
				assertThat(account.calculateBalance()).isEqualTo(Money.of(500));
			}
		}
	}
}