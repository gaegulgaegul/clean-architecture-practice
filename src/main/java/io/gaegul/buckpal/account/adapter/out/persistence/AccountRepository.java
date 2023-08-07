package io.gaegul.buckpal.account.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 계좌 DB 저장소
 */
interface AccountRepository extends JpaRepository<AccountJpaEntity, Long> {
}
