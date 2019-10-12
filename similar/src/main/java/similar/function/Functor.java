package similar.function;

import java.util.function.Function;

public interface Functor<T> {
    //fmap :: (a -> b) -> f a -> f b
    <B> Boxes.Box<B> fmap(Function<T, B> fn);
}
