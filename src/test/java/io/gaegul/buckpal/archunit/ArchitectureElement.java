package io.gaegul.buckpal.archunit;

import static com.tngtech.archunit.lang.conditions.ArchConditions.containNumberOfElements;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import java.util.List;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

/**
 * 아키텍처 검증 엘리먼트
 */
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

	/**
	 * 의존되지 않아야 할 패키지 구조 검증
	 * @param fromPackageName 검증 패키지명
	 * @param toPackageName 의존 대상 패키지명
	 * @param classes 기본 패키지 구조의 클래스 모음
	 */
	static void denyDependency(final String fromPackageName, final String toPackageName, final JavaClasses classes) {
		noClasses()
			.that()
			.resideInAPackage(matchAllClassesInPackage(fromPackageName))
			.should()
			.dependOnClassesThat()
			.resideInAnyPackage(matchAllClassesInPackage(toPackageName))
			.check(classes);
	}

	/**
	 * 의존되지 않아야 할 패키지 구조 검증
	 * @param fromPackages 검증 패키지 목록
	 * @param toPackages 의존 대상 패키지 목록
	 * @param classes 기본 패키지 구조의 클래스 모음
	 */
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

	/**
	 * 패키지 구조의 모든 클래스 정보를 반환하는 클래스패스 반환
	 * @param packageName 패키지명
	 * @return
	 */
	private static String matchAllClassesInPackage(final String packageName) {
		return packageName + "..";
	}

	/**
	 * 빈 패키지 구조가 되면 안되는 영역 검증
	 * @param packageName 패키지명
	 */
	void denyEmptyPackage(final String packageName) {
		classes()
			.that()
			.resideInAPackage(matchAllClassesInPackage(packageName))
			.should(containNumberOfElements(DescribedPredicate.greaterThanOrEqualTo(1)))
			.check(classesInPackage(packageName));
	}

	/**
	 * 패키지 구조의 클래스 모음 반환
	 * @param packageName 패키지명
	 * @return
	 */
	private JavaClasses classesInPackage(final String packageName) {
		return new ClassFileImporter().importPackages(packageName);
	}

	/**
	 * 빈 패키지 구조가 되면 안되는 영역 검증
	 * @param packages 패키지 목록
	 */
	void denyEmptyPackages(final List<String> packages) {
		for (String packageName : packages) {
			denyEmptyPackage(packageName);
		}
	}
}
