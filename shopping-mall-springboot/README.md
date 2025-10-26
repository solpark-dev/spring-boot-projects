# 🛍️ Shopping Mall Spring Boot Application

Spring MVC 프로젝트를 Spring Boot로 마이그레이션한 전자상거래(쇼핑몰) 애플리케이션입니다.

---

## 📋 목차
1. [프로젝트 소개](#-프로젝트-소개)
2. [기술 스택](#-기술-스택)
3. [주요 기능](#-주요-기능)
4. [실행 환경](#-실행-환경)
5. [설치 및 실행 방법](#-설치-및-실행-방법)
6. [프로젝트 구조](#-프로젝트-구조)
7. [API 엔드포인트](#-api-엔드포인트)
8. [문제 해결](#-문제-해결)

---

## 🎯 프로젝트 소개

**Shopping Mall Application**은 다음과 같은 전자상거래 핵심 기능을 제공합니다:
- 회원가입/로그인 (일반 + 소셜 로그인)
- 상품 관리 (등록, 조회, 수정, 재고 관리)
- 장바구니
- 주문/결제
- 마이페이지
- 관리자 기능

---

## 🛠️ 기술 스택

### Backend
- **Spring Boot 2.7.18**
- **MyBatis 2.3.2** (ORM)
- **Oracle Database** (JDBC)
- **Maven** (빌드 도구)

### Frontend
- **JSP** (JavaServer Pages)
- **JSTL** (JSP Standard Tag Library)
- **JavaScript**

### 기타
- **AOP** (Aspect-Oriented Programming) - 로깅
- **소셜 로그인**: Kakao, Naver, Google

---

## ✨ 주요 기능

### 1. 회원 관리
- ✅ 일반 회원가입/로그인
- ✅ 소셜 로그인 (Kakao, Naver, Google)
- ✅ 회원 정보 수정
- ✅ 회원 목록 조회 (관리자)
- ✅ 아이디 중복 체크
- ✅ 회원 상태 관리 (ACTIVE, WITHDRAWN, DORMANT)

### 2. 상품 관리
- ✅ 상품 등록/조회/수정
- ✅ 카테고리별 상품 조회
- ✅ 상품 검색 (이름, 가격대)
- ✅ 다중 이미지 업로드
- ✅ 재고 관리

### 3. 장바구니
- ✅ 장바구니 담기
- ✅ 수량 변경
- ✅ 장바구니 삭제

### 4. 주문/결제
- ✅ 주문 생성
- ✅ 주문 상태 관리
- ✅ 주문 내역 조회
- ✅ 결제 정보 관리

### 5. 배송지 관리
- ✅ 다중 배송지 등록
- ✅ 기본 배송지 설정

---

## 💻 실행 환경

프로젝트를 실행하기 위해 다음이 필요합니다:

### 필수 요구사항
- **JDK 11** 이상
- **Maven 3.6** 이상
- **Oracle Database 11g** 이상 (또는 XE 버전)
- **IDE** (Eclipse, IntelliJ IDEA, VS Code 등)

### 권장 사양
- **RAM**: 최소 4GB (8GB 권장)
- **디스크**: 최소 500MB 여유 공간

---

## 🚀 설치 및 실행 방법

### 1️⃣ 프로젝트 다운로드

```bash
# ZIP 파일 다운로드 후 압축 해제
# 또는 Git Clone (저장소가 있는 경우)
git clone [repository-url]
cd shopping-mall-springboot
```

### 2️⃣ 데이터베이스 설정

#### Oracle 데이터베이스 접속
```sql
-- SQL*Plus 또는 SQL Developer로 접속
sqlplus scott/tiger@localhost:1521/xe
```

#### 초기 스키마 및 데이터 생성
```sql
-- 프로젝트 루트의 initialize3.sql 실행
@initialize3.sql
```

**또는** SQL Developer에서:
1. `initialize3.sql` 파일 열기
2. 전체 선택 (Ctrl+A)
3. 실행 (F5 또는 실행 버튼)

#### 데이터베이스 연결 정보 수정 (필요시)

`src/main/resources/application.properties` 파일에서 본인의 DB 정보에 맞게 수정:

```properties
# 데이터베이스 연결 정보
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=scott
spring.datasource.password=tiger
```

### 3️⃣ 프로젝트 빌드

#### Maven 빌드
```bash
# 프로젝트 루트 디렉토리에서 실행
mvn clean install
```

빌드가 성공하면 `target/shopping-mall.war` 파일이 생성됩니다.

### 4️⃣ 애플리케이션 실행

#### 방법 1: Maven으로 실행 (권장)
```bash
mvn spring-boot:run
```

#### 방법 2: IDE에서 실행
1. **Eclipse/IntelliJ**에서 프로젝트 Import
   - Eclipse: `File` → `Import` → `Existing Maven Projects`
   - IntelliJ: `File` → `Open` → 프로젝트 폴더 선택

2. **ShoppingMallApplication.java** 파일 찾기
   - 위치: `src/main/java/com/model2/mvc/ShoppingMallApplication.java`

3. **Run As → Java Application** (또는 `Shift+F10` in IntelliJ)

#### 방법 3: JAR 파일로 실행
```bash
java -jar target/shopping-mall.war
```

### 5️⃣ 애플리케이션 접속

브라우저에서 다음 URL로 접속:
```
http://localhost:8080
```

#### 테스트 계정

**일반 사용자:**
- ID: `user01`
- Password: `1111`

**관리자:**
- ID: `admin`
- Password: `1234`

---

## 📁 프로젝트 구조

```
shopping-mall-springboot/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/model2/mvc/
│   │   │       ├── ShoppingMallApplication.java    # 메인 클래스
│   │   │       ├── common/                         # 공통 유틸리티
│   │   │       │   ├── Page.java                   # 페이징
│   │   │       │   ├── Search.java                 # 검색 조건
│   │   │       │   ├── aspect/                     # AOP 로깅
│   │   │       │   └── interceptor/                # 인터셉터
│   │   │       ├── config/                         # 설정 클래스
│   │   │       │   ├── WebMvcConfig.java          # Spring MVC 설정
│   │   │       │   ├── MyBatisConfig.java         # MyBatis 설정
│   │   │       │   └── AppConfig.java             # 공통 설정
│   │   │       ├── service/                        # 비즈니스 로직
│   │   │       │   ├── domain/                     # 도메인 객체
│   │   │       │   ├── user/                       # 회원 관리
│   │   │       │   ├── product/                    # 상품 관리
│   │   │       │   ├── cart/                       # 장바구니
│   │   │       │   ├── purchase/                   # 주문
│   │   │       │   ├── category/                   # 카테고리
│   │   │       │   ├── address/                    # 배송지
│   │   │       │   ├── kakao/                      # 카카오 로그인
│   │   │       │   ├── naver/                      # 네이버 로그인
│   │   │       │   └── google/                     # 구글 로그인
│   │   │       └── web/                            # 컨트롤러
│   │   │           ├── user/                       # 회원 컨트롤러
│   │   │           ├── product/                    # 상품 컨트롤러
│   │   │           ├── purchase/                   # 주문 컨트롤러
│   │   │           └── mypage/                     # 마이페이지 컨트롤러
│   │   ├── resources/
│   │   │   ├── application.properties              # 설정 파일
│   │   │   ├── mybatis/
│   │   │   │   ├── mybatis-config.xml             # MyBatis 설정
│   │   │   │   └── mapper/                        # SQL Mapper XML
│   │   │   └── static/                            # 정적 리소스
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── views/                          # JSP 파일
│   └── test/                                       # 테스트 코드
├── pom.xml                                         # Maven 설정
├── initialize3.sql                                 # DB 초기화 스크립트
└── README.md                                       # 이 파일
```

---

## 🌐 API 엔드포인트

### 회원 관리
- `GET /user/login` - 로그인 페이지
- `POST /user/json/login` - 로그인 처리
- `GET /user/logout` - 로그아웃
- `GET /user/addUser` - 회원가입 페이지
- `POST /user/json/addUser` - 회원가입 처리
- `GET /user/listUser` - 회원 목록 (관리자)
- `POST /user/updateUser` - 회원 정보 수정

### 상품 관리
- `GET /product/listProduct` - 상품 목록
- `GET /product/getProduct?prodNo={prodNo}` - 상품 상세
- `GET /product/addProduct` - 상품 등록 페이지 (관리자)
- `POST /product/addProduct` - 상품 등록 처리 (관리자)
- `POST /product/updateProduct` - 상품 수정 (관리자)

### 장바구니
- `GET /cart/getCartList` - 장바구니 목록
- `POST /cart/json/addCart` - 장바구니 담기
- `PUT /cart/json/updateCartQuantity` - 수량 변경
- `DELETE /cart/json/deleteCart` - 장바구니 삭제

### 주문
- `GET /purchase/addPurchase?prodNo={prodNo}` - 주문 페이지
- `POST /purchase/addPurchase` - 주문 생성
- `GET /purchase/listPurchase` - 주문 내역

### 마이페이지
- `GET /mypage/home` - 마이페이지 메인
- `GET /mypage/orderHistory` - 주문 내역
- `GET /mypage/addressList` - 배송지 관리

---

## 🐛 문제 해결

### 1. 데이터베이스 연결 실패

**증상:** `Cannot get JDBC Connection` 오류

**해결방법:**
1. Oracle 데이터베이스가 실행 중인지 확인
   ```bash
   # Windows
   services.msc → OracleServiceXE 확인
   
   # Linux
   systemctl status oracle-xe
   ```

2. `application.properties`의 DB 접속 정보 확인
   ```properties
   spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
   spring.datasource.username=scott
   spring.datasource.password=tiger
   ```

3. 방화벽에서 1521 포트 허용 확인

### 2. Port 8080이 이미 사용 중

**증상:** `Port 8080 was already in use`

**해결방법:**
1. 다른 포트 사용
   ```properties
   # application.properties에 추가
   server.port=8081
   ```

2. 또는 8080 포트 사용 중인 프로세스 종료
   ```bash
   # Windows
   netstat -ano | findstr :8080
   taskkill /PID [PID번호] /F
   
   # Linux/Mac
   lsof -i :8080
   kill -9 [PID번호]
   ```

### 3. Maven 빌드 실패

**증상:** `BUILD FAILURE`

**해결방법:**
1. Maven 저장소 정리
   ```bash
   mvn clean
   ```

2. 의존성 강제 업데이트
   ```bash
   mvn clean install -U
   ```

3. `.m2/repository` 폴더 삭제 후 재빌드

### 4. JSP 페이지가 보이지 않음

**증상:** 404 오류 또는 다운로드 창이 뜸

**해결방법:**
1. `pom.xml`에 JSP 의존성 확인
   ```xml
   <dependency>
       <groupId>org.apache.tomcat.embed</groupId>
       <artifactId>tomcat-embed-jasper</artifactId>
   </dependency>
   ```

2. `application.properties`의 뷰 설정 확인
   ```properties
   spring.mvc.view.prefix=/WEB-INF/views/
   spring.mvc.view.suffix=.jsp
   ```

### 5. 파일 업로드 실패

**증상:** 파일 업로드 시 오류

**해결방법:**
1. 업로드 경로 확인 및 생성
   ```bash
   # Windows
   mkdir C:\work\sol\images
   
   # Linux/Mac
   mkdir -p /tmp/images
   ```

2. `application.properties` 경로 수정
   ```properties
   product.upload.path=C:/work/sol/images
   # 또는
   product.upload.path=/tmp/images
   ```

### 6. 로그 확인

애플리케이션 로그 확인 방법:
```bash
# 콘솔에서 실행 중이라면 실시간 로그 확인 가능
# 또는 로그 파일 확인 (설정한 경우)
tail -f logs/application.log
```

---

## 📞 지원 및 문의

프로젝트 관련 문의사항이 있으시면 다음으로 연락해주세요:
- **Email**: your-email@example.com
- **GitHub Issues**: [프로젝트 이슈 페이지]

---

## 📝 변경 이력

### Version 1.0.0 (2025-10-24)
- ✅ Spring MVC → Spring Boot 마이그레이션 완료
- ✅ XML 설정 → Java Config + Properties로 변경
- ✅ 내장 Tomcat 서버 적용
- ✅ 소셜 로그인 (Kakao, Naver, Google) 통합
- ✅ JSP 뷰 유지

---

## 📜 라이선스

This project is licensed under the MIT License.

---

## 🎓 학습 참고사항

### Spring Boot vs Spring MVC 차이점

| 항목 | Spring MVC | Spring Boot |
|------|-----------|-------------|
| **설정 방식** | XML 기반 | Java Config + Properties |
| **서버** | 외부 Tomcat 필요 | 내장 Tomcat |
| **의존성 관리** | 개별 라이브러리 | Starter 패키지 |
| **실행 방법** | WAR 배포 → Tomcat 실행 | JAR 실행 또는 mvn spring-boot:run |
| **자동 설정** | 수동 Bean 등록 | Auto Configuration |

### 주요 변경사항

1. **설정 파일 통합**
   - 기존: `web.xml`, `context-*.xml`, `jdbc.properties`, `common.properties`
   - 변경: `application.properties` 하나로 통합

2. **서버 실행**
   - 기존: Tomcat 설치 → WAR 배포 → 서버 시작
   - 변경: `mvn spring-boot:run` 명령어 하나로 실행

3. **의존성 관리**
   - 기존: 20+ 개별 라이브러리 관리
   - 변경: 5개의 Starter로 간소화

---

**Happy Coding! 🎉**
