# Handturn-znmj（智能门禁-HT）

#### 介绍
- Handturn-znmj项目为一个智能门禁项目，中途各种原因，导致项目搁置。现将此项目开源，提供给需要的人学习和参考、交流。
- 有兴趣的可加QQ群：645035348


项目主要分成三个模块:
- Handturn-znmj-App:APP端，主要提供用户注册和邀请功能，目前只对微信小程序进行了测试和调试。使用技术：uniapp.
- Handturn-znmj-Desktop:桌面中间服务端（windows），Java语言开发，使用者为终端设备的主控制服务端。提供用户人脸注册，设备视频监控。使用技术：awt（由于视频流问题，只能使用该老技术实现）、sqlite、websocket。
- Handturn-znmj-Web:后端云端服务，Java语言开发，可提供云端用户注册，设备授权功能。使用技术：springboot、mybatis、layui、websocket等。


#### 安装教程

1.  uniapp项目使用HBuilderX开发工具。
2.  java项目使用Ideal开发工具，环境JDK1.8。

#### 使用说明

中间件服务端
![输入图片说明](https://images.gitee.com/uploads/images/2021/0925/103439_6c01d418_599554.png "图片1.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0925/103544_f23b1915_599554.png "图片2.png")
云端控制台
![输入图片说明](https://images.gitee.com/uploads/images/2021/0925/103646_1b81ebae_599554.png "图片4.png")

