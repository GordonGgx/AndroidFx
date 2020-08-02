package similar.core;

public interface ILifecycle {

    void onCreated();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
