[![Release](https://jitpack.io/v/ashLikun/frame.svg)](https://jitpack.io/#ashLikun/frame)

# **Frame**
项目的基本框架

CustomMvp项目简介
    项目Mvp框架
    基类activity和fragment
## 使用方法

build.gradle文件中添加:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
并且:

```gradle
dependencies {
    compile 'com.github.ashLikun.frame:adapter:{latest version}'//万能适配器
    compile 'com.github.ashLikun.frame:adapterDatabind:{latest version}'//databind万能适配器
    compile 'com.github.ashLikun.frame:xrecycleview:{latest version}'//封装recycleView
    compile 'com.github.ashLikun.frame:stickyrecyclerview:{latest version}'//悬浮的头部
    compile 'com.github.ashLikun.frame:flatbutton:{latest version}'//按钮
    compile 'com.github.ashLikun.frame:loadingandretrymanager:{latest version}'//布局切换
    compile 'com.github.ashLikun.frame:numberprogressbar:{latest version}'//数字精度条
    compile 'com.github.ashLikun.frame:segmentcontrol:{latest version}'//多段选择
    compile 'com.github.ashLikun.frame:supergridlayout:{latest version}'//流布局和表格布局
    compile 'com.github.ashLikun.frame:banner:{latest version}'//广告条
    compile 'com.github.ashLikun.frame:charbar:{latest version}'//字母索引
    compile 'com.github.ashLikun.frame:supertoobar:{latest version}'//toobar
    compile 'com.github.ashLikun.frame:scaleimageview:{latest version}'//缩放的ImageView
    compile 'com.github.ashLikun.frame:wheelview3d:{latest version}'//3d滑轮
    compile 'com.github.ashLikun.frame:animcheckbox:{latest version}'//动画单选View
    compile 'com.github.ashLikun.frame:badgeview:{latest version}'//消息条数
    compile 'com.github.ashLikun.frame:baseresource:{latest version}'//基础的资源
    compile 'com.github.ashLikun.frame:pathanim:{latest version}'//path动画
    compile 'com.github.ashLikun.frame:okhttputils:{latest version}'//okhttp封装
    compile 'com.github.ashLikun.frame:http:{latest version}'//Retrofit封装
    compile 'com.github.ashLikun.frame:utils:{latest version}'//工具
    compile 'com.github.ashLikun.frame:glideutils:{latest version}'//glide封装
}
```


### 混肴
#### Okhttputils
        -dontwarn okio.**
        -dontwarn javax.annotation.Nullable
        -dontwarn javax.annotation.ParametersAreNonnullByDefault
#### adapter

