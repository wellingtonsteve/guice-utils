package guiceutils.childinjectortools.destroyable;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class DestroyableComponentWithFactoryModuleTest {

    private Injector injector;


    @Before
    public void createInjector() {
        injector = Guice.createInjector(new PreDestroyMethodFinderModule(), new DestroyableComponentWithFactoryModule(), new ExplicitBindingsModule());
    }

    @Test
    public void DestroyableComponentLifecycleManagerFactory_is_bound() {
        assertNotNull(injector.getInstance(DestroyableComponentLifecycleManagerFactory.class));
    }
}
