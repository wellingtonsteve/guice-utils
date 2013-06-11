package guiceutils.childinjectortools.destroyable;

import javax.annotation.PreDestroy;

public class FooWithPreDestroy {

	@PreDestroy
	private void destroy() {
		
	}
	
}
