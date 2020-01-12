package factories;

import org.mockito.ArgumentMatcher;

import java.util.function.Predicate;

public class LambdaMatcher<T> implements ArgumentMatcher<T> {

    private final Predicate<T> predicate;
    private final String description;

    public LambdaMatcher(Predicate<T> predicate){
        this(predicate, "");
    }

    public LambdaMatcher(Predicate<T> predicate, String description){
        this.predicate = predicate;
        this.description = description;
    }

    @Override
    public boolean matches(T t) {
        return this.predicate.test(t);
    }
}
