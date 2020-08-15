# AndroidFx

[![](https://jitpack.io/v/GordonGgx/AndroidFx.svg)](https://jitpack.io/#GordonGgx/AndroidFx)


简化JavaFx的开发过程，提供方便使用的组件。
Api尽可能的模仿Android**

## Features
1. 提供SimilarPreloader
2. 提供AndroidApplication
3. 提供Activity支持，支持Activity生命周期
4. 提供本地广播（LocalBroadcast）
5. 异步任务支持（Task），支持FX->work线程互相切换
6. 提供盒子模型（Boxes），该类属于一个学习haskell过程中的玩具类，实现了如何在Java中使用Monad的编程风格，可以学习参考

## TODO
1. 数据持久化支持（ContentProvider）
2. 更多的方便控件
3. 场景切换动画支持
4. Service 支持

## 使用方式
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
        implementation 'com.github.GordonGgx:AndroidFx:Tag'
}
```

## Requirements
本框架建立于JDK14的基础上，作为客户端GUI请不要吝啬你的JDK版本

## Contact
如有遇到问题可以在issues中提问。
我的QQ群：392154157
推荐国内的JavaFx交流群：518914410。欢迎您的加入
