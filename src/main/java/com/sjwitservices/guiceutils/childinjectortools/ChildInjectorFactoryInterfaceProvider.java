package com.sjwitservices.guiceutils.childinjectortools;

import java.lang.reflect.Proxy;

import javax.inject.Inject;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;

/**
 * Given a factory interface T, this class provides an instance of T. 
 * <br><br>
 * When a method is called on the factory, a child injector is created using the supplied 
 * Guice Module. Parameters passed into the factory interface are also bound into child injector.  
 * The return value of the factory method is then taken from the child injector.
 * <br><br>
 * Current restrictions on factory method parameters:<br>
 * - Annotations on parameters are ignored, and therefore will not be injectable in the child module<br>
 * - This means each parameter's type must be unique within that method<br>
 * &nbsp;&nbsp;  e.g. Foo createFoo(FooOptions options, FooOptions otherOptions) will fail<br>
 * - Behaviour when using generics is unknown currently
 * @param <T>
 */
class ChildInjectorFactoryInterfaceProvider<T> implements Provider<T> {
	
	private final Class<T> factoryClass;
	private final Module[] modules;
	//Using Proxy to create our instance of the factory interface. This Invocation Handler looks after calls the methods on the factory.
	private FactoryInterfaceInvocationHandler invocationHandler;
	
	ChildInjectorFactoryInterfaceProvider(Class<T> factoryClass, Module[] modules) {
		this.factoryClass = factoryClass;
		this.modules = modules;
	}
	
	/*
	 * Guice should call this to inject the current injector
	 */
	@Inject public void setParentInjector(Injector parentInjector) {
		createInvocationHandler(parentInjector);
	}

	@Override
	public T get() {
		if (invocationHandler == null)
			throw new IllegalStateException("No invocation handler available because parent Injector has not been set");
		return createInstanceInvocationHandler();
	}

	private void createInvocationHandler(Injector parentInjector) {
		invocationHandler = new FactoryInterfaceInvocationHandler(parentInjector, modules, factoryClass);
	}

	@SuppressWarnings("unchecked")
	private T createInstanceInvocationHandler() {
		return (T) Proxy.newProxyInstance(factoryClass.getClassLoader(), new Class[] {factoryClass}, invocationHandler);
	}
	
}