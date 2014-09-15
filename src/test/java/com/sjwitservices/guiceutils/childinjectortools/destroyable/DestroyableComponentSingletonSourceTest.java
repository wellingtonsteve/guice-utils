package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;

@RunWith(MockitoJUnitRunner.class)
public class DestroyableComponentSingletonSourceTest {

	@Mock Injector injector;
	@Mock
	DestroyableScopeVisitor visitor;
	
	@Mock Key<Object> key;
	@Mock Binding<Object> binding;
	@Mock Object instance;
	
	Map<Key<?>, Binding<?>> bindings;
	
	DestroyableComponentSingletonSource source;
	
	@Before
	public void setupBindingMap() {
		bindings = new HashMap<Key<?>, Binding<?>>();
		bindings.put(key, binding);
		
		when(injector.getAllBindings()).thenReturn(bindings);
		when(injector.getInstance(key)).thenReturn(instance);
	}

	@Test
	public void if_binding_is_in_scope_then_it_is_obtained_from_injector() {
		when_scoping_visitor_visits_binding_indicate_that_the_binding_is_in_scope(true);
		initialise_source_and_get_singleton_list();
		verify(injector).getInstance(key);
	}
	
	@Test
	public void if_binding_is_not_in_scope_then_it_is_not_requested_from_injector() {
		when_scoping_visitor_visits_binding_indicate_that_the_binding_is_in_scope(false);
		initialise_source_and_get_singleton_list();
		verify(injector).getAllBindings();
		verifyNoMoreInteractions(injector);
	}
	
	@Test
	public void get_destroyable_component_singletons_returns_list_containing_bindings_contained_in_scope() {
		when_scoping_visitor_visits_binding_indicate_that_the_binding_is_in_scope(true);
		List<Object> singletons = initialise_source_and_get_singleton_list();
		assertThat(singletons, contains(instance));
	}
	
	private List<Object> initialise_source_and_get_singleton_list() {
		source = new DestroyableComponentSingletonSource(injector, visitor);
		return source.getDestroyableComponents();
	}
	
	private void when_scoping_visitor_visits_binding_indicate_that_the_binding_is_in_scope(boolean inScope) {
		when(binding.acceptScopingVisitor(visitor)).thenReturn(inScope);
	}
	
}
