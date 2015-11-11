# DroidFix

# MoreInfo
[详细原理](https://mp.weixin.qq.com/s?__biz=MzI1MTA1MzM2Nw==&mid=400118620&idx=1&sn=b4fdd5055731290eef12ad0d17f39d4a&scene=1&srcid=1106Imu9ZgwybID13e7y2nEi#wechat_redirect)

解决方案
        该方案基于的是android dex分包方案的，关于dex分包方案，网上有几篇解释了，所以这里就不再赘述，[具体可以看这里](https://m.oschina.net/blog/308583)
    
       一个ClassLoader可以包含多个dex文件，每个dex文件是一个Element，多个dex文件排列成一个有序的数组dexElements，当找类的时候，会按顺序遍历dex文件，然后从当前遍历的dex文件中找类，如果找类则返回，如果找不到从下一个dex文件继续查找。    

 所以为了实现补丁方案，所以必须从这些方法中入手，防止类被打上CLASS_ISPREVERIFIED标志。
        最终空间的方案是往所有类的构造函数里面插入了一段代码，代码如下：
        
        
```        
if (ClassVerifier.PREVENT_VERIFY) {
    System.out.println(AntilazyLoad.class);
}
```

  其中AntilazyLoad类会被打包成单独的antilazy.dex，这样当安装apk的时候，classes.dex内的类都会引用一个在不相同dex中的AntilazyLoad类，这样就防止了类被打上CLASS_ISPREVERIFIED的标志了，只要没被打上这个标志的类都可以进行打补丁操作。
        然后在应用启动的时候加载进来.AntilazyLoad类所在的dex包必须被先加载进来,不然AntilazyLoad类会被标记为不存在，即使后续加载了hack.dex包，那么他也是不存在的，这样屏幕就会出现茫茫多的类AntilazyLoad找不到的log。
        所以Application作为应用的入口不能插入这段代码。（因为载入hack.dex的代码是在Application中onCreate中执行的，如果在Application的构造函数里面插入了这段代码，那么就是在hack.dex加载之前就使用该类，该类一次找不到，会被永远的打上找不到的标志)
        
# How
      1 正式包会生成一份缓存文件，里面记录了所有class文件的md5，还有一份mapping混淆文件。
      ２. 在后续的版本中使用-applymapping选项，应用正式版本的mapping文件，然后计算编译完成后的class文件的md5和正式版本进行比较，把不相同的class文件打包成补丁包。