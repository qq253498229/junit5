/*
 * Copyright 2015-2016 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.junit.gen5.engine.junit5.extension;

import static org.junit.gen5.api.Assertions.assertFalse;
import static org.junit.gen5.api.Assertions.assertNotNull;
import static org.junit.gen5.api.Assertions.assertThrows;
import static org.junit.gen5.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.junit.gen5.api.Test;
import org.junit.gen5.api.TestReporter;
import org.junit.gen5.api.extension.ExtensionContext;
import org.junit.gen5.commons.util.PreconditionViolationException;
import org.junit.gen5.commons.util.ReflectionUtils;
import org.mockito.Mockito;

/**
 * @since 5.0
 */
class TestReporterParameterResolverTests {

	TestReporterParameterResolver resolver = new TestReporterParameterResolver();

	@Test
	void testSupports() {
		Parameter parameter1 = findParameterOfMethod("methodWithTestReporterParameter", TestReporter.class);
		assertTrue(this.resolver.supports(parameter1, null, null));

		Parameter parameter2 = findParameterOfMethod("methodWithoutTestReporterParameter", String.class);
		assertFalse(this.resolver.supports(parameter2, null, null));
	}

	@Test
	void testSupports_NullSafe() {
		assertThrows(PreconditionViolationException.class, () -> {
			this.resolver.supports(null, null, null);
		});
	}

	@Test
	void testResolve() {
		Parameter parameter = findParameterOfMethod("methodWithTestReporterParameter", TestReporter.class);

		TestReporter testReporter = this.resolver.resolve(parameter, null, Mockito.mock(ExtensionContext.class));
		assertNotNull(testReporter);
	}

	private Parameter findParameterOfMethod(String methodName, Class<?>... parameterTypes) {
		Method method = ReflectionUtils.findMethod(Sample.class, methodName, parameterTypes).get();
		return method.getParameters()[0];
	}

	static class Sample {

		void methodWithTestReporterParameter(TestReporter reporter) {
		}

		void methodWithoutTestReporterParameter(String nothing) {
		}

	}

}
