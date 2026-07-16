# 寿州古城旅游管理系统
这是一个基于springboot+vue+redis的综合性旅游管理平台，以淮南市寿县为背景，涵盖前台游客展示与后台数据管理。
寿县古城旅游管理系统

技术架构

┌─────────────────────┐         ┌─────────────────────┐         ┌──────────┐
│   Vue 2.6 前端       │  CORS   │  Spring Boot 2.7    │  MyBatis │  MySQL   │
│   Element UI         │ ◄─────► │  tk.mybatis         │ ◄──────► │  5.x     │
│   ECharts 5          │  :8080  │  PageHelper         │          │  :3306   │
│   Axios              │         │  Fastjson           │          └──────────┘
│   端口 :8080          │         │  端口 :8088          │          ┌──────────┐
└─────────────────────┘         │  Redis 缓存(可选)     │          │  Redis   │
                                └─────────────────────┘          │  :6379   │
                                                                 └──────────┘
层级	技术
前端框架	Vue 2.6 + Vue Router + Vuex
UI 库	Element UI 2.14
图表	ECharts 5.5
HTTP	Axios 0.20
后端框架	Spring Boot 2.7.18
ORM	MyBatis + tk.mybatis（通用 Mapper）
分页	PageHelper
数据库	MySQL 5.x（数据库名 shou xian）
连接池	Druid
缓存	Redis（可选，用于首页热点数据）
构建	Maven（后端）+ Vue CLI 4.5（前端）
功能模块（14 个业务模块）
前台（游客端）：

首页轮播图展示
热门景点浏览与详情
地方美食浏览与详情
旅游线路浏览与详情
新闻资讯浏览与详情
留言板（查看/留言/回复）
用户注册/登录
旅游线路预订
收藏功能
后台（管理端）：

模块	功能
账号管理	管理员账号增删改、用户管理
地区管理	景点所属地区维护
景点信息管理	景点增删改查，图片上传
地方美食管理	美食增删改查
美食分类管理	分类维护
旅游线路管理	线路增删改查
订单管理	预订订单查询、支付确认
新闻管理	新闻分类+信息增删改查
轮播图管理	首页轮播图维护
友情链接管理	友情链接维护
留言管理	留言查看与回复
收藏记录管理	用户收藏查看
数据分析	ECharts 图表（景点收藏分析、线路预订分析）
后端分层架构

Controller (22个) → Service 接口 (15个) → ServiceImpl (14个) → Mapper (15个) → MySQL
                                    ↑                    ↑
                              IServiceBase          MapperBase<T>
                              (定义 CRUD 接口)       (tk.mybatis 通用 Mapper)
                                    ↑                    ↑
                              ServiceBase<E>        自动生成 SQL
                              (模板方法实现全部 CRUD)
每个业务模块的 Service 实现类只需 2 行代码（注入 Mapper + 返回 getDao()），CRUD 逻辑全部由 ServiceBase 基类提供。

核心特性
通用 CRUD 封装：基于模板方法模式，14 个业务模块复用同一套增删改查逻辑，减少 90% 重复代码
物理分页：PageHelper + Example 条件构造器，支持动态字段搜索和自定义排序
Token 认证：自定义 HandlerInterceptor，支持管理员/用户双角色登录
文件上传：支持图片上传，按日期分目录存储，相对路径写入数据库
富文本编辑：集成 UEditor，支持图文混排内容
首页缓存：Redis Cache-Aside 模式缓存首页 5 个热门模块，10 分钟自动过期
图表分析：ECharts 实现景点收藏分析和线路预订分析
CORS 跨域：开发环境下前端直连后端，无需 webpack proxy
数据库
16 张表，包括：admins、yonghu、jingdianxinxi、difangmeishi、meishifenlei、lvyouxianlu、yuding、xinwenfenlei、xinwenxinxi、lunbotu、diqu、liuyanban、youqinglianjie、shoucangjilu、token、dx。

默认账号
角色	用户名	密码
管理员	admin	admin
用户	currry	123
启动方式

# 终端 1：后端（需要 JDK 8+、MySQL 运行中）
cd bysj-server && mvn spring-boot:run

# 终端 2：前端
cd bysj-client && npm run serve

# 浏览器访问
http://localhost:8080        # 前台首页
http://localhost:8080/admin  # 后台管理系统
