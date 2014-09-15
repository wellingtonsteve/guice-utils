package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import javax.inject.Inject;

/**
 *
 * Should only be used in extreme cases.
 *
 */
public class DestroyableComponentLifecycleManagerFactory {

    private final PreDestroyMethodFinder preDestroyFinder;

    @Inject
    public DestroyableComponentLifecycleManagerFactory(PreDestroyMethodFinder preDestroyFinder) {
        this.preDestroyFinder = preDestroyFinder;
    }

    public DestroyableComponentLifecycleManager create(DestroyableComponentsSource componentSource) {
        return new DestroyableComponentLifecycleManager(componentSource, preDestroyFinder);
    }
}
