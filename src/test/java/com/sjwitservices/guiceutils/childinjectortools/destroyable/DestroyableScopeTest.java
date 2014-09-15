package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;



@RunWith(MockitoJUnitRunner.class)
public class DestroyableScopeTest {

	@Mock Scope scope;
	@Mock Key<Object> key;
	@Mock Provider<Object> provider;
	
	String EXPECTED_TO_STRING_VALUE = "Scopes.Destroyable";
	
	DestroyableScope destroyableScope;
	
	@Test
	public void when_call_scope_the_call_is_delegated_to_the_injected_scope() {
		initialise_scope();
		destroyableScope.scope(key, provider);
		verify(scope).scope(key, provider);
	}
	
	@Test
	public void to_string_returns_valid_string() {
		initialise_scope();
		assertEquals(destroyableScope.toString(), EXPECTED_TO_STRING_VALUE);
	}
	
	private void initialise_scope() {
		destroyableScope = new DestroyableScope(scope);
	}

	
	

	
}
