package io.gaegul.buckpal.account.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 금융 거래 활동 저장소
 */
interface ActivityRepository extends JpaRepository<ActivityJpaEntity, Long> {

	/**
	 * 시작 기준 일시부터 소유자 계좌의 거래 활동 목록을 반환한다.
	 * @param ownerAccountId 소유자 계좌 ID
	 * @param since 시작 기준 일시
	 * @return
	 */
	@Query("select a from ActivityJpaEntity a"
		+ " where a.ownerAccountId = :ownerAccountId"
		+ " and a.timestamp >= :since")
	List<ActivityJpaEntity> findByOwnerSince(@Param("ownerAccountId") Long ownerAccountId, @Param("since") LocalDateTime since);

	/**
	 * 종료 기준 일시까지 소유자 계좌의 입금 합계 금액을 반환한다.
	 * @param accountId 계좌 ID
	 * @param until 종료 기준 일시
	 * @return
	 */
	@Query("select sum(a.amount) from ActivityJpaEntity a"
		+ " where a.targetAccountId = :accountId"
		+ " and a.ownerAccountId = :accountId"
		+ " and a.timestamp < :until")
	Long getDepositBalanceUntil(@Param("accountId") Long accountId, @Param("until") LocalDateTime until);

	/**
	 * 종료 기준 일시까지 소유자 계좌의 출금 합계 금액을 반환한다.
	 * @param accountId 계좌 ID
	 * @param until 종료 기준 일시
	 * @return
	 */
	@Query("select sum(a.amount) from ActivityJpaEntity a"
		+ " where a.sourceAccountId = :accountId"
		+ " and a.ownerAccountId = :accountId"
		+ " and a.timestamp < :until")
	Long getWithdrawalBalanceUntil(@Param("accountId") Long accountId, @Param("until") LocalDateTime until);
}
