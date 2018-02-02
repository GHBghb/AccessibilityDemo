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

