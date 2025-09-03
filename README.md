# 인프라 구성

## 개발 환경

### 단일 EC2를 개발 환경으로 사용

```text
# 인스턴스 사양
- t4g.small
- OS: Ubuntu 24.04
- Architecture: ARM 64
- 스토리지: 8GB (SSD)
- 탄력적 ip 할당
```

추후 수평 확장이 필요할 경우, 로드 밸런서(ex. aws alb)를 추가하고 여러 대의 EC2 인스턴스를 운영하는 방안을 고려.
t4g.small 사양은 단일 Spring 애플리케이션을 운영하는 데 충분하다 판단.

### 개발 환경 HTTPS 적용

- 도메인: meringue-dev.routie.me.
    - A 레코드로 개발 EC2의 탄력적 IP 연결.
- 인증서: Let's Encrypt를 통해 발급받은 무료 SSL 인증서 사용.


- 애플리케이션에 직접 TLS 핸드셰이크를 처리하는 대신, Nginx를 리버스 프록시로 설정하여 HTTPS 트래픽을 처리.
    - 애플리케이션의 책임을 줄이고, 확장성과 유지보수성이 향상됨.
- Https 관련 Nginx 설정 예시
    - http로 들어온 요청을 https로 리다이렉트(308 Permanent Redirect)하는 80 포트 Server 존재.
    - 443 포트 Server에서 SSL 인증서 경로 설정 및 리버스 프록시 기능 수행.

### 개발 환경 메트릭 수집

- CloudWatch Agent를 EC2 인스턴스에 설치하여 CPU 사용률, 메모리 사용량, 디스크 I/O 등 기본적인 시스템 메트릭 수집.
    - 현재 Memory Utilization 과 Cpu Usage Idle 메트릭을 DASHBOARD로 시각화하여 모니터링 중.
- 애플리케이션 Health Check를 위한 커스텀 메트릭 설정.
    - Spring Actuator의 `/actuator/health` 엔드포인트를 주기적(현재 디버깅을 위해 1분)으로 호출하여 애플리케이션 상태를 확인.
    - 그 결과를 CloudWatch의 로그 그룹으로 전송.
        - health_check.sh 스크립트를 작성해 cron으로 주기적 실행.
    - 로그 그룹의 로그 스트림의 지표 필터 기능을 통해 로그를 커스텀 메트릭으로 변환, DASHBOARD로 시각화하여 모니터링 중.
    - 추후 actuator 의 다양한 엔드포인트를 통한 커스텀 메트릭 수집을 고려 중.
        - 이를 대비하기 위해 `/actuator` 엔드포인트에 대한 접근은 localhost(EC2)로 제한.
        - 이 설정은 Nginx 설정 파일에서 `location /actuator` 블록을 통해 구현.

### 개발 환경 CI/CD 파이프라인 구축

- CI
    - GitHub Actions를 사용하여 코드 푸시 시 자동으로 빌드 및 배포 파이프라인 실행.
        - `develop` 브랜치에 푸시될 때마다 파이프라인이 트리거되도록 설정.
        - gradle test 를 통해 테스트 자동화.
- CD
    - self-hosted runner를 EC2 인스턴스에 설치하여 GitHub 에서 Job Polling 을 통해 워크플로우 실행.
    - 빌드된 JAR 파일을 EC2 인스턴스로 복사하고, Actions Secret에 저장된 dev 용 application.yml 파일로 애플리케이션 설정.
    - 기존에 실행 중인 애플리케이션이 있다면 종료하고, 새로운 JAR 파일로 애플리케이션 재시작.
    - profile 을 dev로 설정하여 개발 환경에 맞는 설정 적용.

### 개발 환경 API 문서

- 5시간이라는 제약으로 인해 API 문서를 후순위로 설정.
    - 기본적인 Swagger 설정을 통해 request, response 형식만 제공.

## 배포 환경

### 단일 EC2를 배포 환경으로 사용

