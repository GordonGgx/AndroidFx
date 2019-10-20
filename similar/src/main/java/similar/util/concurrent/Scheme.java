package similar.util.concurrent;

import javafx.application.Platform;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * 作业模式
 * {@link WorkScheme}
 * {@link UIScheme}
 */
public abstract class Scheme {

    /**
     * 创建工作线程的作业模式
     * @return 作业模式
     */
    public static Scheme work(){

        return new WorkScheme();
    }

    /**
     * 创建JavaFx主线程的作业模式
     * @return 作业模式
     */
    public static Scheme ui(){
        return new UIScheme();
    }

    abstract void execute(Runnable runnable);

    /**
     * 该工作模式使用{@link ForkJoinPool#commonPool()}创建如果当前CPU可用核心数<2,则直接使用{@link Thread}创建
     * @see ForkJoinPool
     */
    private static class WorkScheme extends Scheme{
        private static final boolean USE_COMMON_POOL =
                (ForkJoinPool.getCommonPoolParallelism() > 1);

        private static final Executor ASYNC_POOL = USE_COMMON_POOL ?
                ForkJoinPool.commonPool() : new ThreadPerTaskExecutor();

        static final class ThreadPerTaskExecutor implements Executor {
            public void execute(Runnable r) { new Thread(r).start(); }
        }
        @Override
        void execute(Runnable runnable){
            ASYNC_POOL.execute(runnable);
        }
    }

    /**
     * 该工作模式使用{@link Platform#runLater(Runnable)}包裹工作，
     * 如果当前执行现场已经是JavaFxApplicationThread则直接运行任务
     */
    private static class UIScheme extends Scheme{

        @Override
        void execute(Runnable runnable) {
            if(Platform.isFxApplicationThread()){
                runnable.run();
            }else {
                Platform.runLater(runnable);
            }
        }
    }
}
