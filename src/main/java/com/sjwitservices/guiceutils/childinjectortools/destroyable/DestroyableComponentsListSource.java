package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import java.util.ArrayList;
import java.util.List;

public class DestroyableComponentsListSource implements DestroyableComponentsSource {

    private final List<Object> registeredComponents = new ArrayList<Object>();

    public void registerComponent(Object component) {
        registeredComponents.add(component);
    }

    public void clear() {
        registeredComponents.clear();
    }

    @Override
    public List<Object> getDestroyableComponents() {
        return registeredComponents;
    }
}
