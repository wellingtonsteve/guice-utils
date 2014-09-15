package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;

class DestroyableComponentSingletonSource implements DestroyableComponentsSource {

	private final Injector injector;	
	private final DestroyableScopeVisitor scopeVisitor;
	
	@Inject 
	DestroyableComponentSingletonSource(Injector injector, DestroyableScopeVisitor scopeVisitor) {
		this.injector = injector;
		this.scopeVisitor = scopeVisitor;
	}
	
	public List<Object> getDestroyableComponents() {
		List<Object> destroyableComponentSingletons = new ArrayList<Object>();
		for (Entry<Key<?>, Binding<?>> entry : injector.getAllBindings().entrySet()) {
			boolean isInScope = entry.getValue().acceptScopingVisitor(scopeVisitor);
			
			if (isInScope) {
				Object destroyableComponent = injector.getInstance(entry.getKey());
				destroyableComponentSingletons.add(destroyableComponent);
			}
		}
		
		return destroyableComponentSingletons;
	}
	
}
