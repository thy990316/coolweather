db包      用于存放数据库模型相关的代码
gson包    用于存放GSON模型相关的代码
service包 用于存放服务相关的代码
util包    用于存放工具相关的代码

app/build.gradle
LitePal   用于对数据库进行操作
OkHttp    用于进行网络请求
GSON      用于解析JSON数据
Glide     用于加载和展示图片。

创建三个实体类Province、City、County

创建assets>litepal.xml
<?xml version="1.0" encoding="utf-8"?>
<litepal>
    <dbname value="cool_weather"></dbname>
    <version value="1"></version>
    <list>
        <mapping class="com.example.coolweather.db.Province"></mapping>
        <mapping class="com.example.coolweather.db.City"></mapping>
        <mapping class="com.example.coolweather.db.County"></mapping>
    </list>
</litepal>

在AndroidManifest.xml里面加入
 android:name="org.litepal.LitePalApplication"

这样我们就将所有的配置都完成了，数据库和表会在首次执行任意数据库操作的时候自动创建。

===========================================================================
  我们已经知道，全国所有省市县的数据都是从服务器端获取到的，因此这里和服务器的交互是必不可少的
  在util包下增加一个HttpUtil类

  由于服务器返回的省市县数据都是JSON格式的，所以我们最好再提供一个工具类来解析和处理这种数据。
  在util包下新建一个Utility类

  需要准备的工具类就这么多,现在可以开始写界面了。由于遍历全国省市县的功能我们在后面还会复用，因此就不写在活动里面了
  而是写在碎片里面，这样需要复用的时候直接在布局里面引用碎片就可以了。
  在res/layout目录中新建choose_area.xml布局

  接下来也是最关键的一步，我们需要编写用于遍历省市县数据的碎片了。
  新建ChooseAreaFragment继承自Fragment

  这样我们就把遍历全国省市县的功能完成了，可是碎片是不能直接显示在界面上的，因此我
  们还需要把它添加到活动里才行。修改activity_ main.xml 中的代码

  另外，我们刚才在碎片的布局里面已经自定义了一个标题栏，因此就不再需要原生的
  ActionBar了，修改res/values/styles.xml中的代码
  <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">

  声明程序所需要的权限。修改AndroidManifest.xml中的代码
    <uses-permission android:name="android.permission.INTERNET"/>

=============================================================================

  1、定义GSON实体类
  在gson包下建立一个Basic类、AQI类、Now类、Suggestion类、Forecast类。
    创建一个总的实例类(Weather类)来引用各个实体类

  2、编写天气页面
     创建WeatherActivity------activity_weather.xml
     将界面的不同部分写在不同的布局文件里面，再引入布局的方式集成到上面的xml文件
     title.xml          头布局中放置了两个TextView,一个居中显示城市名，一个居右显示更新时间。

     now.xml            当前天气信息的布局中放置了两个TextView, 一个用于显示当前气温, 一个用于显示天气概况。

     forecast.xml       未来几天天气信息布局的最外层使用LinearLayout定义了一个半透明的背景，然后使用TextView
                        定义了一个标题，接着又使用一个LinearLayout定义了一个用于显示未来几天天气信息的布局。
                        不过这个布局中并没有放入任何内容，因为这是要根据服务器返回的数据在代码中动态添加的。

     forecast_item.xml  子项布局中放置了4个TextView,一个用于显示天气预报日期，一个用于显示天气概况，另
                        外两个分别用于显示当天的最高温度和最低温度。

     aqi.xml            空气质量信息布局使用LinearLayout定义了一个半透明的背景,然后使用TextView定义了一个
                        标题。接下来，这里使用LinearLayout和RelativeLayout嵌套的方式实现了一个左右平分并且
                        居中对齐的布局，分别用于显示AQI指数和PM 2.5指数。

     suggestion.xml     生活建议信息布局先定义了一个半透明的背景和一个标题，然后下面使用了3个TextView分别
                        用于显示舒适度、洗车指数和运动建议的相关数据。

     天气界面上每个部分的布局文件都编写好了，引入到activity_weather.xml
        首先最外层布局使用了一个FrameLayout,并将它的背景色设置成colorPrimary。然后在FrameLayout中嵌套了一个
     ScrollView, 这是因为天气界面中的内容比较多，使用ScrollView可以允许我们通过滚动的方式查看屏幕以外的内容。
     ScrollView的内部只允许存在一个直接子布局.

  3、将天气显示在界面上
     首先需要在Utility类中添加一个用于解析天气JSON数据的方法.

     接下来的工作是我们如何在活动中去请求天气数据，以及将数据展示到界面上。修改WeatherActivity中的代码.

     处理完了WeatherActivity中的逻辑,接下来我们要做的,就是如何从省市县列表界面跳转到天气界面了，修改ChooseAreaFragment中的代码

     另外，我们还需要在MainActivity中加入一个缓存数据的判断才行。修改MainActivity

  4、获取必应每日一图
     首先修改activity_weather.xml中的代码
        这里我们在FrameLayout中添加了一个ImageView, 并且将它的宽和高都设置成match_parent。
        由于FrameLayout默认情况下会将控件都放置在左上角，因此ScrollView会完全覆盖住ImageView,从而ImageView也就成为背景图片了。

     接着修改WeatherActivity中的代码
        尝试从SharedPreferences中读取缓存的背景图片。如果有缓存的话就直接使用Glide来加载这张图片，
        如果没有的话就调用loadBingPic()方法去请求今日的必应背景图。

        loadBingPic()方法,先是调用了HttpUtil.send0kHttpRequest()方法获取到必应背景图的链接，
        然后将这个链接缓存到SharedPreferences当中,再将当前线程切换到主线程，最后使用Glide来加载这张图片就可以了。
        另外需要注意，在requestWeather()方法的最后也需要调用一下loadBingPic()方法，这样在每次请求天气信息的时候同时也会刷新背景图片。

     解决背景图并没有和状态栏融合到一起的问题！！！
        修改WeatherActivity中的代码

     天气界面的头布局几乎和系统状态栏紧贴到一起了
        借助android:fitsSystemWindows属性就可以了。修改activity_weather.xml中的代码。
        表示会为系统状态栏留出空间

