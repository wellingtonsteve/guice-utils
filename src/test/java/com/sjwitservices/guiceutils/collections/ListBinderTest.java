package com.sjwitservices.guiceutils.collections;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ListBinderTest {

    private String firstItem = "first";
    private String secondItem = "second";
    private String thirdItem = "third";
    private List<String> expectedList = Arrays.asList(firstItem, secondItem, thirdItem);
    private Key<List<String>> listKey = Key.get(new TypeLiteral<List<String>>(){{}});

    @Test
    public void items_bound_with_ListBinder_are_injected_in_priority_order() {
        Injector injector = Guice.createInjector(new AbstractListModule() {
            @Override
            protected void configure() {
                ListBinder<String> listBinder = newListBinder();
                listBinder.addBinding(3).toInstance(thirdItem);
                listBinder.addBinding(1).toInstance(firstItem);
                listBinder.addBinding(2).toInstance(secondItem);
            }
        });

        assertThat(injector.getInstance(listKey), equalTo(expectedList));
    }

    @Test
    public void items_bound_with_different_ListBinders_are_all_injected_in_priority_order() {
        Injector injector = Guice.createInjector(new AbstractListModule() {
            @Override
            protected void configure() {
                newListBinder().addBinding(3).toInstance(thirdItem);
                newListBinder().addBinding(1).toInstance(firstItem);
                newListBinder().addBinding(2).toInstance(secondItem);
            }

        });

        assertThat(injector.getInstance(listKey), equalTo(expectedList));
    }

    @Test
    public void items_bound_with_different_modules_are_all_injected_in_priority_order() {
        Injector injector = Guice.createInjector(new AbstractListModule() {
            @Override
            protected void configure() {
                newListBinder().addBinding(3).toInstance(thirdItem);
            }

        }, new AbstractListModule() {
            @Override
            protected void configure() {
                newListBinder().addBinding(1).toInstance(firstItem);
                newListBinder().addBinding(2).toInstance(secondItem);
            }

        });

        assertThat(injector.getInstance(listKey), equalTo(expectedList));
    }

    private static abstract class AbstractListModule extends AbstractModule {
        protected ListBinder<String> newListBinder() {
            return ListBinder.newListBinder(binder(), String.class);
        }
    }
}
