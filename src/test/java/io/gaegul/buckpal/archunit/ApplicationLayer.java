package io.gaegul.buckpal.archunit;

import java.util.ArrayList;
import java.util.List;

import com.tngtech.archunit.core.domain.JavaClasses;

public class ApplicationLayer extends ArchitectureElement {
	private final HexagonalArchitecture parentContext;
	private final List<String> incomingPortsPackages = new ArrayList<>();
	private final List<String> outgoingPortsPackages = new ArrayList<>();
	private final List<String> servicePackages = new ArrayList<>();

	public ApplicationLayer(final String basePackage, final HexagonalArchitecture parentContext) {
		super(basePackage);
		this.parentContext = parentContext;
	}

	public ApplicationLayer incomingPorts(final String packageName) {
		this.incomingPortsPackages.add(fullQualifiedPackage(packageName));
		return this;
	}

	public ApplicationLayer outgoingPorts(final String packageName) {
		this.outgoingPortsPackages.add(fullQualifiedPackage(packageName));
		return this;
	}

	public ApplicationLayer services(final String packageName) {
		this.servicePackages.add(fullQualifiedPackage(packageName));
		return this;
	}

	public HexagonalArchitecture and() {
		return parentContext;
	}

	void doesNotDependOn(final String packageName, final JavaClasses classes) {
		denyDependency(this.basePackage, packageName, classes);
	}

	public void incomingAndOutgoingPortsDoNotDependOnEachOther(JavaClasses classes) {
		denyAnyDependency(this.incomingPortsPackages, this.outgoingPortsPackages, classes);
		denyAnyDependency(this.outgoingPortsPackages, this.incomingPortsPackages, classes);
	}

	List<String> allPackages() {
		final List<String> allPackages = new ArrayList<>();
		allPackages.addAll(this.incomingPortsPackages);
		allPackages.addAll(this.outgoingPortsPackages);
		allPackages.addAll(this.servicePackages);
		return allPackages;
	}

	void doesNotContainEmptyPackages() {
		denyEmptyPackages(allPackages());
	}
}
