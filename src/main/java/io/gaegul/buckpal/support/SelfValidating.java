package io.gaegul.buckpal.support;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * 유효성 검증 규칙에 위반된 정보가 있는지 확인하는 클래스
 * <code>@Valid</code> 애너테이션의 동작을 대신 수행한다.
 * @param <T>
 */
public abstract class SelfValidating<T> {

	private Validator validator;

	public SelfValidating() {
		this.validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	/**
	 * 유효성 검증 규칙에 위반된 정보를 확인한다.
	 */
	protected void validateSelf() {
		final Set<ConstraintViolation<T>> violations = validator.validate((T)this);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}
}
