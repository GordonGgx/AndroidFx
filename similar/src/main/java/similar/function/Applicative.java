package similar.function;

public interface Applicative<T,R>{

    //(<*>) :: f (a -> b) -> f a -> f b
    Boxes.Box<R> ap(Boxes.Box<T> box);
}
