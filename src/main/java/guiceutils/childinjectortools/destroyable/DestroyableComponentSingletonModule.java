package guiceutils.childinjectortools.destroyable;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * Creates a module level scope that adds basic LifecycleManagement for components
 * that are created in a module generated from a ChildInjector. 
 * 
 * If an object is scoped as DestroyableComponentSingleton, when the parent component
 * that the dependencies were bound for is destroyed, the ComponentSingletonDestroyer
 * will invoke a destroy method annotated by @PreDestroy.
 * 
 * @author ckurzeja
 *
 */
public class DestroyableComponentSingletonModule extends AbstractModule {

	@Override
	protected void configure() {
		DestroyableScope destroyableScope = new DestroyableScope(Scopes.SINGLETON);
		bindScope(DestroyableComponentSingleton.class, destroyableScope);
        bind(DestroyableComponentsSource.class).to(DestroyableComponentSingletonSource.class);
        bind(DestroyableComponentSingletonSource.class).in(Singleton.class);
		bind(DestroyableScopeVisitor.class).in(Singleton.class);
		bind(DestroyableComponentLifecycleManager.class).in(Singleton.class);
	}

}
