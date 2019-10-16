package similar.utils.concurrent;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>异步任务类，用来处理耗时任务代替{@link javafx.concurrent.Task}使用
 * 该类中提供了工作线程{@link Scheme#work()}到JavaFx UI{@link Scheme#ui()}线程的切换能力</p>
 * <h2>线程切换</h2>
 * <p>{@link Task#runAs(Scheme)}方法用来设置线程切换模式</p>
 * <pre><code>
 *  runAs(Scheme.work())
 *  runAs(Scheme.ui())
 * </code></pre>
 * <h2>基本使用方法</h2>
 * <p>
 *     该类用来执行异步耗时任务，结果可传递到下一个任务，<br>
 *     如果在执行任务过程中产生了异常，则中断后续的其他任务，最终交给exceptionally<br>
 *     如果当上个任务发生了异常仍就想继续执行后续的任务，则可以在上个任务后使用exceptionThen则可以捕获上一个任务异常，并允许传递一个正常结果给下一个任务<br>
 *     如果后续任务是exceptionThen，上个任务并没有发生异常，则该任务会将上个任务的执行结果传递给后续的任务
 * </p>
 * <pre><code>
 *     {@literal Task.async(()->{
 *             //耗时任务
 *             //...
 *             return "result";
 *         }).andThen(str->{
 *             //耗时任务
 *             //...
 *             return str.toUpperCase();
 *         }).runAs(Scheme.ui()).andThen(str->{
 *             //当前Javafx线程中，可以更新UI
 *             str.split("g")[7]="0";
 *             return str.toLowerCase();
 *         }).exceptionThen(throwable -> {
 *              //如果上一个任务发生了异常，仍就想继续执行后续的任务,可以使用此操作符
 *              //如果没有这个任务则其后的所有任务中断直接执行exceptionally，如果有改任务的话
 *             return "fixResult";
 *         }).runAs(Scheme.work()).andThen(str->{
 *             return str.split("g");
 *         }).runAs(Scheme.work()).andThen(strings -> {
 *             System.out.println(strings[7]);
 *         }).exceptionally(ex->{
 *              //异常总汇
 *             System.out.println(Thread.currentThread().getName());
 *             ex.printStackTrace();
 *         });}
 * </code></pre>
 *
 * @param <T>
 * @author ggx
 * @version 1.0 2019-10-16
 */
public class Task<T>  {

    private T result;


    /**
     * 线程切换模式
     * @see Scheme
     */
    private Scheme scheme;

    /**
     * 未来需要执行的任务
     */
    private FutureTask pending;


    private Task(){

    }

    ////////////////////////////////////////////////////////////
    //                    Static method                       //
    ////////////////////////////////////////////////////////////
    public static  <U>Task<U> async(Supplier<U> fn){
        Objects.requireNonNull(fn);
        Task<U> task=new Task<>();
        task.runAs(Scheme.work());
        task.scheme.execute(new SupplyTask<>(task,fn));
        return task;
    }

    public <U>Task<U> andThen(Function<T,U> fn){
        Objects.requireNonNull(fn);
        Task<U> task=new Task<>();
        task.runAs(scheme);
        FunctionTask<T,U> runnable=new FunctionTask<>(this,task,fn);
        tryExecute(runnable);
        return task;
    }
    public <U>Task<U> andThen(Supplier<U> fn){
        Objects.requireNonNull(fn);
        Task<U> task=new Task<>();
        task.runAs(scheme);
        SupplyTask<U> runnable=new SupplyTask<>(task,fn);
        tryExecute(runnable);
        return task;
    }
    public Task<Void> andThen(Consumer<T> fn){
        Objects.requireNonNull(fn);
        Task<Void> task=new Task<>();
        task.runAs(scheme);
        AcceptTask<T> runnable=new AcceptTask<>(this,task,fn);
        tryExecute(runnable);
        return task;
    }


    private  void completeValue(T t) {
        this.result=t;
    }

    private  void completeThrowable(Throwable t) {
        //将当前任务的异常堆栈信息传递给下一个任务，如果下一个任务存在的话
        if(pending!=null){
            pending.setException(t);
        }
    }

    /**
     *  尝试执行任务如果当前任务结果不存在则将任务放置等待将来执行{@link Task#postComplete()}
     * @param runnable FutureTask
     * @see FutureTask
     */
    private void tryExecute(FutureTask runnable){
        if(result!=null){
            scheme.execute(runnable);
        }else {
            pending=runnable;
        }
    }

    /**
     * 当前任务完成尝试执行下一个任务，如果任务存在的话
     */
    private void postComplete() {
        if (pending!=null){
            //执行下一个任务
            scheme.execute(pending);
        }
    }

    /**
     * 任务链最终结果
     * @param fn Consumer
     * @see Consumer
     */
    public void exceptionally(Consumer<Throwable> fn){
        Objects.requireNonNull(fn);
        ExceptionTask runnable=new ExceptionTask(this,fn);
        tryExecute(runnable);
    }

    /**
     * 如果当前任务有异常发生可以在此方法里尝试处理异常后在返回正确的结果来保证任务链的正常执行
     * @param fn
     * @return Task
     */
    public Task<T> exceptionThen(Function<Throwable,? extends T> fn){
        Objects.requireNonNull(fn);
        Task<T> task=new Task<>();
        task.runAs(scheme);
        ExceptionThenTask<T> runnable=new ExceptionThenTask<>(this,task,fn);
        tryExecute(runnable);
        return task;
    }


    /**
     * 切换工作模式
     * @param scheme
     * @return
     * @see Scheme
     */
    public Task<T> runAs(Scheme scheme){
        this.scheme=scheme;
        return this;
    }

    /**
     * 无参数有返回值的任务
     * @param <O>
     */
    static class SupplyTask<O> extends FutureTask{
        Task<O> dep;
        Supplier<? extends O> fn;

        SupplyTask(Task<O> dep,Supplier<O> fn){
            this.dep = dep; this.fn = fn;
        }

        @Override
        public void run() {
            //如果有异常就任务结束
            if(getException()!=null){
                dep.completeThrowable(getException());
            }else {
                try {
                    dep.completeValue(fn.get());
                }catch (Throwable ex){
                    dep.completeThrowable(ex);
                }
            }
            dep.postComplete();
        }
    }

    /**
     * 有参数有返回值的任务
     * @param <I>
     * @param <O>
     */
    static class FunctionTask<I,O> extends FutureTask{
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
            if(getException()!=null){
                next.completeThrowable(getException());
            }else {
                try {
                    next.completeValue(fn.apply(current.result));
                }catch (Throwable ex){
                    next.completeThrowable(ex);
                }
            }
            next.postComplete();
        }
    }

    /**
     * 有参数无返回值的任务
     * @param <I>
     */
    static class AcceptTask<I> extends FutureTask{
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
            if(getException()!=null){
                next.completeThrowable(getException());
            }else {
                try {
                    fn.accept(current.result);
                }catch (Throwable ex){
                    next.completeThrowable(ex);
                }

            }
            next.postComplete();
        }
    }

    /**
     * 处理异常事件汇总，该任务是最终任务
     */
    static class ExceptionTask extends FutureTask{
        Task current;
        Consumer<Throwable> fn;

        ExceptionTask(Task current, Consumer<Throwable> fn){
            this.current=current;
            this.fn = fn;

        }

        @Override
        public void run() {
            //如果没有异常则直接退出任务
            if(getException()==null){
                return;
            }
            try {
                fn.accept(getException());
            }catch (Throwable ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * 异常处理的任务
     * @param <O>
     */
    static class ExceptionThenTask<O> extends FutureTask{
        Task<O> next;
        Task<O> current;
        Function<Throwable,? extends O> fn;

        ExceptionThenTask(Task<O> current, Task<O> next, Function<Throwable,? extends O> fn){
            this.current=current;
            this.fn = fn;
            this.next=next;
        }

        @Override
        public void run() {
            try {
                //如果当前任务发生异常则应用异常否则直接执行下一个任务
                if(getException()!=null){
                    next.completeValue(fn.apply(getException()));
                }else {
                    next.completeValue(current.result);
                }
            }catch (Throwable ex){
                next.completeThrowable(ex);
            }
            next.postComplete();
        }
    }
}
