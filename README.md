AccessibilityService继承了service，他是在后台运行的。

1、AccessibilityService是系统服务，该服务完全由系统管理,并遵循已有的服务周期.

2、开启一个服务只能由用户在设置中打开,而关闭则只能由用户在设置中关闭或者服务本身通过diableSelf()方法关闭

3、系统绑定该服务之后,会调用onServiceConnected()方法,这个方法可以被重写,在这里可以做一些初始化的操作.

4、在实际的操作实验中发现，即便手动开启该服务，在6.0以上的系统经过一段时间也会自动关闭。

在实际开发中，可以继承AccessibilityService类，然后有选择的实现其中的一部分函数，就可以实现一些特殊的功能。

AccessibilityService函数

1.onAccessibilityEvent(AccessibilityEvent event
)必须重写。AccessibilityEvent是一个事件类，里面封装了许多字段，表示各种不同的事件（通知、窗口内容）。形参event表示事件变化，接收来的AccessibilityEvent是可以经过过滤的，过滤是在配置工作时设置的。 

2.onInterrupt()必须重写。这个在系统想要中断AccessibilityService返给的响应时会调用。在整个生命周期里会被调用多次。

3.onServiceConnected()可选。在系统成功连接上这个AccessibilityService会调用。在这个方法里主要做初始化工作。

4.onUnbind()可选。在系统将要关闭这个AccessibilityService会被调用。在这个方法中主要做释放资源的工作。

声明

AccessibilityService和在menifest中声明其他service一样，但要额外做两件事

1.配置<intent-filter>,其name为固定的

2.声明BIND_ACCESSIBILITY_SERVICE权限,

<service
    android:name=".AutoReplyService"
    android:enabled="true"
    android:exported="true"
    android:label="@string/app_name"
  android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE">
    <intent-filter>
        <action android:name="android.accessibilityservice.AccessibilityService"/>
    </intent-filter>
</service>

配置

AccessibilityService可以添加一些配置信息，目的是只接收一些特定的事件.例如：监听特定的包、android给我们提供2种配置方法：

方法1：meta-data标签方式：

在manifest声明的servce中提供一个meta-data标签,然后通过android:resource指定相应的配置文件(在res目录下创建xml文件,并在其中创建配置文件accessibilityservice.xml):

<meta-data
    android:name="android.accessibilityservice"
    android:resource="@xml/auto_reply_service_config"/>

方法2：调用setServiceInfo(AccessibilityServiceInfo)方式：AccessibilityServiceInfo用于配置AccessibilityService信息,类中包含了大量用于配置的常量字段。

1.这个方法可以在任何时候调用，动态的去改变service配置信息

2.这个方法只能用来配置动态属性，如：eventTypes，feedbackType，flags，notificationTimeout等。

通常是在onServiceConnected()进行配置
启动服务

当我们配置完成，进行运行后就可以安装到手机.安装成功后,在设置->辅助功能中便可以找到我们的服务.该服务默认处在关闭状态,需要手动开启.

获取界面指定控件

Android中的View体系是一个树形结构，所以每一个View就是一个节点。安卓系统提供了两个方法让我们来进行查找想要的节点View

第一种是通过节点View的Text内容来查找

findAccessibilityNodeInfosByText("查找内容") 这种方式查找，对于有文本内容的哪些控件，可以使用这种方式快速的找到。

第二种是通过节点View在xml布局中的id名称

findAccessibilityNodeInfosByViewId("@id/xxx")我们可以通过HierarchyView查找指定控件的id，

模拟点击

在找到指定的View节点，调用方法模拟事件：

performAction(AccessibilityNodeInfo.ACTION_CLICK)调用这个方法，AccessibilityNodeInfo类中封装了与控件有关的相关字段和方法，这里的参数就是指定事件的名称，这个和AccessibilityEvent中监听的那些事件是一一对应的，这里是模拟点击事件，同理也可以模拟View的滚动事件，长按事件等。

实例

QQ自动回复：实现QQ自动回复，其实就是监听QQ的包名以及通知变化，当QQ来消息的时候，自动跳到聊天界面，填充消息并点击发送。（类似于自动化测试）

具体步骤：

1.      监听QQ发出的Notification，此时要判断是否锁屏状态

2.      点击Notification的contentIntent属性，进入到QQ详情页

3.      从根节点开始，查找指定的edittext，并进行内容填充。



5. findAccessibilityNodeInfosByText（发送）

调用performAction(AccessibilityNodeInfo.ACTION_CLICK)实现模拟点击



总结一下，辅助功能的大概思路

第一、有选择的监控界面变化，并进行通知，有需要的进行跳转

第二、当有event发生时，寻找到我们想要的View节点

第三、模拟操作，实现特定功能


坑点

1.包被混淆，之前通过AndroidDevice Monitor查看ID，然后根据findAccessbilityNodeInfoById ()去匹配节点，包被混淆之后每个版本不一样

2.很难找出没有文本的控件，如EditText，ImageView

3、部分手机在手动开启一段时间后自动关闭，导致部分功能不稳定。

4、对于有手机锁屏密码的无法打开其屏幕

用途：

辅助功能还可以进行一些其他的功能：

1、  微信抢红包：监听微信红包，当收到红包时,自动模拟点击

2、  强制停止应用：我们可以监听系统的应用详情页面，然后找到：结束运行的节点View，然后模拟点击即可

3、  检测自己的app是否在前台。

