package similar.core.annotations;

import similar.core.LaunchMode;

import java.lang.annotation.*;

@Repeatable(Activities.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Activity {

    Class<? extends similar.core.Activity> name();

    LaunchMode lunchMode() default LaunchMode.STANDARD;

    boolean mainActivity() default false;
}
