package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DestroyableComponentLifecycleManagerTest {

	@Mock
	DestroyableComponentSingletonSource singletonSource;
	@Mock
	PreDestroyMethodFinder preDestroyFinder;

	@Mock IllegalArgumentException illegalArgException;
	@Mock InvocationTargetException methodInvokationException;
	
	ClassWithAnnotatedPreDestroy preDestroyClass = new ClassWithAnnotatedPreDestroy();
	Method method;
	Method exceptionMethod;
	
	List<Object> singletonList;
	
	DestroyableComponentLifecycleManager manager;

    @Before
    public void find_test_methods() throws NoSuchMethodException {
        method = preDestroyClass.getClass().getDeclaredMethod("destroy");
        exceptionMethod = preDestroyClass.getClass().getDeclaredMethod("throwError");
    }

	@Before 
	public void initialise_singleton_list() {
		singletonList = new ArrayList<Object>();
		singletonList.add(preDestroyClass);
		when(singletonSource.getDestroyableComponents()).thenReturn(singletonList);
	}
	
	@Test
	public void calling_start_invokes_getDestroyableComponentSingletons_from_singletonSource() {
		initialise_manager();
		manager.start();
		verify(singletonSource).getDestroyableComponents();
	}
	
	@Test
	public void calling_end_before_calling_start_does_not_interact_with_the_preDestroyFinder() {
		initialise_manager();
		manager.end();
		Mockito.verifyNoMoreInteractions(preDestroyFinder);
	}
	
	@Test
	public void calling_end_destroys_any_scoped_objects_with_pre_destroy_annotated_methods() throws Exception {
		when_getPreDstroy_method_for_pre_destroy_class_return(method);
		initialise_manager_and_destroy_component_singletons();
		assertThat(preDestroyClass.isDestroyed, is(true));
	}
	

	private void initialise_manager() {
		manager = new DestroyableComponentLifecycleManager(singletonSource, preDestroyFinder);
	}
		
	@Test
	public void calling_end_does_not_destroy_scoped_object_without_a_predestroy_annotation() throws Exception {
		when_getPreDstroy_method_for_pre_destroy_class_return(null);
		initialise_manager_and_destroy_component_singletons();
		assertThat(preDestroyClass.isDestroyed, is(false));
	}

	@Test
	public void when_call_end_and_the_preDestroyFinder_get_method_throws_illegal_arg_exception_then_a_destroying_component_singleton_exception_is_thrown_containing_illegal_arg_exception() {
		when(preDestroyFinder.getMethod(any(Class.class))).thenThrow(illegalArgException);
		try {
			initialise_manager_and_destroy_component_singletons();
		} catch (DestroyingSingletonComponentException e) {
			assertThat(e.getThrownExceptions(), contains((Exception) illegalArgException));
		}	
	}
	
	@Test
	public void when_call_end_and_method_invokation_throws_exception_then_a_destroying_component_singleton_exception_is_thrown_containing_method_exception() {
		when_getPreDstroy_method_for_pre_destroy_class_return(exceptionMethod);
		try {
			initialise_manager_and_destroy_component_singletons();
		} catch (DestroyingSingletonComponentException e) {
			assertEquals(e.getThrownExceptions().get(0).getClass(), InvocationTargetException.class);
		}
	}

	private void initialise_manager_and_destroy_component_singletons() {
		initialise_manager();
		manager.start();
		manager.end();
	}
	
	private void when_getPreDstroy_method_for_pre_destroy_class_return(Method method) {
		when(preDestroyFinder.getMethod(Mockito.any(Class.class))).thenReturn(method);
	}
	
	private class ClassWithAnnotatedPreDestroy {
		
		boolean isDestroyed = false;
		
		@PreDestroy
		public void destroy() {
			isDestroyed = true;
		}
		
		//This is actually used but IDE complained about it not being used. Just leave the supress
		//and all is well.
		@SuppressWarnings("unused")
		public void throwError() throws Exception {
			throw new Exception();
		}
		
	}
	
}
