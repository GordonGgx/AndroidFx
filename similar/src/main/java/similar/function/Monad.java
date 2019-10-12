package similar.function;

import java.util.function.Function;

public interface Monad<T> {
    //(>>=) :: m a -> (a -> m b) -> m b
    <R> Boxes.Box<R> bind(Function<T, Boxes.Box<R>> fn);

}
