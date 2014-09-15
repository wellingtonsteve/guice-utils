package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;

public class DestroyableComponentWithFactoryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DestroyableComponentLifecycleManagerFactory.class).in(Singleton.class);
	}

}
