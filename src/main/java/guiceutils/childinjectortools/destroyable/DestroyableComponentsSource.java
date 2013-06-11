package guiceutils.childinjectortools.destroyable;

import java.util.List;

public interface DestroyableComponentsSource {

    List<Object> getDestroyableComponents();
}
