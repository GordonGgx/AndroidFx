package similar.function;

import java.util.function.Function;

public class Boxes {

    public static <R> Box<R> box(R value){
        return new Box<>(value);
    }

    public static <I,O> Ap<I,O> box(Function<I,O> value){
        return new Ap<>(value);
    }



    public static class Box<T> implements Functor<T>,Monad<T>{
        private final T value;

        private Box(T value){
            this.value=value;
        }

        @Override
        public <B> Box<B> fmap(Function<T, B> fn) {
            return box(fn.apply(value));
        }

        public <B> B as(Function<T, B> fn) {
            return fn.apply(value);
        }

        public T get(){
            return value;
        }

        @Override
        public <R> Box<R> bind(Function<T, Box<R>> fn) {
            return fn.apply(value);
        }
    }

    public static class Ap<T,R> implements Applicative<T,R>,ApMonad<T,R>{
        private final Box<Function<T,R>> fn;

        private Ap(Function<T,R> value){
            this.fn= new Box<>(value);
        }
        

        @Override
        public Box<R> ap(Box<T> box) {
            return box.fmap(fn.value);
        }


        @Override
        public <F> Ap<T, F> compose(Function<R, F> fn) {
//            return Boxes.box((T param)->fn.apply(this.fn.value.apply(param)));
            return Boxes.box(fn.compose(this.fn.value));
        }
    }
}