=========================================================================

手动更新天气和切换城市
   1、手动更新天气   采用下拉刷新的方式
      首先修改activity_weather.xml中的代码
          在ScrollView的外面又嵌套了一层SwipeRefreshLayout,这样ScrollView就自动拥有下拉刷新功能了。

      然后修改WeatherActivity中的代码，加入更新天气的处理逻辑
         首先在onCreate()方法中获取到了SwipeRefreshLayout的实例，
         然后调用setColorSchemeResources()方法来设置下拉刷新进度条的颜色,使用主题中的colorPrimary作为进度条的颜色。
         接着定义了一个weatherId变量,用于记录城市的天气id,然后调用set0nRefreshListener()方法来设置一个下拉刷新的监听器,
         当触发了下拉刷新操作的时候,就会回调这个监听器的onRefresh()方法,我们在这里去调用requestWeather()方法请求天气信息就可以了。
         当请求结束后，还需要调用SwipeRefreshLayout的setRefreshing()方法并传人false,用于表示刷新事件结束，并隐藏刷新进度条。

   2、切换城市
      首先按照Material Design的建议，我们需要在头布局中加入一个切换城市的按钮，
      不然的话用户可能根本就不知道屏幕的左侧边缘是可以拖动的。修改title.xml中的代码
          添加了一个Button作为切换城市的按钮，并且让它居左显示。准备好了一张图片来作为按钮的背景图。

      修改activity_weather.xml布局来加人滑动菜单功能
          我们在SwipeRefreshLayout的外面又嵌套了一层DrawerLayout。
          DrawerLayout中的第一个子控件用于作为主屏幕中显示的内容，第二个子控件用于作为滑动菜单中显示的内容
          因此这里我们在第二个子控件的位置添加了用于遍历省市县数据的碎片。

      接下来需要在WeatherActivity中加入滑动菜单的逻辑处理，修改WeatherActivity中的代码
          首先在onCreate()方法中获取到新增的DrawerLayout和Button的实例，然后在
          Button的点击事件中调用DrawerLayout的openDrawer()方法来打开滑动菜单就可以了。

          我们还需要处理切换城市后的逻辑才行。这个工作就必须要在ChooseAreaFragment中进行了，
          因为之前选中了某个城市后是跳转到WeatherActivity的,而现在由于我们本来就是在WeatherActivity当中的,
          因此并不需要跳转，只是去请求新选择城市的天气信息就可以了。

      根据ChooseAreaFragment的不同状态来进行不同的逻辑处理，修改ChooseAreaFragment中的代码
          instanceof关键字可以用来判断一个对象是否属于某个类的实例。我们在碎片中调用getActivity()方法，
          然后配合instanceof关键字，就能轻松判断出该碎片是在MainActivity当中，还是在WeatherActivity当中。
          如果是在MainActivity当中，那么处理逻辑不变。如果是在WeatherActivity当中，那么就关闭滑动菜单，
          显示下拉刷新进度条，然后请求新城市的天气信息。

================================================================================
后台自动更新天气
    为了要让酷欧天气更加智能，保证用户每次打开软件时看到的都是最新的天气信息。
    要想实现上述功能，就需要创建一个长期在后台运行的定时任务

      首先在service包下新建一个服务，右击com.example.coolWeather.service-→New→Service→Service，
      创建一个AutoUpdateService，并将Exported和Enabled这两个属性都勾中。然后修改AutoUpdateService中的代码，
          在onStartCommand( )方法中先是调用了updateWeather()方法来更新天气,后调用了updateBingPic()方法来更新背景图片。
          这里我们将更新后的数据直接存储到SharedPreferences文件中就可以了,因为打开WeatherActivity的时候都会优先从SharedPreferences
          缓存中读取数据。

          为了保证软件不会消耗过多的流量,这里将时间间隔设置为8小时, 8小时后AutoUpdateReceiver的onStartCommand()方法就会重新执行，
          这样也就实现后台定时更新的功能了。

      我们还需要在代码某处去激活AutoUpdateService这个服务才行。修改WeatherActivity中的代码

===================================================================================
修改名称和图标
      修改AndroidManifest.xml中的代码    android:icon="@mipmap/logo"

      接下来我们修改一下程序的名称， 打开res/values/string.xml文件,其中app_name.
      对应的就是程序名称，将它修改成*****即可






