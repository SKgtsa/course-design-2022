# 2022下半学期Web应用课设项目
## 前端
### 采用
- Vue3 框架
- axios: 处理异步请求
- Element-Plus: 组件帮助开发
- vue-router: 构建单页面应用
- ESLint: 监督代码质量
## 后端
### 采用
 - SpringBoot2 框架
 - LomBok :简化开发
 - MyBatis-Plus
 - SQLite
## 第一章 项目简介与系统总体要求
### 1.1 简介
 &emsp;&emsp;之前的课程中我们学习了基于Spring Boot框架的Web后端开发技术。在此基础上，我们在《Web技术》课程继续学习Web前端开发技术。<br/>
 &emsp;&emsp;Web前端有内容、样式、逻辑三个目标，分别使用HTML、CSS、JavaScript（JS）语言描述，在浏览器（Web Browser）中解释、渲染、执行。其中HTML、CSS为静态的描述型语言，使用文本编辑器编写、由浏览器展示和绘制。JS是专为Web设计的编程语言，运行在浏览器内置的解释器和运行时环境（Runtime Environment）中。Chrome浏览器代号V8的JS执行引擎由于出色的性能，演化出了可单独使用的Node.js运行时环境。<br/>
 &emsp;&emsp;业务功能的扩展大大增加了前端复杂度，其中又有很多相似的共性任务。为更好地重用代码、组织数据、高效通信、实现模块化、降低耦合性，人们开发了各种前端框架（framework），为前端开发提供完整的解决方案。主流框架有Angular、React、Vue等，本课程以Vue为例介绍前端框架的使用。
### 1.2 实验基本要求
 &emsp;&emsp;本次课程设计需要开发出一个教学信息管理平台，本平台需要能够实现对管理员、教师、学生各类功能的实现。体现于用户管理（登录、注册）、基本信息维护（修改密码、头像上传）、课程管理（增加、删除、修改、查找）、学工管理（社会实践、课外活动、成果奖励的增删改查）、学生主页（简历、博客等）、教师主页（简历、研究方向等），同时前端展示框架涉及到二级菜单导航、系统Logo、个人头像、分区布局显示。此外还注重于用户体验、数据设计的合理和规范、项目质量的保障。
### 1.3 环境说明
 **实验资源包**含有三个目录：  
 &emsp;&emsp;**java-server**是后台开发框架，基于Spring Boot框架的Java程序，也包含基于HTML的基础前端框架。可在IntelliJ IDEA中开发运行，也可导出JAR独立运行。  
 &emsp;&emsp;**java-client-expend**是基于Vue的前端开发框架和示例。可用VSCode软件开发，需要Node.js软件运行和调试。  
 &emsp;&emsp;**java-client**是前端测试框架。前后端通信使用JSON格式，HTTP的Content-Type为“application/json”、“multipart/form-data”。
### 1.4 开发进度表
|   阶段    |            主要工作             |    时间    |
|:-------:|:---------------------------:|:--------:|
| 系统需求分析  |  制定计划并确定系统功能要求<br/>确认开发计划   | 第一周至第二周  |
| 系统概要属性  | 建立系统总体结构、划分功能模块<br/>定义各功能接口 | 第三周至第四周  |
| 系统详细设计  |   设计各模块具体算法<br/>确定模块间详细接口   | 第五周至第七周  |
| 系统测试与修改 | 编写源代码、测试整个应用并适当<br/>编写开发报告  | 第七周至第十六周 |
## 第二章 系统需求分析
### 2.1 对功能的定义
 **1.&emsp;学生信息管理**  
 - 限于全体学生操作。  
 - 一般浏览者只能查看信息，不能对它做任何修改。而且不同的用户级别所看到的信息是有限制的。  

**2.&emsp;教师信息管理**  
 - 主要适合全体老师操作。  
 - 一般浏览者只能查看信息，不能对它做任何修改。  
 - 课程、成绩查询可以查询到老师课程的具体信息以及与此课程相关的学生该方面的信息。  

 **3.&emsp;操作管理**  
 - 仅限管理员。其中可以添加、修改、删除操作学生、老师的信息。  
## 第三章 架构设计
### 3.1 系统总体架构：
 &emsp;&emsp;本次课程设计为教学信息管理平台，即要从管理员端、教师端以及学生端三个方向进行设计。<br/>
 &emsp;&emsp;学生端和教师端都包含的主要操作是对于基本信息维护方面的信息修改。<br/>
 &emsp;&emsp;而教学信息管理平台大部分操作都是由管理员端对于各个用户端口信息的修改实现的，从而达到修改教师端、学生端的目的。<br/>
 &emsp;&emsp;整个系统是由多个功能模块组合而成的，要将所有的功能模块都一一列举出来，然后进行逐个的功能设计，使得每一个模块都有相对应的功能设计，然后进行系统整体的设计。<br/>
