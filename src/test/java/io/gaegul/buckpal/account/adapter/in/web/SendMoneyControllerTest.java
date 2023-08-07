package io.gaegul.buckpal.account.adapter.in.web;

import static io.gaegul.buckpal.account.domain.Account.AccountId;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.gaegul.buckpal.account.application.port.in.SendMoneyCommand;
import io.gaegul.buckpal.account.application.port.in.SendMoneyUsecase;
import io.gaegul.buckpal.account.domain.Money;

@DisplayName("SendMoneyController 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(controllers = SendMoneyController.class)
class SendMoneyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SendMoneyUsecase sendMoneyUsecase;

	@Nested
	class sendMoney_메서드는 {

		@Nested
		class 올바른_파라미터를_전달하면 {

			@Test
			void 성공한다() throws Exception {
				mockMvc.perform(post("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}", 41L, 42L, 500L)
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk());

				then(sendMoneyUsecase).should()
					.sendMoney(eq(new SendMoneyCommand(
						new AccountId(41L),
						new AccountId(42L),
						Money.of(500L)
					)));
			}
		}
	}
}