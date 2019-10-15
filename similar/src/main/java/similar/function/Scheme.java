package similar.function;

import javafx.application.Platform;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public abstract class Scheme {

    public static Scheme io(){

        return new IOScheme();
    }
    public static Scheme ui(){
        return new UIScheme();
    }

    abstract void execute(Runnable runnable);

    private static class IOScheme extends Scheme{
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
