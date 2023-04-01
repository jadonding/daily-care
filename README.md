# springboot-demo
Spring Boot + Gradle + Github Action + Docker + Ghcr

## 1. 工程介绍
- Spring Boot 工程脚手架，快速构建新项目
- 使用 Gradle 构建工具
- Docker 镜像构建
- 含有 Github Action 自动构建 Docker 镜像并推送到 Github Container Registry
- 提供默认log4j2配置，日志输出到文件
- 提供zip打包脚本，打包成zip文件
- 提供服务启动、调试、停止、重启脚本（Linux环境下）

## 2. 使用方式

### 2.1. 打zip包
```shell
./gradlew clean zip
```

打出的zip包在build/distributions目录下

### 2.2. 运行

将上一步打出的zip包传到Linux服务器，解压，进入解压目录，执行
运行：
```shell
./service.sh start
```

调试（指远程debug）：
```shell
./service.sh debug
```

停止：
```shell
./service.sh stop
```

重启：
```shell
./service.sh restart
```

### 2.3. 更新项目

如果不涉及到依赖包的更新，只是修改了代码，可以在本地执行
```shell
./gradlew clean bootJar
```
打出的jar包在build/libs目录下，
将打出的jar包传到Linux服务器，**替换**掉原来的jar包即可

注意：需要保证工程目录下只有一个jar包，否则无法启动

如果涉及到依赖包的更新，需要重新打zip包，重复上面的打包、运行章节

### 2.4. Docker镜像构建

本项目的Dockerfile在根目录下，使用之前需要先执行打包操作
```shell
./gradlew clean zip
```

构建docker镜像
```shell
docker build -t springboot-demo .
```

### 2.5. 使用Github Action构建的Docker镜像

本项目的Github Action配置文件在[.github/workflows](.github/workflows)目录下，每次push到master分支时会自动构建Docker镜像并推送到Github Container Registry


以本项目为例，拉取镜像
```shell
docker pull ghcr.io/jadonding/springboot-demo:latest
```

使用镜像
```shell
docker run -d  \
--name springboot-demo  \
-p 8899:8899 \
-v /opt/package/springboot-demo/config:/app/config \
-v /opt/package/springboot-demo/logs:/app/logs \
--restart=always \
ghcr.io/jadonding/springboot-demo:latest
```

参数说明：
- -d 后台运行
- --name 容器名称
- -p 端口映射
- -v 挂载目录
- --restart=always 自动重启
- ghcr.io/jadonding/springboot-demo:latest 镜像名称
- /app/config 容器内的配置文件目录
- /app/logs 容器内的日志目录
- /opt/package/springboot-demo/config 宿主机的配置文件目录
- /opt/package/springboot-demo/logs 宿主机的日志目录

建议将配置文件和日志文件挂载到宿主机，这样可以方便查看日志和修改配置文件