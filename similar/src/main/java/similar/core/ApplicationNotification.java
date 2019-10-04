package similar.core;

import javafx.application.Preloader;

class ApplicationNotification implements Preloader.PreloaderNotification {

    private SimilarApplication application;

    public ApplicationNotification(SimilarApplication application) {
        this.application = application;
    }

    public SimilarApplication getApplication() {
        return application;
    }
}
