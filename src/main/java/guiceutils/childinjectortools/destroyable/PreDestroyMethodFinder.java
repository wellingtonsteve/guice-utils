package guiceutils.childinjectortools.destroyable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.annotation.PreDestroy;

class PreDestroyMethodFinder {

	public Method getMethod(Class<?> type) {
		Method method = findMethodWithAnnotation(type, PreDestroy.class);
		
		if (method != null)
			if (method.getParameterTypes().length != 0)
				throw new IllegalArgumentException("PreDestroy should have no arguments");
		
		return method;
	}
	
	private Method findMethodWithAnnotation(Class<?> type, Class<? extends Annotation> annotationType) {
		Method method = null;
		int annotatedMethodCount = 0;
		for (Method declaredMethod : type.getDeclaredMethods()) {
			Annotation fromElement = declaredMethod.getAnnotation(annotationType);
			
			if (fromElement != null) {
				assert annotatedMethodCount == 0 : "Class should only define one PreDestroy annotation";
				
				method = declaredMethod;
				method.setAccessible(true);
				
				annotatedMethodCount++;
			}
		}
		
		return method;
	}
	
}
