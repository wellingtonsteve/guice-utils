package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import java.lang.annotation.Annotation;

import com.google.inject.Scope;
import com.google.inject.spi.BindingScopingVisitor;

class DestroyableScopeVisitor implements BindingScopingVisitor<Boolean> {

	@Override
	public Boolean visitEagerSingleton() {
		return false;
	}

	@Override
	public Boolean visitScope(Scope scope) {
		return scope instanceof DestroyableScope;
	}

	@Override
	public Boolean visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
		return scopeAnnotation.equals(DestroyableComponentSingleton.class);
	}

	@Override
	public Boolean visitNoScoping() {
		return false;
	}

}
