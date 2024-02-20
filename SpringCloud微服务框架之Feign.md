# SpringCloud微服务框架之Feign

#### 为什么需要feign？

先来看一个例子，不使用feign调用服务的接口，是这样的

```java
@AllArgsConstructor
@RestController
public class OrderController {

    private final RestTemplate restTemplate;

    @GetMapping("/order/add")
    public String addOrder(){
        //使用nacos作为注册中心，来进行服务的发现
        String msg = restTemplate.getForObject("http://store-nacos-demo/store/subtract", String.class);
        return "下单成功！" + msg;
    }

}
```

这段代码是一个完整的，服务与服务之间调用的过程。order-service服务通过http调用store-nacos-demo服务的接口。这段代码看起来已经很不错了。至少在调用的过程中，无需知晓被调用服务的ip、端口等信息，直接通过服务名称+接口名称的方式进行访问。但是仔细想想，我们能不能像调用本地方法一样，来调用其他服务的接口。feign就是为了解决这个问题而诞生的。feign是netfilx的一个框架，目前已经停止更新，但是openfeign完美的继承和扩展了feign。

#### 如何使用openfeign？

使用openfeign一共需要4个步骤。首先要明确一个事情，openfeign在哪个地方使用。注意，是在服务调用方使用。而不是服务提供方使用。但是再思考一个问题。也有可能服务调用方，也是另外一个服务的提供方。但不管怎么说，openfeign是给服务调用方使用的。

好了，明确了这个事情。我们接下来实现。

第一步，在服务中引入openfeign

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

这里需要注意的是openfeign是springcloud的框架，注意是springcloud的框架，不是springcloudalibaba的，也不是springboot的，所以需要看清楚父pom中是否引入了springcloud。

```xml
<!-- spring-cloud 版本管理器 -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-dependencies</artifactId>
  <version>${spring.cloud.version}</version>
  <type>pom</type>
  <scope>import</scope>
</dependency>
```

springcloudalibaba是一码事儿，springcloud是另一码事儿。两者是结合的关系。但不是包含的关系。

第二步，在启动类上加上@EnableFeignClients注解，否则会报错。

```java
@SpringBootApplication
@EnableFeignClients //此注解不能省，否则业务引入类的时候编译报错
public class OrderFeignDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeignDemoApplication.class, args);
    }
}
```

第三步，编写实际业务。

新创建feign目录，在该目录下填写实际业务，比如order-feign服务调用store-nacos-demo服务的接口。

```java
@FeignClient(name = "store-nacos-demo", path = "/store") //name对应的是服务提供方的名称，path对应的是服务提供方的整体mapping名称。
public interface StoreFeignService {

    @GetMapping("/subtract")  //此处对应的是服务提供方，接口的mapping.
    String subtractStore();  //此处对应的是服务提供方的方法名。

}
```

需要在feign目录下创建相关的业务接口，并以@FeignClient注解来修饰。

将被调用方的服务接口代码贴出来，对比一下。

```java
@RestController
@RequestMapping("/store")
public class StoreController {

    @Value("${server.port}")
    private int port;

    @GetMapping("/subtract")
    public String subtractStore(){
        return "端口号"+ port +"，服务扣减库存";
    }

}
```



第四步，在实际的业务类中调用方法。

```java
@AllArgsConstructor
@RestController
public class OrderController {

    private final StoreFeignService storeFeignService;
    @GetMapping("/order/add")
    public String addOrder(){
        //使用nacos作为注册中心，来进行服务的发现
//        String msg = restTemplate.getForObject("http://store-nacos-demo/store/subtract", String.class);
        String msg = storeFeignService.subtractStore();
        return "下单成功！" + msg;
    }

}
```

在实际的业务中，注入feign目录下创建好的，实际业务类，并通过业务类调用方法的方式，来进行访问。就像调用本地方法一样。

至此，服务中使用openfeign就完成了。

#### openfeign的负载均衡

