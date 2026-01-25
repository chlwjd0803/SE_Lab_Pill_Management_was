# 1. Eclipse Temurin JDK 17 이미지를 사용합니다.
# (사양이 충분하므로 기능이 풍부한 일반 이미지를 사용합니다.)
FROM eclipse-temurin:17-jdk

# 2. 빌드된 jar 파일의 위치를 변수로 지정
ARG JAR_FILE=build/libs/*.jar

# 3. jar 파일을 컨테이너 내부로 복사
COPY ${JAR_FILE} app.jar

# 4. 앱 실행 명령어
ENTRYPOINT ["java", "-jar", "/app.jar"]