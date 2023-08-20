package io.gaegul.buckpal.archunit;

import static com.tngtech.archunit.lang.conditions.ArchConditions.containNumberOfElements;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import java.util.List;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

public class ArchitectureElement {

	final String basePackage;

	public ArchitectureElement(final String basePackage) {
		this.basePackage = basePackage;
	}

	/**
	 * 풀 패키지 경로 반환
	 * @param relativePackage 상대 경로 패키지
	 * @return
	 */
	String fullQualifiedPackage(final String relativePackage) {
		return this.basePackage + "." + relativePackage;
	}

	static void denyDependency(final String fromPackageName, final String toPackageName, final JavaClasses classes) {
		noClasses()
			.that()
			.resideInAPackage(matchAllClassesInPackage(fromPackageName))
			.should()
			.dependOnClassesThat()
			.resideInAnyPackage(matchAllClassesInPackage(toPackageName))
			.check(classes);
	}

	static void denyAnyDependency(final List<String> fromPackages, final List<String> toPackages, final JavaClasses classes) {
		for (String fromPackage : fromPackages) {
			for (String toPackage : toPackages) {
				noClasses()
					.that()
					.resideInAPackage(matchAllClassesInPackage(fromPackage))
					.should()
					.dependOnClassesThat()
					.resideInAnyPackage(matchAllClassesInPackage(toPackage))
					.check(classes);
			}
		}
	}

	private static String matchAllClassesInPackage(final String packageName) {
		return packageName + "..";
	}

	void denyEmptyPackage(final String packageName) {
		classes()
			.that()
			.resideInAPackage(matchAllClassesInPackage(packageName))
			.should(containNumberOfElements(DescribedPredicate.greaterThanOrEqualTo(1)))
			.check(classesInPackage(packageName));
	}

	private JavaClasses classesInPackage(final String packageName) {
		return new ClassFileImporter().importPackages(packageName);
	}

	void denyEmptyPackages(final List<String> packages) {
		for (String packageName : packages) {
			denyEmptyPackage(packageName);
		}
	}
}