### 3.2 工作流程：
 &emsp;&emsp;操作者登录前端，选定用户进行相应的操作，其中学生只能对自己当前用户的部分功能和信息进行操作，老师可对教学班级下的学生的课程、成绩等系统进行操作以及对当前用户的部分信息进行操作，而管理员可以对所有教师和学生的各项功能和数据进行操作。进行操作时，对应的工作模块会对操作者输入的数据进行处理，经过处理的数据被传入和数据库相关联的接口并通过接口进入数据库，最后输入的数据会被送回对应的工作模块上并最终反映在前端。
## 第四章 功能介绍和突出技术点的介绍
### 4.1 功能介绍
**1.&emsp;头像与照片上传：**<br/>前端将token和avatar文件上传到后端的头像更改接口 后端会通过token获取到用户身份，然后将头像文件存储到服务器本地，并分配url，将url绑定给用户的avatarUrl属性。<br/>
**2.&emsp;对用户各项信息的管理功能（主要针对管理员权限）：**<br/>虽然用户有着各种各样的信息，但系统处理这些信息时采取的措施大致相同，具体措施如下：每个信息管理功能都有编辑与删除两个按键。操作者点击编辑，输入相关数据后提交，系统就会根据对应的ID将数据存储到数据库中准确的位置，并且当操作者再次进入该信息管理界面时，之前编辑的数据就会呈现在界面上。操作者点击某个信息的删除按键后，该信息的所有数据就会在数据库中被删除，且不会再呈现在界面上。<br/>
**3.&emsp;二级菜单：**<br/>在对数据对象的操作中，对于存储结构较复杂的对象，采取二级菜单的方式提高操作友好性。<br/>
**4.&emsp;学生画像的实现方法：**<br/>采用每次查看现统计的方法。<br/>
**5.&emsp;学生互评：**<br/>给学生对象增加了evaluateNum 代表他给别人写的评价的个数 来解决互评到5人以上的问题。<br/>
### 4.2 突出技术点的介绍
**1.**&emsp;后端引入了EasyExcel 可以自动对excel表格进行处理，起到批量注册学生账户 批量录入成绩的功能。<br/>
**2.**&emsp;前端引入了TinyMCE富文本编辑器 可以提供友好的人机交互界面，同时有着强大的富文本编辑功能。<br/>
**3.**&emsp;后端登录状态维持工具 基于链表实现，按时间顺序向后增加。每次查询登陆状态时从最早的状态开始遍历，同时删除过期状态。能够实现生成 临时存储 验证手机验证码的功能，并且在登陆后能够不进行数据库查询验证登陆状态并获取用户id，解决了登录唯一问题。<br/>
**4.**&emsp;后端的id生成 对于数据量比较多的数据库对象采用了雪花id。<br/>
**5.**&emsp;后端能够接收图片并映射url ，同时充当图床，前后端可独立运行。<br/>
**6.**&emsp;引入了腾讯云短信api 可以通过短信的方式进行防刷验证以及手机验证码登录。<br/>
**7.**&emsp;引入腾讯云图形验证，作为账号密码登录时的机器人验证。<br/>
**8.**&emsp;后端有sql防注入设计 包括 严格限制传参数据类型 所有数据库操作都在token验证成果后才执行. 对于String参数 先检查格式，然后进行数据库查找。使用@Query注解，起到了预编译的效果。<br/>
**9.**&emsp;通过nginx反向代理，实现了在一台服务器上同时架设前端和后端，并对应不同的域名。<br/>
**10.**&emsp;通过在后端设置CrossOrigin 解决了前后端交互时的跨域问题。<br/>
**11.**&emsp;通过SHA3散列加密对密码加密存储，保证了数据的安全性。<br/>
**12.**&emsp;前后端均已上线：<br/>&emsp;&emsp; 前端[https://coursedesign.clankalliance.cn](https://coursedesign.clankalliance.cn) <br/>
&emsp;&emsp; 后端[https://courseback.clankalliance.cn](https://courseback.clankalliance.cn) <br/>&emsp;&emsp; 均配置了https证书，保证前后端通讯 安全性。<br/>
**13.**&emsp;引入cdn内容分发网络，提高前端的响应速度。<br/>
# 一定不要将后端的application.yml上传git!!!
# 包含腾讯云密钥 会导致严重数据泄露事故
