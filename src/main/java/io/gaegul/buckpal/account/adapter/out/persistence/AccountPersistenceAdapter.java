package io.gaegul.buckpal.account.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import io.gaegul.buckpal.account.application.port.out.LoadAccountPort;
import io.gaegul.buckpal.account.application.port.out.UpdateAccountStatePort;
import io.gaegul.buckpal.account.domain.Account;
import io.gaegul.buckpal.account.domain.Account.AccountId;
import io.gaegul.buckpal.account.domain.Activity;
import lombok.RequiredArgsConstructor;

/**
 * 계좌 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort {
	private final AccountRepository accountRepository;
	private final ActivityRepository activityRepository;
	private final AccountMapper accountMapper;

	@Override
	public Account loadAccount(AccountId accountId, LocalDateTime baselineDate) {
		final AccountJpaEntity account = accountRepository.findById(accountId.getValue())
			.orElseThrow(EntityNotFoundException::new);

		final List<ActivityJpaEntity> activities = activityRepository.findByOwnerSince(accountId.getValue(), baselineDate);

		final Long withdrawalBalance = orZero(activityRepository.getWithdrawalBalanceUntil(accountId.getValue(), baselineDate));

		final Long depositBalance = orZero(activityRepository.getDepositBalanceUntil(accountId.getValue(), baselineDate));

		return accountMapper.mapToDomainEntity(
			account,
			activities,
			withdrawalBalance,
			depositBalance
		);
	}

	@Override
	public void updateActivities(Account account) {
		for (final Activity activity : account.getActivityWindow().getActivities()) {
			if (activity.getId() == null) {
				activityRepository.save(accountMapper.mapToJpaEntity(activity));
			}
		}
	}

	/**
	 * null인 경우 기본값(0) 반환
	 * @param value 숫자 값
	 * @return
	 */
	private Long orZero(final Long value) {
		return value == null ? 0L : value;
	}
}
