package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class DestroyableComponentSingletonModuleTest {

	private Injector injector;
	
	@Before
	public void createInjector() {
		injector = Guice.createInjector(new PreDestroyMethodFinderModule(), new DestroyableComponentSingletonModule(), new ExplicitBindingsModule());
	}
	
	@Test
	public void list_of_scopes_contains_DestroyableComponentSingleton_annotation() {
		Assert.assertThat(injector.getScopeBindings().keySet(), Matchers.hasItem(DestroyableComponentSingleton.class));
	}
	
	@Test
	public void DestroyableComponentLifecycleManager_is_bound() {
		Assert.assertNotNull(injector.getInstance(DestroyableComponentLifecycleManager.class));
	}
	
	@Test
	public void DestroyableComponentSingletonSource_is_bound() {
		Assert.assertNotNull(injector.getInstance(DestroyableComponentSingletonSource.class));
	}
	
	@Test
	public void DestroyableScopeVisitor_is_bound() {
		Assert.assertNotNull(injector.getInstance(DestroyableScopeVisitor.class));
	}
	
	@Test
	public void PreDestroyMethodFinder_is_bound() {
		Assert.assertNotNull(injector.getInstance(PreDestroyMethodFinder.class));
	}
	
}
