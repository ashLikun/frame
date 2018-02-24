

[![Release](https://jitpack.io/v/ashLikun/frame.svg)](https://jitpack.io/#ashLikun/PhotoView)


# **Frame**
项目的基本框架
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
    	compile 'com.github.ashLikun.frame:stickyrecyclerview::{latest version}'//悬浮的头部
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
    	compile 'com.github.ashLikun.frame:http:{latest version}'//Retrofit封装
    	compile 'com.github.ashLikun.frame:glideutils:{latest version}'//glide封装
}
```
#Glide库的混淆
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#OkHttp库的混淆
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault

#支持java8
compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
}

