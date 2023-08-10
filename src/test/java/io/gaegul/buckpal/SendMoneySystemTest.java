package io.gaegul.buckpal;

import static org.assertj.core.api.BDDAssertions.then;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import io.gaegul.buckpal.account.application.port.out.LoadAccountPort;
import io.gaegul.buckpal.account.domain.Account;
import io.gaegul.buckpal.account.domain.Account.AccountId;
import io.gaegul.buckpal.account.domain.Money;

@DisplayName("송금하기 시스템 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SendMoneySystemTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private LoadAccountPort loadAccountPort;

	AccountId sourceAccountId = new AccountId(1L);
	AccountId targetAccountId = new AccountId(2L);
	Money transferredAmount = Money.of(500L);

	@Test
	@Sql("SendMoneySystemTest.sql")
	void 송금_API에_HTTP_요청을_보내고_계좌_잔고를_확인한다() {
		final Money initialSourceBalance = sourceAccount().calculateBalance();
		final Money initialTargetBalance = targetAccount().calculateBalance();

		final ResponseEntity response = whenSendMoney(sourceAccountId, targetAccountId, transferredAmount);

		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(sourceAccount().calculateBalance()).isEqualTo(initialSourceBalance.minus(transferredAmount));
		then(targetAccount().calculateBalance()).isEqualTo(initialTargetBalance.plus(transferredAmount));
	}

	private Account sourceAccount() {
		return account(sourceAccountId);
	}

	private Account targetAccount() {
		return account(targetAccountId);
	}

	private Account account(final AccountId accountId) {
		return loadAccountPort.loadAccount(accountId, LocalDateTime.now());
	}

	private ResponseEntity whenSendMoney(final AccountId sourceAccountId, final AccountId targetAccountId, final Money amount) {
		final HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		final HttpEntity<Void> request = new HttpEntity<>(null, headers);
		return restTemplate.exchange(
			"/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
			HttpMethod.POST,
			request,
			Object.class,
			sourceAccountId.getValue(),
			targetAccountId.getValue(),
			amount.getAmount()
		);
	}
}
