# -------------------------------------------------------------
# 1단계: 빌드(Build) 스테이지
# -------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy AS build_stage

WORKDIR /app

# 1. [수정] 빌드에 필요한 모든 파일을 'backend/' 경로에서 복사합니다.
COPY backend/gradlew .
COPY backend/gradle ./gradle
COPY backend/build.gradle .
COPY backend/settings.gradle .

# 2. [수정] 소스 코드를 'backend/src'에서 복사합니다.
COPY backend/src ./src

# 3. 빌드 명령 실행
RUN ./gradlew build --no-daemon -x test

# -------------------------------------------------------------
# 2단계: 실행(Runtime) 스테이지
# -------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# 4. [수정] 빌드된 .jar 파일의 경로를 'backend/' 기준으로 수정합니다.
COPY --from=build_stage /app/build/libs/insk-backend-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]