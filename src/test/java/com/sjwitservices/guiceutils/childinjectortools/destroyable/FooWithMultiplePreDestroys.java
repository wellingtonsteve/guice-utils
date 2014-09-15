package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import javax.annotation.PreDestroy;

public class FooWithMultiplePreDestroys {

	@PreDestroy
	public void foo() {
		
	}
	
	@PreDestroy
	public void bar() {
		
	}
	
	
}
