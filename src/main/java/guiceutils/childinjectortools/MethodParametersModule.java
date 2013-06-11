package guiceutils.childinjectortools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.google.inject.AbstractModule;

class MethodParametersModule extends AbstractModule {
	private final Object[] args;
	private final Method method;

	MethodParametersModule(Object[] args, Method method) {
		this.args = args;
		this.method = method;
	}

	@Override
	protected void configure() {
		Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterTypes.length; i++) {
            bindClass(parameterTypes[i], args[i], annotations[i]);
        }
	}

	private <T> void bindClass(Class<T> clazz, Object object, Annotation[] annotations) {
        if (annotations.length == 0)
            bind(clazz).toInstance(clazz.cast(object));
        else if (annotations.length == 1)
            bind(clazz).annotatedWith(annotations[0]).toInstance(clazz.cast(object));
        else
            throw new RuntimeException("Multiple annotations found on binding for "+clazz.getName());
    }
}