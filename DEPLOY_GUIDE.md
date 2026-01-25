# 📋 [표준 운영 절차] Pill-App 서버 배포 및 유지보수 명세서

이 문서는 영남대학교 컴퓨터공학과 학부연구생 및 관련 프로젝트 후임자를 위해 작성되었습니다. 본 명세서의 절차를 무시할 경우 네트워크 충돌 및 데이터 유실이 발생할 수 있으므로 반드시 순서대로 수행하십시오.

---

## 1. 서버 인프라 구성도 (Infrastructure)

### [네트워크 포트 정의]
* **외부 망 (Public):**
    * API 서비스: `8080` (Spring Boot 접속용)
    * 데이터베이스: `3307` (외부 관리 도구/DataGrip 접속용)
* **내부 망 (Docker Network):**
    * 통신 경로: `spring-app` -> `pill_db:3306`
    * **주의:** 컨테이너끼리 통신할 때는 서버의 공인 IP나 `3307` 포트를 절대 사용하지 않습니다.



---

## 2. 서버 환경 구축 (최초 1회 필수)

서버 터미널(SSH)에 접속하여 아래 명령어를 실행하여 기반을 마련합니다.

### ① 도커 네트워크 생성 및 DB 연결
```bash
# 1. 전용 브리지 네트워크 생성
docker network create app-network

# 2. 기존 MySQL 컨테이너(pill_db)를 해당 네트워크에 강제 연결
docker network connect app-network pill_db
```

### ② 물리 저장소(HDD) 경로 확보 및 권한 설정
대용량 이미지 저장을 위해 HDD 마운트 경로를 사용하며, 도커 컨테이너가 파일을 쓰고 읽을 수 있도록 권한을 개방합니다.
```bash
# 1. 저장 디렉토리 생성
sudo mkdir -p /data/cj/pill_image_data/images
sudo mkdir -p /data/cj/pill_image_data/samples

# 2. 권한 설정 (도커 컨테이너의 root 계정 접근 허용)
sudo chmod -R 777 /data/cj/pill_image_data/
```

---

## 3. 애플리케이션 빌드 및 이미지 배포 (로컬 PC)

이 작업은 개발자의 로컬 PC(Mac/Windows)에서 수행합니다.

### ① 스프링 부트 빌드 (Executable JAR 생성)
```bash
# 프로젝트 루트 경로에서 실행
./gradlew clean build -x test
```

### ② 도커 이미지 빌드 (플랫폼 호환성 준수)
서버 CPU 아키텍처(AMD64)에 맞추기 위해 `--platform` 옵션을 반드시 사용해야 합니다.
```bash
# 1. 도커 허브 로그인
docker login

# 2. 리눅스 서버용 이미지 빌드
docker build --platform linux/amd64 -t chlwjd0803/pill-app:latest .

# 3. 도커 허브 업로드
docker push chlwjd0803/pill-app:latest
```

---

## 4. 서버 배포 설정 명세 (Server Side)

서버의 배포 디렉토리(`~/pill/app/`)에 아래 두 파일을 작성하여 배치합니다.

### ① docker-compose.yml
```yaml
version: '3.8'

services:
  spring-app:
    image: chlwjd0803/pill-app:latest
    container_name: pill-app
    restart: always
    ports:
      - "8080:8080"
    environment:
      # .env 파일에서 변수 치환
      DB_URL: ${DB_URL}
      DB_USERNAME: ${DB_USERNAME}
      DB_PASSWORD: ${DB_PASSWORD}
      UPLOAD_DIR: /app/uploads
      SAMPLE_DIR: /app/samples
    volumes:
      # [물리적 HDD 경로] : [컨테이너 내부 논리 경로]
      - /data/cj/pill_image_data/images:/app/uploads
      - /data/cj/pill_image_data/samples:/app/samples
    networks:
      - app-network

networks:
  app-network:
    external: true
```

### ② .env (환경변수 관리)
**주의:** DB_URL의 호스트는 반드시 `pill_db`이고 포트는 `3306`이어야 합니다.
```text
DB_URL=jdbc:mysql://pill_db:3306/[데이터베이스명]?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul
DB_USERNAME=root
DB_PASSWORD=[실제_비밀번호]
```

---

## 5. 운영 및 장애 대응 명령어 (업데이트)

기존 `docker-compose` 명령어가 아닌 최신 **`docker compose` (공백 사용)** 명령어를 권장합니다.

### [신규 버전 배포 및 설정 변경 루틴]
코드 수정이나 환경변수(`.env`) 변경 시 다음 명령어를 수행합니다.
```bash
# 1. (선택) 최신 이미지 수신
docker compose pull spring-app

# 2. 기존 컨테이너 강제 제거 (설정 꼬임 및 프로세스 충돌 방지)
docker rm -f pill-app

# 3. 컨테이너 재기동
docker compose up -d spring-app
```

### [장애 진단 모니터링]
```bash
# 1. 실시간 로그 확인
docker compose logs -f pill-app

# 2. 네트워크 연결 상태 확인
docker network inspect app-network

# 3. 컨테이너 내부 쉘 접속 (필요 시)
docker exec -it pill-app /bin/bash
```

---

## 6. 유지보수 핵심 주의사항 (Checklist)

1.  **DB 포트 주의:** 외부(DataGrip) 접속은 `3307`, 서버 내부(Spring) 접속은 `3306`입니다. 혼동 시 연결이 거부됩니다.
2.  **localhost 사용 금지:** 컨테이너 환경에서 `localhost`는 자기 자신을 의미합니다. DB 주소는 반드시 컨테이너 이름인 `pill_db`를 사용하십시오.
3.  **권한 유실:** 서버 재부팅 등으로 HDD 마운트 권한이 변경될 경우 `chmod` 명령어를 다시 수행해야 파일 업로드가 정상 작동합니다.
4.  **이미지 아키텍처:** Mac M1/M2에서 빌드 시 반드시 `--platform linux/amd64`를 붙여야 서버에서 `Exec format error`가 발생하지 않습니다.
5.  **명령어 호환성:** `KeyError: 'ContainerConfig'` 발생 시 구형 하이픈 명령어(`docker-compose`) 대신 최신 명령어(`docker compose`)를 사용하십시오.