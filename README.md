# **Frame**
项目的基本框架


### 1.用法
使用前，对于Android Studio的用户，可以选择添加:
    
	compile 'com.github.ashLikun.frame:adapter:1.4.0'//万能适配器
	compile 'com.github.ashLikun.frame:adapterDatabind:1.4.0'//databind万能适配器
	compile 'com.github.ashLikun.frame:xrecycleview:1.4.0'//封装recycleView
	compile 'com.github.ashLikun.frame:stickyrecyclerview:1.4.0'//悬浮的头部
	compile 'com.github.ashLikun.frame:flatbutton:1.4.0'//按钮
	compile 'com.github.ashLikun.frame:loadingandretrymanager:1.4.0'//布局切换
	compile 'com.github.ashLikun.frame:numberprogressbar:1.4.0'//数字精度条
	compile 'com.github.ashLikun.frame:segmentcontrol:1.4.0'//多段选择
	compile 'com.github.ashLikun.frame:supergridlayout:1.4.0'//流布局和表格布局
	compile 'com.github.ashLikun.frame:banner:1.4.0'//广告条
	compile 'com.github.ashLikun.frame:charbar:1.4.0'//字母索引
	compile 'com.github.ashLikun.frame:supertoobar:1.4.0'//toobar
	compile 'com.github.ashLikun.frame:scaleimageview:1.4.0'//缩放的ImageView
	compile 'com.github.ashLikun.frame:wheelview3d:1.4.0'//3d滑轮
	compile 'com.github.ashLikun.frame:animcheckbox:1.4.0'//动画单选View
	compile 'com.github.ashLikun.frame:badgeview:1.4.0'//消息条数
	compile 'com.github.ashLikun.frame:baseresource:1.4.0'//基础的资源
	compile 'com.github.ashLikun.frame:pathanim:1.4.0'//path动画
	compile 'com.github.ashLikun.frame:http:1.4.0'//Retrofit封装
	compile 'com.github.ashLikun.frame:utils:1.4.0'//工具
	compile 'com.github.ashLikun.frame:glideutils:1.4.0'//glide封装

utils项目简介
##使用方法

build.gradle文件中添加:

   compile 'com.github.ashLikun.frame:utils:1.4.0'//工具

##详细介绍
utils是一系列通用类、辅助类、工具类的集合

其中包括bitmap处理，文件操作，加密存储器，shell命令，静默安装，计数器，均值器，吐司，日志，校验，提示，网络监测等基础功能，以及一些Base64、MD5、Hex、Byte、Number、Dialog、Filed、Class、Package、Telephone、Random等工具类。

1. async包：异步与并发
-----
- **AsyncExecutor**：   一个简单的可以自定义线程池并发执行器
2. log包：日志
-----
- **Log**：             一个和android系统日志类同名(方便快速替换)的Log工具类，不同的是这个Log具有一键开关功能，方便快速开发打开调试模式。
3. assit包：辅助
-----
- **Averager**：        均值器， 添加一些列数字或时间戳，获取其均值。
- **FlashLight**：      闪光灯， 开启、关闭闪光灯。
- **KeyguardLock**：    锁屏管理， 锁屏、禁用锁屏，判断是否锁屏
- **LogReader**：       日志捕获器， 将某个程序、级别的日志记录到sd卡中，方便远程调试。
- **Network**：         网络探测器， 判断网络是否打开、连接、可用，以及当前网络状态。
- **SilentInstaller**： 安装器， 静默安装、卸载（仅在root过的手机上）。
- **TimeAverager**：    计时均值器， 统计耗时的同时，多次调用可得出其花费时间均值。
- **TimeCounter**：     计时器， 顾名思义，统计耗时用的。
- **Toastor**：         吐司， 解决多次连续弹出提示问题，可只弹出最后一次，也可连续弹出轻量级提示。
- **WakeLock**：        屏幕管理， 点亮、关闭屏幕，判断屏幕是否点亮
4. data包：数据处理
-----
- **DataKeeper**：       加密存储器，持久化工具，可加密，更简单、安全的存储（持久化）、获取数字、布尔值、甚至对象。
- **chipher包**：        放置加解密辅助类。
5. io包：文件与IO
-----
- **Charsets**：         字节编码类
- **FilenameUtils**：    通用的文件名字、路径操作工具类
- **FileUtils**：        通用文件操作工具类
- **IOUtils**：          通用IO流操作工具类
- **StringCodingUtils**：字符串编码工具类
- **stream包**：         IO流操作辅助类
-
6. receiver包：通用广播接收器
-----
- **ScreenReceiver**：  屏幕接收器，可收到屏幕点亮、关闭的广播，并通过回调通知给调用者
- **PhoneReceiver**：      电话监听，来电、去电、通话、挂断的监听以及来去电话号码的获取。
- **SmsReceiver**：        短信接收器，可获取短信内容，发送者号码，短信中心号码等。

7. utils包：常用工具类
-----
- **AndroidUtil**：     android信息， 获取android手机品牌、商家、版本号等信息
- **AppUtil**：         app工具， 检测是否前台运行
- **BitmapUtil**：      位图操作， 拍照，裁剪，圆角，byte、string互转，压缩，放缩，保存等
- **ByteUtil**：        byte工具类
- **ClassUtil**：       类工具， 新建实例，判断类的类型等
- **DialogUtil**：      对话框工具类， 统一全局对话框
- **FieldUtil**：       属性工具类，获取属性值、获取属性泛型类型等
- **FileUtil**：        文件工具类
- **HexUtil**：         16进制工具类，16进制和byte、char像话转化
- **MD5Util**：         MD5工具类
- **NotificationUtil**：通知工具类，便捷显示到顶部栏
- **NumberUtil**：      数字工具类，各种数字安全转化
- **PackageUtil**：     应用程序类，打开、安装，卸载，启动应用以及获取应用信息
- **RandomUtil**：      随机工具类，产生随机string或数字，随机洗牌等
- **ShellUtil**：       shell 命令工具类
- **TelephoneUtil**：   电话工具类，手机号、运营商、IMEI、IMSI等信息
- **VibrateUtil**：     震动工具类，调用系统震动功能

8. service包：通用服务
-----
- **NotificationService**：通知监听，各类通知服务的监听，获取通知的简述、标题、内容等信息，可以获取诸如QQ、微信、淘宝、浏览器等所有的在通知栏提示的消息。

