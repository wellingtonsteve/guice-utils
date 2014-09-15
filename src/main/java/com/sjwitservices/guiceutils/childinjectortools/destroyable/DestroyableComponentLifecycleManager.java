package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * DestroyableScope generates Singleton scoped providers by delegating to Scopes.SINGLETON.
 * 
 * It tracks any of the scoped providers and can be invoked under the ComponentSingletonDestroyer
 * to check any of the registered singletons for a @PreDestroy method and call that method to 
 * destroy the singletons.
 * 
 * @author ckurzeja
 *
 */
public class DestroyableComponentLifecycleManager {

	private final DestroyableComponentsSource componentSource;
	private final PreDestroyMethodFinder preDestroyFinder;
	
	private List<Object> destroyableComponents = new ArrayList<Object>();
	private List<Exception> thrownExceptions = new ArrayList<Exception>();
	
	@Inject
	DestroyableComponentLifecycleManager(DestroyableComponentsSource componentSource, PreDestroyMethodFinder preDestroyFinder) {
		this.preDestroyFinder = preDestroyFinder;
		this.componentSource = componentSource;
	}
	
	public void start() {
		destroyableComponents = componentSource.getDestroyableComponents();
	}
	
	
	public void end(){
		for(Object object : destroyableComponents) {
			findAndInvokePreDestroyMethod(object);
		}
		
		throwExceptionIfExceptionListNotEmpty();
	}
	
	private void findAndInvokePreDestroyMethod(Object object) {
		try {
			Method method = preDestroyFinder.getMethod(object.getClass());
	
			if (method != null)
				method.invoke(object, new Object[0]);
			
		} catch (Exception e) {
			thrownExceptions.add(e);
		}
	}
	
	private void throwExceptionIfExceptionListNotEmpty() throws DestroyingSingletonComponentException {
		if (thrownExceptions.size() > 0)
			throw new DestroyingSingletonComponentException(thrownExceptions);
	}
}
