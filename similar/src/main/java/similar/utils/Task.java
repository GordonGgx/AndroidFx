package similar.utils;

import javafx.application.Platform;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class Task<T>  {

    enum IO{
        UI,WORK
    }

    public static final IO UI=IO.UI;
    public static final IO WORK=IO.WORK;

    private static final boolean USE_COMMON_POOL =
            (ForkJoinPool.getCommonPoolParallelism() > 1);

    private static final Executor ASYNC_POOL = USE_COMMON_POOL ?
            ForkJoinPool.commonPool() : new ThreadPerTaskExecutor();

    /** if ForkJoinPool.commonPool() cannot support parallelism */
    static final class ThreadPerTaskExecutor implements Executor {
        public void execute(Runnable r) { new Thread(r).start(); }
    }


    private T result;

    private IO io;

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


    public <U>Task<U> async(Supplier<U> fn){
        Objects.requireNonNull(fn);
        Task<U> task=new Task<>();
        task.runAs(IO.WORK);
        ASYNC_POOL.execute(new SupplyTask<>(task,fn));
        return task;
    }

    public <U>Task<U> andThen(Function<T,U> fn){
        Objects.requireNonNull(fn);
        Task<U> task=new Task<>();
        IO io=this.io;
        task.runAs(io);

        return task;
    }


    public final void completeValue(T t) {
        this.result=t;
    }

    private final void postComplete() {

    }

    public Task<T> runAs(IO io){
        this.io=io;
        return this;
    }

    static class SupplyTask<O> extends ForkJoinTask<Void>
                implements Runnable{
        Task<O> dep;
        Supplier<? extends O> fn;

        SupplyTask(Task<O> dep,Supplier<O> fn){
            this.dep = dep; this.fn = fn;
        }

        @Override
        public Void getRawResult() {
            return null;
        }

        @Override
        protected void setRawResult(Void value) {

        }

        @Override
        protected boolean exec() {
            run();
            return false;
        }

        @Override
        public void run() {
            try {
                dep.completeValue(fn.get());
            }catch (Throwable ex){
            }
            dep.postComplete();
        }
    }



}
