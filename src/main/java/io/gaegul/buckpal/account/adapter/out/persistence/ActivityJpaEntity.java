package io.gaegul.buckpal.account.adapter.out.persistence;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 금융 거래 활동 DB 관리 엔티티
 */
@Entity
@Table(name = "activity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityJpaEntity {

	@Id
	@GeneratedValue
	public Long id;							// ID

	@Column
	private LocalDateTime timestamp;		// 거래 일시

	@Column
	private Long ownerAccountId;			// 소유주 계좌 ID

	@Column
	private Long sourceAccountId;			// 송신 계좌 ID

	@Column
	private Long targetAccountId;			// 수신 계좌 ID

	@Column
	private Long amount;					// 금액
}
