package com.sjwitservices.guiceutils.childinjectortools.example.foo;

import javax.inject.Singleton;

import com.google.inject.PrivateModule;

public class FooModule extends PrivateModule {

	@Override
	protected void configure() {
		bind(Foo.class).to(FooImpl.class).in(Singleton.class);
		expose(Foo.class);
	}
	
	

}
