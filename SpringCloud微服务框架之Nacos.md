# SpringCloud微服务框架之Nacos服务注册与发现

##### Nacos由服务端与客户端两部分组成。

1. Nacos服务端的搭建，参考《Nacos的安装与启动.md》
2. Nacos客户端的应用。

#### 为什么要使用Nacos？

在微服务的架构中，每一个单独的服务都视为微服务架构中的一个子服务。这些大大小小的服务构成了庞大的系统。但是这些服务与服务之间的交互就变成了一个复杂而难以维护的事情。

比如说A服务调用B服务的API。按照传统的做法是，在A服务的配置文件中，配置好B服务的接口地址，然后在A服务中发送Http进行访问。但是假如B服务有N多个集群节点，这时候A服务怎么办？是不是把每个服务的节点都需要配置下来？又假如B服务某个节点宕机了，访问不了了，这时A服务又怎么办？

上述的问题，通过Nacos就能够很好的处理。Nacos的服务注册与发现，正好解决了这些问题。

#### 如何让服务变成Nacos监管的服务？

一、在要使用Nacos的服务中引入Nacos的客户端。

```xml
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

二、在配置文件中，将Nacos的服务端信息配置进来。

```yaml
#配置服务的名称
spring:
  application:
    name: order-nacos-demo
#配置nacos服务端
  cloud:
    nacos:
      server-addr: 127.0.0.1:8848
      discovery:
        username: nacos
        password: nacos
        namespace: public
```

通过以上两步就完成了，服务由Nacos的注册和发现。

下边以order-service为例，调用store-service的方法时，就可以直接使用服务名称+接口名字的方式进行访问。

```java
@GetMapping("/order/add")
public String addOrder(){
  //使用nacos作为注册中心，来进行服务的发现
  String msg = restTemplate.getForObject("http://store-nacos-demo/store/subtract", String.class);
  return "下单成功！" + msg;
}
```

代码中的store-nacos-demo为store-service服务的名称，/store/subtrace为接口地址，可以看出，在调用过程中，并没有暴露出接口的ip、port等信息。

注：以前的版本需要再Application启动类的类名上添加@EnableDiscoveryClient注解。新版本无需添加，亦可使用。

```java
@EnableDiscoveryClient //此注释可有可无，均可使用。
public class OrderNacosDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderNacosDemoApplication.class, args);
    }
}
```

#### 集群部署时，如何调用服务API？

Nacos默认集成了Ribbon框架，Ribbon框架默认按照轮询的策略进行负载均衡。

当服务集群部署时，服务之间的调用需要采用负载均衡的方式来进行访问。具体操作如下：

```java
public class OrderNacosDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderNacosDemoApplication.class, args);
    }

    @Bean
    @LoadBalanced //使用此注解来表示负载均衡
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```

#### Nacos集群部署

nacos集群部署分几个步骤。

修改nacos conf文件夹里的 application.properties

修改server.port端口号、

修改数据库配置

```java
spring.datasource.platform=mysql

### Count of DB:
db.num=1

### Connect URL of DB:
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=root
db.password.0=lastroot
```

nacos集群部署时，需要使用数据库。默认使用的是mysql数据库。

修改启动sh的启动模式

```java
export MODE="cluster"
```

