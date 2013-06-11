package guiceutils.childinjectortools.example.main;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import guiceutils.childinjectortools.ChildInjectorBuilder;
import guiceutils.childinjectortools.example.foo.Foo;
import guiceutils.childinjectortools.example.foo.FooFactory;
import guiceutils.childinjectortools.example.foo.FooModule;
import guiceutils.childinjectortools.example.foo.FooOptions;

public class ChildInjectorBuilderTest {

	@Test public void test_everything_binds_nicely_in_foo_example() {
		Injector injector = Guice.createInjector(create_an_example_module_that_uses_foo_factory());
		FooOptions fooOptions = create_foo_options();
		FooFactory fooFactory = inject_foo_factory(injector);
		pass_foo_options_into_foo_factory_to_make_a_foo(fooOptions, fooFactory);
	}

	private AbstractModule create_an_example_module_that_uses_foo_factory() {
		return new AbstractModule() {
			@Override
			protected void configure() {
				install(ChildInjectorBuilder.implement(FooFactory.class).using(new FooModule()));
			}
		};
	}
	
	private FooOptions create_foo_options() {
		FooOptions fooOptions = new FooOptions();
		System.err.println("FooOptions created: " + fooOptions);
		return fooOptions;
	}

	private FooFactory inject_foo_factory(Injector injector) {
		FooFactory fooFactory = injector.getInstance(FooFactory.class);
		return fooFactory;
	}

	private void pass_foo_options_into_foo_factory_to_make_a_foo(FooOptions fooOptions, FooFactory fooFactory) {
		Foo foo = fooFactory.makeFoo(fooOptions);
		System.err.println("FooFactory returned " + foo);
	}

}
