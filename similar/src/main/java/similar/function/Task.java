package similar.function;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Task<T>  {

    private T result;
    private Throwable ex;

    private Scheme scheme;

    private Runnable pending;


    private Task(){

    }

    private Task(T result){
        this.result=result;
    }

    public static <U>Task<U> unit(){
        return new Task<>();
    }

    public static <U>Task<U> of(U result){
        return new Task<>(result);
    }


    public static  <U>Task<U> async(Supplier<U> fn){
        Objects.requireNonNull(fn);
        Task<U> task=new Task<>();
        task.runAs(Scheme.io());
        task.scheme.execute(new SupplyTask<>(task,fn));
        return task;
    }

    public <U>Task<U> andThen(Function<T,U> fn){
        Objects.requireNonNull(fn);
        Task<U> task=new Task<>();
        task.runAs(scheme);
        FunctionTask<T,U> runnable=new FunctionTask<>(this,task,fn);
        if(result!=null){
            task.scheme.execute(runnable);
        }else {
            pending=runnable;
        }
        return task;
    }
    public <U>Task<U> andThen(Supplier<U> fn){
        Objects.requireNonNull(fn);
        Task<U> task=new Task<>();
        task.runAs(scheme);
        SupplyTask<U> runnable=new SupplyTask<>(task,fn);
        if(result!=null){
            task.scheme.execute(runnable);
        }else {
            pending=runnable;
        }
        return task;
    }
    public Task<Void> andThen(Consumer<T> fn){
        Objects.requireNonNull(fn);
        Task<Void> task=new Task<>();
        task.runAs(scheme);
        AcceptTask<T> runnable=new AcceptTask<>(this,task,fn);
        if(result!=null){
            task.scheme.execute(runnable);
        }else {
            pending=runnable;
        }
        return task;
    }


    private  void completeValue(T t) {
        this.result=t;
    }

    private  void completeThrowable(Throwable t) {
        this.ex=t;
    }

    private  void postComplete() {
        if(ex!=null){

        }else {
            scheme.execute(pending);
        }
    }

    public Task<T> runAs(Scheme scheme){
        this.scheme=scheme;
        return this;
    }

    static class SupplyTask<O> implements Runnable{
        Task<O> dep;
        Supplier<? extends O> fn;

        SupplyTask(Task<O> dep,Supplier<O> fn){
            this.dep = dep; this.fn = fn;
        }

        @Override
        public void run() {
            //如果有异常就任务结束
            if(dep.ex!=null){
                return;
            }
            try {
                dep.completeValue(fn.get());
            }catch (Throwable ex){
                dep.completeThrowable(ex);
            }
            dep.postComplete();
        }
    }

    /**
     * 执行下个任务
     * @param <I>
     * @param <O>
     */
    static class FunctionTask<I,O> implements Runnable{
        Task<O> next;
        Task<I> current;
        Function<I,O> fn;

        FunctionTask(Task<I> current,Task<O> next,Function<I,O> fn){
            this.current=current;
            this.fn = fn;
            this.next=next;
        }

        @Override
        public void run() {
            //如果有异常就任务结束
            if(current.ex!=null){
                return;
            }
            try {
                next.completeValue(fn.apply(current.result));
            }catch (Throwable ex){
                next.completeThrowable(ex);
            }
            current.postComplete();
        }
    }


    static class AcceptTask<I> implements Runnable{
        Task<Void> next;
        Task<I> current;
        Consumer<I> fn;

        AcceptTask(Task<I> current,Task<Void> next,Consumer<I> fn){
            this.current=current;
            this.fn = fn;
            this.next=next;
        }

        @Override
        public void run() {
            //如果有异常就任务结束
            if(current.ex!=null){
                return;
            }
            try {
                fn.accept(current.result);
            }catch (Throwable ex){
                next.completeThrowable(ex);
            }
            current.postComplete();
        }
    }
}
