# 需要先在宿主机执行gradle clean zip命令
# 基于哪个镜像
FROM openjdk:8
MAINTAINER 1520173329@mail.jxust.edu.cn

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY build/distributions/*.zip ./build/distributions/
RUN ls -al build/distributions/
# unzip
RUN unzip build/distributions/*.zip
# remove zip package
RUN rm -f build/distributions/*.zip
# rename dir
RUN mv daily-care/ app
# define workdir0
WORKDIR /app

RUN ls -al
# rename jar
RUN mv *.jar app.jar
# 声明需要暴露的端口
EXPOSE 8899
# 配置容器启动后执行的命令
ENTRYPOINT ["sh","-c", "java ${JVM_OPTIONS} -jar app.jar"]