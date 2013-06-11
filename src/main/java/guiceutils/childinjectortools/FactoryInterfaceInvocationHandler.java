package guiceutils.childinjectortools;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.google.inject.Injector;
import com.google.inject.Module;

class FactoryInterfaceInvocationHandler implements InvocationHandler {
	
	private final Injector parentInjector;
	private final Module[] childModules;
	private final Class<?> factoryInterface;
	
	FactoryInterfaceInvocationHandler(Injector parentInjector, Module[] modules, Class<?> factoryInterface) {
		this.parentInjector = parentInjector;
		this.childModules = modules;
		this.factoryInterface = factoryInterface;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)  {
		if (method.getName().equals("equals"))
			return false;
		else if (method.getName().equals("hashCode"))
			return 0;
		else if (method.getName().equals("toString"))
			return "FactoryInterfaceInvocationHandler_Implementation_of_" + factoryInterface.getName();
		
		Injector childInjector = createChildInjector(method, args);
		return childInjector.getInstance(method.getReturnType());
	}

	private Injector createChildInjector(Method method, Object[] args) {
		Module parameterModule = createModuleThatBindsMethodParameters(method, args);
		Module[] allRequiredModules = appendModule(childModules, parameterModule);
		return parentInjector.createChildInjector(allRequiredModules);
	}

	private Module[] appendModule(Module[] baseModules, Module additionalModule) {
		Module[] modules = Arrays.copyOf(baseModules, baseModules.length + 1);
		modules[baseModules.length] = additionalModule;
		return modules;
	}

	private MethodParametersModule createModuleThatBindsMethodParameters(Method method, Object[] args) {
		return new MethodParametersModule(args, method);
	}
	
}