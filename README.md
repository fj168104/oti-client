# oti-client
oti客户端样例

1、文件说明：
java文件夹：
ClientTest 是 客户端测试类 
ServerTest 是 服务端测试类

libs文件夹：
oti-client-0.0.1-SNAPSHOT.jar 是 oti客户端sdk 
mr-framework-tool-0.0.4.jar 是 基础库

resources文件夹：
config/db.setting 是默认的数据库配置
oti_config.xml 是 默认的接口详细配置（该文件可以通过服务端下载得到）

2、测试案例：
``  
**************** 
demo1: http协议， json报文，远程配置载入方式
运行 ServerTest.testDefault() 启动默认配置的服务端
运行 ClientTest.testDefault() 运行默认配置的客户端


demo2: http协议， json报文, 本地配置文件载入方式
运行 ServerTest.testDefault() 启动默认配置的服务端
运行 ClientTest.testLocalConfigLoad() 运行客户端


demo3: http协议， XML报文, 远程配置载入方式
运行 ServerTest.testXml() 启动XML配置的服务端
运行 ClientTest.testXMLParser() 运行XML配置的客户端
 
 
demo4: tcp协议， json报文, 远程配置载入方式
运行 ServerTest.testTcpAgent() 启动tcp协议的服务端
运行 ClientTest.testTcpAgent() 运行tcp协议的客户端


demo4: http协议， json报文, 外部设置数据源
运行 ServerTest.testDefault() 启动默认配置的服务端
运行 ClientTest.testOutterDatasource() 设置数据源后启动客户端

 ****************** 
 
``

2、日志：
本客户端做了兼容处理， 采用动态自动适配模式；
它会自动检测引入的日志框架包从而将日志输出到此框架；
对于日志框架的监测顺序是：
Slf4j(Logback) > Log4j > Log4j2 > Apache Commons Logging > JDK Logging > Console
无需做额外的配置可集成到项目中使用

3、 松散结构
客户端尽量使每个模块独立，不合其它部分产生依赖。 
系统主要包含如下模块：
解析器
协议代理
服务端处理Handler 
Field 
Message
Transaction
监听器Listener
OTIContainer
这些模块均可以定制实现， 插件式注入，无需改变其它模块即可使用。

4、依赖包：
demo项目中依赖于第三方jar
jetty 
jsoup 
c3p0
mysql-connector-java
lombok
用户给据需要自行引入即可




