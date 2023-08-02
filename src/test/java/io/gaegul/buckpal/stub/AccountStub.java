package io.gaegul.buckpal.stub;

import io.gaegul.buckpal.account.domain.Account;
import io.gaegul.buckpal.account.domain.Account.AccountId;
import io.gaegul.buckpal.account.domain.ActivityWindow;
import io.gaegul.buckpal.account.domain.Money;

/**
 * 계좌 테스트 데이터 stub
 */
public class AccountStub {

	public static AccountBuilder defaultAccount() {
		return new AccountBuilder()
			.withAccountId(new AccountId(42L))
			.withBaselineBalance(Money.of(999L))
			.withActivityWindow(new ActivityWindow(
				ActivityStub.defaultActivity().build(),
				ActivityStub.defaultActivity().build()));
	}


	public static class AccountBuilder {

		private AccountId accountId;
		private Money baselineBalance;
		private ActivityWindow activityWindow;

		public AccountBuilder withAccountId(AccountId accountId) {
			this.accountId = accountId;
			return this;
		}

		public AccountBuilder withBaselineBalance(Money baselineBalance) {
			this.baselineBalance = baselineBalance;
			return this;
		}

		public AccountBuilder withActivityWindow(ActivityWindow activityWindow) {
			this.activityWindow = activityWindow;
			return this;
		}

		public Account build() {
			return Account.withId(this.accountId, this.baselineBalance, this.activityWindow);
		}

	}
}
