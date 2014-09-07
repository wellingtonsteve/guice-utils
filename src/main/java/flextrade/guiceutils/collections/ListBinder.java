package flextrade.guiceutils.collections;

import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.Errors;
import com.google.inject.spi.Message;
import com.google.inject.spi.Toolable;
import com.google.inject.util.Types;

import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A ListBinding will create a List<T> binding for the types added to it.
 * ListBinder can be used from different modules allowing you to build up a
 * list across the application.
 * <p>
 * List order is determined via priorities -- all objects inserted with the same
 * priority will appear in a completely undefined order.
 *
 * @param <T>
 */
public class ListBinder<T> implements Module, Provider<List<T>> {
    private static final AtomicInteger nextUniqueId = new AtomicInteger(1);

    private static <T> ListBinder<T> newListBinder(Binder binder, TypeLiteral<T> type, Key<List<T>> listKey) {
        binder = binder.skipSources(ListBinder.class);
        ListBinder<T> result = new ListBinder<T>(binder, type, listKey);
        binder.install(result);
        return result;
    }

    public static <T> ListBinder<T> newListBinder(Binder binder, TypeLiteral<T> type) {
        return newListBinder(binder, type, Key.get(listOf(type)));
    }

    public static <T> ListBinder<T> newListBinder(Binder binder, TypeLiteral<T> type, Class<? extends Annotation> annotation) {
        return newListBinder(binder, type, Key.get(listOf(type), annotation));
    }

    public static <T> ListBinder<T> newListBinder(Binder binder, TypeLiteral<T> type, Annotation annotation) {
        return newListBinder(binder, type, Key.get(listOf(type), annotation));
    }

    public static <T> ListBinder<T> newListBinder(Binder binder, Class<T> type) {
        return newListBinder(binder, TypeLiteral.get(type));
    }

    public static <T> ListBinder<T> newListBinder(Binder binder, Class<T> type, Class<? extends Annotation> annotation) {
        return newListBinder(binder, TypeLiteral.get(type), annotation);
    }

    public static <T> ListBinder<T> newListBinder(Binder binder, Class<T> type, Annotation annotation) {
        return newListBinder(binder, TypeLiteral.get(type), annotation);
    }

    @SuppressWarnings("unchecked")
    static <T> TypeLiteral<List<T>> listOf(TypeLiteral<T> elementType) {
        Type type = Types.listOf(elementType.getType());
        return (TypeLiteral<List<T>>) TypeLiteral.get(type);
    }

    private final Binder binder;
    private final TypeLiteral<T> elementType;
    private final Key<List<T>> listKey;
    private List<Binding<T>> bindings;
    private String listName;

    private ListBinder(Binder binder, TypeLiteral<T> elementType, Key<List<T>> listKey) {
        this.binder = binder;
        this.elementType = elementType;
        this.listKey = listKey;
        this.listName = nameFor(listKey);
    }

    private String nameFor(Key<?> key) {
        Annotation annotation = key.getAnnotation();
        Class<? extends Annotation> annotationType = key.getAnnotationType();

        if (annotation != null && !Annotations.isMarker(annotationType))
            return annotation.toString();

        if (annotationType != null)
            return "@" + annotationType.getName();

        return "";
    }

    public void configure(Binder binder) {
        binder.bind(listKey).toProvider(this);
    }

    public LinkedBindingBuilder<T> addBinding(int priority) {
        return binder.bind(Key.get(elementType, new PriorityWrapper(priority, listName)));
    }

    private boolean keyMatches(Key<?> key) {
        Annotation annotation = key.getAnnotation();
        return key.getTypeLiteral().equals(elementType)
                && annotation instanceof ListElement && ((ListElement) annotation).listName().equals(listName);
    }

    @Inject
    @Toolable
    void initialize(Injector injector) {
        List<Binding<T>> bindings = new ArrayList<Binding<T>>();

        for ( Binding<?> entry : injector.findBindingsByType(elementType) ) {
            if ( !keyMatches(entry.getKey()) )
                continue;

            @SuppressWarnings("unchecked")
            Binding<T> binding = (Binding<T>) entry;
            bindings.add(binding);
        }

        Collections.sort(bindings, new PriorityComparator());
        this.bindings = Collections.unmodifiableList(bindings);
    }

    public List<T> get() {
        List<T> result = new ArrayList<T>(bindings.size());
        for (Binding<T> binding : bindings) {
            T newItem = binding.getProvider().get();
            checkConfiguration(newItem != null, "Set injection failed due to null element");
            result.add(newItem);
        }
        return Collections.unmodifiableList(result);
    }

    @Override public boolean equals(Object o) {
        return o instanceof ListBinder && ((ListBinder<?>) o).listKey.equals(listKey);
    }

    @Override public int hashCode() {
        return listKey.hashCode();
    }

    @Override public String toString() {
        return "ListBinder<"+elementType+">";
    }

    static void checkConfiguration(boolean condition, String format, Object... args) {
        if (condition) {
            return;
        }

        throw new ConfigurationException(Collections.singleton(new Message(Errors.format(format, args))));
    }

    @Qualifier
    @Target({ElementType.PARAMETER, ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME)
    private @interface ListElement {
        int priority();
        int uniqueId();
        String listName();
    }

    private class PriorityWrapper implements ListElement {
        private final int uniqueId;
        private final int priority;
        private String listName;

        public PriorityWrapper(int priority, String listName) {
            this.priority = priority;
            this.uniqueId = nextUniqueId.incrementAndGet();
            this.listName = listName;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return ListElement.class;
        }

        @Override
        public int priority() {
            return priority;
        }

        @Override
        public int uniqueId() {
            return uniqueId;
        }

        @Override
        public String listName() {
            return listName;
        }

        @Override
        public int hashCode() {
            return 127 * ("priority".hashCode() ^ priority)
                    + 127 * ("uniqueId".hashCode() ^ uniqueId)
                    + 127 * ("listName".hashCode() ^ listName.hashCode());
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ListElement
                    && ((ListElement) obj).priority() == priority()
                    && ((ListElement) obj).uniqueId() == uniqueId()
                    && ((ListElement) obj).listName().equals(listName());
        }

        @Override
        public String toString() {
            return "@"+ListElement.class.getName()+"(priority="+priority()+",uniqueId="+uniqueId()+",listName="+listName()+")";
        }
    }

    private class PriorityComparator implements Comparator<Binding<T>> {
        @Override
        public int compare(Binding<T> o1, Binding<T> o2) {
            int p1 = ((ListElement) o1.getKey().getAnnotation()).priority();
            int p2 = ((ListElement) o2.getKey().getAnnotation()).priority();
            if(p1 < p2)
                return -1;
            if(p1 > p2)
                return 1;
            else
                return 0;
        }
    }
}
