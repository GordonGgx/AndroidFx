package similar.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Application {

    /**
     * 应用包名
     * 如 com.xxx.app
     */
    String packageName();

    /**
     * 应用唯一图标 资源路径
     */
    String icon() default "icon/ic_launcher_round.png";

    String label() default "app_name";
}
