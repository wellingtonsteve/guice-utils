package guiceutils.childinjectortools.example.foo;

import javax.inject.Inject;

public class FooImpl implements Foo {
	
	@Inject public FooImpl(FooOptions fooOptions) {
		System.err.println(this + " created with foo options " + fooOptions);
	}

}
