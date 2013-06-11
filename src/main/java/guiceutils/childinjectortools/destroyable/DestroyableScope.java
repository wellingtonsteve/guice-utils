package guiceutils.childinjectortools.destroyable;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

class DestroyableScope implements Scope {

	private final Scope singletonScope;
	
	public DestroyableScope(Scope singletonScope) {
		this.singletonScope = singletonScope;
	}
	
	public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
		return singletonScope.scope(key, creator);
	}
	
	@Override
	public String toString() {
		return "Scopes.Destroyable";
	}
	
}
