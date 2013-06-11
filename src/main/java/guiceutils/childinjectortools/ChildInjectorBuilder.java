package guiceutils.childinjectortools;

import java.lang.annotation.Annotation;

import com.google.inject.AbstractModule;
import com.google.inject.Key;

import com.google.inject.Module;

/**
 * <b>Usage:</b> install(ChildInjectorBuilder.implement(FooFactory.class).using(new FooModule()));
 * <br><br>
 * Given a factory interface e.g. FooFactory, this class creates and binds an implementation of that interface. 
 * <br><br>
 * When a method is called on the factory, a child injector is created using the supplied 
 * Guice Module. Parameters passed into the factory interface are also bound into child injector.  
 * The return value of the factory method is then taken from the child injector.
 * <br><br>
 * Current restrictions on factory method parameters:<br>
 * - Annotations on parameters are ignored, and therefore will not be injectable in the child module<br>
 * - This means each parameter's type must be unique within that method<br>
 * &nbsp;&nbsp;  e.g. Foo createFoo(FooOptions options, FooOptions otherOptions) will fail<br>
 * - Behaviour when using generics is unknown currently. It probably won't work.
 * @param <T>
 */
public class ChildInjectorBuilder<T> {
	
	private final Class<T> factoryInterfaceToImplement;
	private Key<T> key;
	
	private ChildInjectorBuilder(Class<T> factoryInterfaceToImplement) {
		this.factoryInterfaceToImplement = factoryInterfaceToImplement;
		key = Key.get(factoryInterfaceToImplement);
	}

	public static <T> ChildInjectorBuilder<T> implement(Class<T> factoryInterfaceToImplement) {
		return new ChildInjectorBuilder<T>(factoryInterfaceToImplement);
	}

	public ChildInjectorBuilder<T> annotatedWith(Class<? extends Annotation> annotationType) {
		key = Key.get(factoryInterfaceToImplement, annotationType);
		return this;
	}
	
	public Module using(final Module...childModules) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(key).toProvider(new ChildInjectorFactoryInterfaceProvider<T>(factoryInterfaceToImplement, childModules));
			}
		};
	}

}
