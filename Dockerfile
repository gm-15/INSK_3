# -------------------------------------------------------------
# 1단계: 빌드(Build) 스테이지
# -------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy AS build_stage

WORKDIR /app

# 1. 빌드에 필요한 모든 파일 복사
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# 2. [수정] 소스 코드를 *먼저* 복사합니다.
COPY src ./src

# 3. [수정] 빌드 명령은 소스 복사 *후*, *한 번만* 실행합니다.
RUN ./gradlew build --no-daemon -x test

# -------------------------------------------------------------
# 2단계: 실행(Runtime) 스테이지
# -------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# 4. 올바른 jar 파일 이름을 복사합니다.
COPY --from=build_stage /app/build/libs/insk-backend-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]