```text
# 인스턴스 사양
- t4g.small
- OS: Ubuntu 24.04
- Architecture: ARM 64
- 스토리지: 8GB (SSD)
- 탄력적 ip 할당
```

추후 수평 확장이 필요할 경우, 로드 밸런서(ex. aws alb)를 추가하고 여러 대의 EC2 인스턴스를 운영하는 방안을 고려.
t4g.small 사양은 단일 Spring 애플리케이션을 운영하는 데 충분하다 판단.

### 배포 환경 HTTPS 적용

- 도메인: meringue-prod.routie.me.
    - A 레코드로 배포 EC2의 탄력적 IP 연결.
- 인증서: Let's Encrypt를 통해 발급받은 무료 SSL 인증서 사용.


- 애플리케이션에 직접 TLS 핸드셰이크를 처리하는 대신, Nginx를 리버스 프록시로 설정하여 HTTPS 트래픽을 처리.
    - 애플리케이션의 책임을 줄이고, 확장성과 유지보수성이 향상됨.
- Https 관련 Nginx 설정 예시
    - http로 들어온 요청을 https로 리다이렉트(308 Permanent Redirect)하는 80 포트 Server 존재.
    - 443 포트 Server에서 SSL 인증서 경로 설정 및 리버스 프록시 기능 수행.

### 배포 환경 메트릭 수집

- CloudWatch Agent를 EC2 인스턴스에 설치하여 CPU 사용률, 메모리 사용량, 디스크 I/O 등 기본적인 시스템 메트릭 수집.
    - 현재 Memory Utilization 과 Cpu Usage Idle 메트릭을 DASHBOARD로 시각화하여 모니터링 중.
- 애플리케이션 Health Check를 위한 커스텀 메트릭 설정.
    - Spring Actuator의 `/actuator/health` 엔드포인트를 주기적(현재 디버깅을 위해 1분)으로 호출하여 애플리케이션 상태를 확인.
    - 그 결과를 CloudWatch의 로그 그룹으로 전송.
        - health_check.sh 스크립트를 작성해 cron으로 주기적 실행.
    - 로그 그룹의 로그 스트림의 지표 필터 기능을 통해 로그를 커스텀 메트릭으로 변환, DASHBOARD로 시각화하여 모니터링 중.
    - 추후 actuator 의 다양한 엔드포인트를 통한 커스텀 메트릭 수집을 고려 중.
        - 이를 대비하기 위해 `/actuator` 엔드포인트에 대한 접근은 localhost(EC2)로 제한.
        - 이 설정은 Nginx 설정 파일에서 `location /actuator` 블록을 통해 구현.

### 배포 환경 CI/CD 파이프라인 구축

- CI
    - GitHub Actions를 사용하여 코드 푸시 시 자동으로 빌드 및 배포 파이프라인 실행.
        - `main` 브랜치에 푸시될 때마다 파이프라인이 트리거되도록 설정.
        - gradle test 를 통해 테스트 자동화.
- CD
    - self-hosted runner를 EC2 인스턴스에 설치하여 GitHub 에서 Job Polling 을 통해 워크플로우 실행.
    - 빌드된 JAR 파일을 EC2 인스턴스로 복사하고, Actions Secret에 저장된 main 용 application.yml 파일로 애플리케이션 설정.
    - 기존에 실행 중인 애플리케이션이 있다면 종료하고, 새로운 JAR 파일로 애플리케이션 재시작.
    - profile 을 main 설정하여 배포 환경에 맞는 설정 적용.
    - 추후, Blue-Green Deployment 전략을 통한 무중단 배포 고려.

### 배포 환경 API 문서

- 5시간이라는 제약으로 인해 API 문서를 후순위로 설정.
    - 기본적인 Swagger 설정을 통해 request, response 형식만 제공.

## 데이터베이스

### 단일 EC2를 데이터베이스 환경으로 사용

