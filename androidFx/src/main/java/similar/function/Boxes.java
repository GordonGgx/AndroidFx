package similar.function;

import java.util.function.Function;

/**
 * 盒子类型,盒子里可以装任意值，当然也能装函数<br>
 * example:<br>
 * <pre><code>
 *  Boxes.box(5).bind(Main::succ).get()
 *  public static Boxes.Box<Integer> succ(int in){
 *       return Boxes.box(in+1);
 *  }
 * 1. Java Curry
 * {@code Function<Integer,Function<Integer,Integer>> add=x->y->x+y;}
 * 2. Applicative
 * {@code
 * Boxes.box(add).ap(Boxes.box(1)).as(Boxes::box).ap(Boxes.box(2));
 * Function<Integer,Function<Integer,Function<Integer,Integer>>> add=x->y->z->x+y+z;
 * int val=Boxes.box(add)
 *              .ap(Boxes.box(1)).as(Boxes::box)
 *              .ap(Boxes.box(3)).as(Boxes::box)
 *              .ap(Boxes.box(5)).as(Boxes::box)
 *              .get();
 *  List<Integer> list=new ArrayList<>();
 *  list.add(3);
 *  list.add(5);
 *  list.add(7);
 *  String data=Boxes.box(Main::head)
 *                 .compose(Main::sub)
 *                 .compose(Main::convertString)
 *                 .ap(Boxes.box(list))
 *                 .get();
 *  public static int head(List<Integer> array){
 *       return array.get(0);
 *  }
 *  public static int sub(int i){
 *       return i-1;
 *  }
 *  public static String convertString(int val){
 *       return String.valueOf(val);
 *  }
 * }
 *</code></pre>
 * @author ggx
 * @version 1.0
 * @since 1.0 2019/10/22
 */
public final class Boxes {

    public static <R> Box<R> box(R value){
        return new Box<>(value);
    }

    public static <I,O> Ap<I,O> box(Function<I,O> value){
        return new Ap<>(value);
    }



    public final static class Box<T> implements Functor<T>,Monad<T>{
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

    public final static class Ap<T,R> implements Applicative<T,R>,ApMonad<T,R>{
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
