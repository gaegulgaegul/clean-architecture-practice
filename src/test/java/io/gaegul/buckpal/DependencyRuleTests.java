package io.gaegul.buckpal;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.importer.ClassFileImporter;

import io.gaegul.buckpal.archunit.HexagonalArchitecture;

@DisplayName("계층 간 의존성 체크")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DependencyRuleTests {

	@Test
	void 도메인_계층은_어플리케이션_계층을_의존할_수_없다() {
		noClasses()
			.that()
			.resideInAPackage("io.gaegul.buckpal.account.domain..")
			.should()
			.dependOnClassesThat()
			.resideInAnyPackage("io.gaegul.buckpal.account.application..")
			.check(new ClassFileImporter()
				.importPackages("io.gaegul.buckpal.account.."));
	}

	@Test
	void 헥사고날_아키텍처_구조_검증() {
		HexagonalArchitecture.boundedContext("io.gaegul.buckpal.account")
			.withDomainLayer("domain")

			.withAdaptersLayer("adapter")
			.incoming("in.web")
			.outgoing("out.persistence")
			.and()

			.withApplicationLayer("application")
			.services("service")
			.incomingPorts("port.in")
			.outgoingPorts("port.out")
			.and()

			.withConfiguration("configuration")
			.check(new ClassFileImporter().importPackages("io.gaegul.buckpal.account.."));
	}
}
