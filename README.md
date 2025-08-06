# Look4Sat: 卫星追踪器

[![Look4Sat CI](https://github.com/rt-bishop/Look4Sat/actions/workflows/main.yml/badge.svg)](https://github.com/rt-bishop/Look4Sat/actions/workflows/main.yml)

[<img src="https://play.google.com/intl/en_gb/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" height="80">](https://play.google.com/store/apps/details?id=com.rtbishop.look4sat)
[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" alt="Get it on F-Droid" height="80">](https://f-droid.org/packages/com.rtbishop.look4sat/)

受 Gpredict 启发的 Android 无线电卫星追踪与过境预测工具

<p float="left">
<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/1.png" width="180"/>
<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/2.png" width="180"/>
<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/3.png" width="180"/>
<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/4.png" width="180">
</p>

### 轻松追踪卫星过境

借助 Celestrak 和 SatNOGS 提供的庞大数据库，您可以获取超过 5000 颗绕地球运行的活跃卫星信息。您可以通过卫星名称或 NORAD 编号搜索整个数据库。

卫星位置和过境情况是相对于您的位置计算的。为了获得可靠信息，请务必在设置菜单中使用 GPS 或 QTH 定位器设置观测位置。

该应用采用 Kotlin、协程（Coroutines）、架构组件（Architecture Components）和 Jetpack Navigation 构建。它现在是、将来也永远是完全无广告且开源的。

特别感谢 DownloadAstro 团队对这款应用的关注以及所发布的采访。

## 主要功能:

* 预测未来一周内的卫星位置和过境情况
* 显示当前活跃和即将到来的卫星过境列表
* 展示活跃过境的进度、极地轨迹和收发器信息
* 在地图上显示卫星的位置数据、覆盖范围和地面轨迹
* 支持通过 TXT 或 TLE 扩展名的文件导入自定义 TLE 数据
* 优先支持离线使用：所有计算均在离线状态下进行。建议每周更新一次 TLE 数据。