```text
# 인스턴스 사양
- t4g.small
- OS: Ubuntu 24.04
- Architecture: ARM 64
- 스토리지: 8GB (SSD)
- 탄력적 ip 할당
```

추후 안정적인 운영을 위해 AWS RDS로 이전하는 방안을 고려.
가장 간편하고 저렴한 방법으로 데이터베이스를 운영하기 위해 단일 EC2 인스턴스에 Docker를 이용해 MySQL 컨테이너를 실행.

### 데이터베이스 설정

- 빠른 설정을 위해 Docker MySQL Container 는 `443 -> 3306` 포트 포워딩 설정.
    - 추후 보안을 위해 VPC 설정 및 보안 그룹 설정을 통해 외부 접근 차단 고려.
- MySQL 설정은 기본 설정을 사용하되, root 계정의 비밀번호는 강력한 비밀번호로 설정.
    - 빠른 설정을 위해 애플리케이션은 root 계정을 사용하도록 구현.
        - 추후 운영 환경에서는 별도의 사용자 계정을 생성하여 최소 권한 원칙을 적용하는 방안을 고려.
- 데이터베이스 백업을 위해 주기적으로 MySQL 덤프를 생성하고, 이를 EC2 인스턴스의 스토리지에 저장하는 스크립트를 작성
    - RDS 이전 시 자동으로 구성할 수 있음.

# 5시간의 제약으로 인해 구축하지 못한 부분

## 로드 밸런서와 수평 확장

- 단일 EC2 인스턴스는 가용성이 매우 떨어지는 구성.
- 추후 AWS Application Load Balancer(ALB)를 도입하여 여러 대의 EC2 인스턴스를 운영하는 방안을 고려.
- 이를 통해 트래픽 분산, 장애 조치, 무중단 배포 등이 가능.
- ALB가 아니어도, 로드 밸런싱을 위한 서버(ex. Nginx 서버) 구성을 직접 구축하는 방안도 고려 가능.

## 데이터베이스 사용자 분리

- 모든 권한을 가진 root 계정을 애플리케이션이 사용하도록 구현되어 있음.
- 운영 환경에서는 최소 권한 원칙을 적용하여, 애플리케이션 전용 사용자 계정을 생성하고 필요한 권한만 부여하는 방안을 고려.
- 이를 통해 보안성을 향상시키고, 잠재적인 피해를 최소화할 수 있음.

## 데이터 손실 없는 데이터베이스 마이그레이션

- jpa 의 ddl auto update 설정을 통해 애플리케이션 시작 시점에 스키마를 자동으로 업데이트하도록 구현되어 있음.
- 운영 환경에서는 데이터 손실 없는 마이그레이션을 위해 Flyway와 같은 자동 스키마 반영 도구를 도입하는 방안을 고려.
- 이를 통해 스키마 변경 이력을 관리하고, 롤백 기능을 제공하여 안정적인 운영이 가능.
- DDL 을 자동화하는 것에 대한 문제도 고민해야 함. 현업 규모의 큰 서비스에서는 애플리케이션의 중단이 매우 큰 손실을 야기하므로 자동 DDL 을 사용하지 않을 것으로 판단.

## 모니터링 및 알림 시스템

- 현재는 CloudWatch 대시보드를 통해 기본적인 메트릭을 시각화하고 있음.
- 추후, CloudWatch Alarms를 설정하여 특정 임계값 초과 시 이메일 또는 SNS 알림을 받는 방안을 고려.

## 보안 강화

- 현재 80, 8080, 22, 25, 443 포트가 개방되어 있음.
- 또한, 외부에서 접근도 가능한 상태.
- 추후, 애플리케이션과 DB 서버를 private subnet에 배치하고 외부 접근을 제한하는 방안을 고려.

```text
Public Subnet: ALB / Nginx
    │
    ▼
Private Subnet: Spring Boot 애플리케이션 서버
    │
    ▼
Private Subnet: DB 서버
```
