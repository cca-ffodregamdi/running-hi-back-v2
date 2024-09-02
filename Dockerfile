# Docker file for Dev

# jdk 17 Image Start
FROM openjdk:17-jdk

# Set the working directory
WORKDIR /app

# Log 파일 저장 디렉토리 생성
RUN mkdir -p /app/logs

ARG PROFILE=dev
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

# 인자 정리 -jar
ARG JAR_FILE=build/libs/*.jar

# jar File Copy
COPY ${JAR_FILE} app.jar

# jar 파일 실행 및 로그 입출력 지정 명령
ENTRYPOINT ["sh", "-c", "java -jar -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} app.jar > /app/logs/app.log 2>&1"]