或许会有人提成问题，使用了openfeign后，也不需要使用@LoadBalanced了，那么是如何实现负载的呢？

openfeign中默认集成了ribbon.

<img src="/Users/liyachao/Library/Application Support/typora-user-images/image-20231019110033238.png" alt="image-20231019110033238" style="zoom:50%;" />

提到了ribbon，都知道了。默认的就是轮询的负载策略。

#### openFeign还能做什么？

##### 日志配置：要实现日志配置，有两种方式，全局配置和局部配置。

***第一种，全局配置***。

创建FeignConfig类。

```java
/**
 * 定义全局的日志
 */
@Configuration //使用此注解，是定义全局的日志
public class FeignConfig {

    /**
     * feign的日志配置
     * @return 日志级别
     */
    @Bean
    public Logger.Level feignLoggingLevel(){
        return Logger.Level.FULL;
    }
}
```

由于springboot的默认日志级别是info，所以在配置文件中，需要指明需要输出的日志级别。

```yaml
logging:
  level:
    com.spring.cloud.demo.order.feign: debug
```

到此完成了openfeign的日志配置。再调用接口时，就可以输出日志。

```java
---> GET http://store-nacos-demo/store/subtract HTTP/1.1
---> END HTTP (0-byte body)
<--- HTTP/1.1 200 (36ms)
connection: keep-alive
content-length: 34
content-type: text/plain;charset=UTF-8
date: Thu, 19 Oct 2023 03:36:12 GMT
keep-alive: timeout=60
端口号8024，服务扣减库存
<--- END HTTP (34-byte body)

```

可以设置输出日志的级别，级别有FULL,BASIC,HEADERS,NONE。FULL表示全部信息，BASIC表示基本信息，HEADERS表示头部信息，NONE表示什么都不输出。

***第二种，局部配置***。

局部配置需要将配置类的@Configuration注解去掉。

在需要配置的feign客户端中，设置 configuration，然后在配置文件中配置需要输出日志的service。

```java
//@Configuration
public class FeignConfig {

    /**
     * feign的日志配置
     * @return 日志级别
     */
    @Bean
    public Logger.Level feignLoggingLevel(){
        return Logger.Level.FULL;
    }
}
```

```java
@FeignClient(name = "member-nacos-demo", path = "/member", configuration = FeignConfig.class) //局部设置
public interface MemberFeignService {

    @GetMapping("/subtract/{id}")
    String subtractMember(@PathVariable("id") String id);
}
```

```yaml
feign:
  client:
    config:
      member-nacos-demo:
        loggerLevel: HEADERS
```

##### 契约配置

什么是契约配置，feign1.x版本用的是@RequestLine注解修饰的。在2.x版本后进行了修改。契约配置就是在程序中增加一个配置，来兼容之前的版本。说白了，就是将feign的mvc注解还原为feign的原生注解。

修改配置文件。

```yaml
feign:
  client:
    config:
      member-nacos-demo:
        contract: feign.Contract.Default
```

修改代码注解为原生注解。

```java
@FeignClient(name = "member-nacos-demo", path = "/member", configuration = FeignConfig.class)
public interface MemberFeignService {
    @RequestLine("GET /subtract/{id}")
    String subtractMember(@Param("id") String id);
}
```

##### 超时时间配置

```yaml
feign:
  client:
    config:
      member-nacos-demo:
        contract: feign.Contract.Default
        connecTimeout: 5000
        readTimeout: 4000
```

连接超时、读取超时

##### openFeign自定义拦截器

实现方式，自定义一个拦截器，实现RequestInterceptor，实现appy方法。

```java
public class FeignInterceptor implements RequestInterceptor {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.uri("/subtract/1");
        logger.info("自定义拦截器");
    }
}
```

第二步，配置文件

```yaml
feign:
  client:
    config:
      member-nacos-demo:
        requestInterceptors[0]:
          com.spring.cloud.demo.order.interceptor.FeignInterceptor
```

