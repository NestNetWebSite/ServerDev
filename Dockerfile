# BUILD (이미지를 만드는 과정)

# Base Image
FROM openjdk:17-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 파일 복사
COPY . .

# Gradle 빌드 실행 (테스트 제외)
RUN ./gradlew build -x test


# Base Image
FROM openjdk:17-alpine

# 운영 환경 컨테이너
WORKDIR /app

# 프로젝트를 빌드한 파일을 변수에 저장
ARG JAR_FILE=./build/libs/NestNet-WebSite-0.0.1-SNAPSHOT.jar

# 컨테이너 WORKDIR 위치에 jar 파일 복사
COPY ${JAR_FILE} nestnet_server.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "nestnet_server.jar"]