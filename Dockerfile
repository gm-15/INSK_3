# -------------------------------------------------------------
# 1단계: 빌드(Build) 스테이지
# JDK 21 이미지를 기반으로 Gradle을 사용해 프로젝트를 빌드합니다.
# -------------------------------------------------------------
FROM openjdk:21-jdk AS BUILD_STAGE

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 래퍼 파일 복사
COPY gradlew .
COPY gradle ./gradle

# build.gradle 파일 복사 (의존성 캐싱을 위해)
COPY build.gradle .
COPY settings.gradle .
# (만약 settings.gradle.kts를 사용한다면 settings.gradle.kts .)

# 먼저 의존성만 다운로드하여 Docker 빌드 캐시를 활용
RUN ./gradlew build --no-daemon -x test

# 전체 소스 코드 복사
COPY src ./src

# 프로젝트 빌드 (테스트는 스킵)
RUN ./gradlew build --no-daemon -x test

# -------------------------------------------------------------
# 2단계: 실행(Runtime) 스테이지
# 빌드 스테이지에서 생성된 .jar 파일만 가져와,
# 불필요한 JDK/Gradle 없이 JRE(Java 실행 환경)만으로 실행합니다.
# -------------------------------------------------------------
FROM openjdk:21-jre-slim

WORKDIR /app

# 빌드 스테이지(BUILD_STAGE)에서 생성된 .jar 파일을 복사
# build/libs/insk_backend-0.0.1-SNAPSHOT.jar 경로가 맞는지 확인 필요
COPY --from=BUILD_STAGE /app/build/libs/insk-backend-0.0.1-SNAPSHOT.jar app.jar
# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]