package io.gaegul.buckpal.account.adapter.out.persistence;

import static io.gaegul.buckpal.account.domain.Activity.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import io.gaegul.buckpal.account.domain.Account;
import io.gaegul.buckpal.account.domain.Account.AccountId;
import io.gaegul.buckpal.account.domain.Activity;
import io.gaegul.buckpal.account.domain.ActivityWindow;
import io.gaegul.buckpal.account.domain.Money;

/**
 * 계좌 도메인 클래스 매핑 기능
 */
@Component
class AccountMapper {

	/**
	 * 계좌 영속성 데이터를 도메인으로 변환
	 * @param account 계좌
	 * @param activities 금융 거래 활동 내역
	 * @param withdrawalBalance 출금액
	 * @param depositBalance 입금액
	 * @return
	 */
	Account mapToDomainEntity(AccountJpaEntity account, List<ActivityJpaEntity> activities,
		final Long withdrawalBalance, Long depositBalance) {
		final Money baselineBalance = Money.subtract(
			Money.of(depositBalance),
			Money.of(withdrawalBalance)
		);

		return Account.withId(
			new AccountId(account.getId()),
			baselineBalance,
			mapToActivityWindow(activities)
		);
	}

	/**
	 * 금융 거래 내역 영속성 데이터를 도메인으로 변환
	 * @param activities 금융 거래 활동 내역
	 * @return
	 */
	ActivityWindow mapToActivityWindow(List<ActivityJpaEntity> activities) {
		final List<Activity> mappedActivities = new ArrayList<>();

		for (final ActivityJpaEntity activity : activities) {
			mappedActivities.add(
				new Activity(
					new ActivityId(activity.getId()),
					new AccountId(activity.getOwnerAccountId()),
					new AccountId(activity.getSourceAccountId()),
					new AccountId(activity.getTargetAccountId()),
					activity.getTimestamp(),
					Money.of(activity.getAmount())
				)
			);
		}

		return new ActivityWindow(mappedActivities);
	}

	ActivityJpaEntity mapToJpaEntity(Activity activity) {
		return new ActivityJpaEntity(
			activity.getId() == null ? null : activity.getId().getValue(),
			activity.getTimestamp(),
			activity.getOwnerAccountId().getValue(),
			activity.getSourceAccountId().getValue(),
			activity.getTargetAccountId().getValue(),
			activity.getMoney().getAmount().longValue());
	}
}
