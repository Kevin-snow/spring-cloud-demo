## Nacos-Server的安装与启动

#### **Nacos的下载**

1. 登录到github，在gitHub的页面，输入nacos搜索。

2. 搜索到nacos

3. 选择第一个alibaba/nacos进入

4. 点击Releases进入，输入对应的版本号

5. 找到对应的版本，点击下载

#### **Nacos的安装**

1. 下载后，解压文件。1

2. 进入bin目录，修改startup.sh文件，此文件默认的MODE类型是cluster集群模式，如果是本地开发，修改为standalone模式。

3. 启动startup.sh文件，会出现一个log日志文件夹，并且有日志文件。如果启动成功会显示log，及访问地址。如果出现

   nohup: /Library/Internet: No such file or directory的日志信息，表示未启动成功。

   原因是未找到jdk的位置。需要检查Java的环境变量配置。

   使用命令 echo $JAVA_HOME 查看环境便令，如果为空，表示没有配置。

   使用open - e .bash_profile打开配置文件，进行配置。

   找到jdk的默认安装路径。/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk.jdk/Contents/Home

   ```java
   export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk.jdk/Contents/Home
   export PATH=$JAVA_HOME/bin:$PATH:.
   export CLASSPATH=$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar:.
   ```

4. 编辑完成后，使用 source .bash_profile 命令，使配置生效。

5. 此时，在通过查看命令 $JAVA_HOME进行查看，会出现Jdk的配置地址。

6. 再次启动nacos，会显示成功信息。

   

   