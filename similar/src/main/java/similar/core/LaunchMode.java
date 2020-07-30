package similar.core;

public enum LaunchMode {
    /**
     * Default
     * 标准模式
     * 标记此模式的Activity每次都会创建一个新的实例
     */
    STANDARD,

    /**
     * 栈顶复用模式
     * 标记为此模式的Activity如果存在于栈顶则不会创建新的实例
     */
    SIGNAL_TOP,

    /**
     * 栈内唯一模式
     * 标记此模式的Activity，如果存在站内则不会创建新的实例，且每次都会将栈内在其只上的Activity出栈
     */
    SIGNAL_TASK
}
