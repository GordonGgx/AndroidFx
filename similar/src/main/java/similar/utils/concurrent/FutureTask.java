package similar.utils.concurrent;

/**
 * 可能携带异常信息的任务类
 *
 */
abstract class FutureTask implements Runnable{

    private Throwable ex;


    public Throwable getException() {
        return ex;
    }

    public void setException(Throwable ex) {
        this.ex = ex;
    }


}
