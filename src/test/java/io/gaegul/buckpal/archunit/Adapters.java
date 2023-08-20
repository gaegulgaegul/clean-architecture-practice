package io.gaegul.buckpal.archunit;

import java.util.ArrayList;
import java.util.List;

import com.tngtech.archunit.core.domain.JavaClasses;


public class Adapters extends ArchitectureElement {
	private final HexagonalArchitecture parentContext;
	private final List<String> incomingAdapterPackages = new ArrayList<>();
	private final List<String> outgoingAdapterPackages = new ArrayList<>();

	public Adapters(final HexagonalArchitecture parentContext, final String basePackage) {
		super(basePackage);
		this.parentContext = parentContext;
	}

	public Adapters incoming(final String packageName) {
		this.incomingAdapterPackages.add(fullQualifiedPackage(packageName));
		return this;
	}

	public Adapters outgoing(final String packageName) {
		this.outgoingAdapterPackages.add(fullQualifiedPackage(packageName));
		return this;
	}

	List<String> allAdapterPackages() {
		final List<String> allAdapters = new ArrayList<>();
		allAdapters.addAll(this.incomingAdapterPackages);
		allAdapters.addAll(this.outgoingAdapterPackages);
		return allAdapters;
	}

	public HexagonalArchitecture and() {
		return parentContext;
	}

	String getBasePackage() {
		return super.basePackage;
	}

	void dontDependOnEachOther(final JavaClasses classes) {
		List<String> allAdapters = allAdapterPackages();
		for (String adapter1 : allAdapters) {
			for (String adapter2 : allAdapters) {
				if (!adapter1.equals(adapter2)) {
					denyDependency(adapter1, adapter2, classes);
				}
			}
		}
	}

	void doesNotDependOn(final String packageName, final JavaClasses classes) {
		denyDependency(this.basePackage, packageName, classes);
	}

	void doesNotContainEmptyPackages() {
		denyEmptyPackages(allAdapterPackages());
	}
}
