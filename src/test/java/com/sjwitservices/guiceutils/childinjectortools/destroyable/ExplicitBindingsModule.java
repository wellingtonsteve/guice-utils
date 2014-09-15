package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import com.google.inject.AbstractModule;

class ExplicitBindingsModule extends AbstractModule {

	@Override
	protected void configure() {
		binder().requireExplicitBindings();
	}
	
}
