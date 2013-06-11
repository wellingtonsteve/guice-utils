package guiceutils.childinjectortools.destroyable;

import javax.annotation.PreDestroy;

public class FooWithPreDestroyWithArguments {

	@PreDestroy
	public void destroy(int x, int y) {
		
	}
	
}
