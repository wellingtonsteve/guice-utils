package guiceutils.childinjectortools.destroyable;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * This module should be bound at a global level.
 *
 * Both lifecycle manager containers require this to be bound.
 */
public class PreDestroyMethodFinderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PreDestroyMethodFinder.class).in(Singleton.class);
    }
}
