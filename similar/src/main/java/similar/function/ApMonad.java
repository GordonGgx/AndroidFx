package similar.function;

import java.util.function.Function;

public interface ApMonad<T,R> {
    //(>>=) :: m (a->b) -> ((a-b)) -> m (b->c)) -> m (b->c)
    //在Ap中如何盒子内仍然是一个函数，那么Ap中的monad表现应该是返回一个组合函数的盒子
    <F> Boxes.Ap<T,F> compose(Function<R, F> fn);
}
