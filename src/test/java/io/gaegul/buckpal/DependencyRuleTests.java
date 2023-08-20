package io.gaegul.buckpal;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.importer.ClassFileImporter;

@DisplayName("계층 간 의존성 체크")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DependencyRuleTests {

	@Test
	void 도메인_계층은_어플리케이션_계층을_의존할_수_없다() {
		noClasses()
			.that()
			.resideInAPackage("gaegul.domain..")
			.should()
			.dependOnClassesThat()
			.resideInAnyPackage("gaegul.application..")
			.check(new ClassFileImporter()
				.importPackages("gaegul.."));
	}
}
