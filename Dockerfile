# 1. Base Image
FROM eclipse-temurin:17-jdk

# 2. Working Directory
WORKDIR /app

# 3. 타임존 설정 (한국 시간 기준)
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

# 4. 빌드된 Jar 파일을 컨테이너에 복사
COPY build/libs/*SNAPSHOT.jar /app.jar

# 5. 포트 노출
EXPOSE 8080

# 6. 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/app.jar"]