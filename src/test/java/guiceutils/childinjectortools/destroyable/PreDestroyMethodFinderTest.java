package guiceutils.childinjectortools.destroyable;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import guiceutils.childinjectortools.destroyable.foo.FooWithoutPreDestroy;

public class PreDestroyMethodFinderTest {
	
	private PreDestroyMethodFinder finder = new PreDestroyMethodFinder();
	
	@Test
	public void when_call_get_pre_destroy_for_class_with_valid_method_ensure_returns_method() {
		assertNotNull(finder.getMethod(FooWithPreDestroy.class));
	}
	
	@Test
	public void when_call_get_pre_destroy_for_class_without_valid_method_ensure_returns_method() {
		assertNull(finder.getMethod(FooWithoutPreDestroy.class));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void when_call_get_pre_destroy_for_class_with_annotated_method_with_arguments_throw_exception() {
		finder.getMethod(FooWithPreDestroyWithArguments.class);
	}
	
	@Test
	public void when_get_pre_destroy_method_it_is_accessible() {
		assertTrue(finder.getMethod(FooWithPreDestroy.class).isAccessible());
	}
	
	@Test(expected=AssertionError.class)
	public void when_class_has_more_than_one_pre_destroy_assertion_error_is_thrown() {
		finder.getMethod(FooWithMultiplePreDestroys.class);
	}
	
}
