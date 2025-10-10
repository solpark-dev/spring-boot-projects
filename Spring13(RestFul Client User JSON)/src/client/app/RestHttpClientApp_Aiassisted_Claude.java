package client.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import spring.domain.User;

/**
 * REST API 클라이언트 - AI가 개선한 버전
 * 
 * 개선사항:
 * 1. 중복 코드 제거 (헬퍼 메서드 추가)
 * 2. try-with-resources로 자동 리소스 관리
 * 3. 더 명확한 메서드명
 * 4. 상세한 주석
 * 5. 최신 HttpClient API 사용
 */
public class RestHttpClientApp_Aiassisted_Claude {
    
    // 서버 기본 URL을 상수로 정의 (변경 용이)
    private static final String BASE_URL = "http://127.0.0.1:8080/Spring13/app/userAPI";
    private static final String CHARSET = "UTF-8";
    
    // ObjectMapper는 thread-safe하므로 재사용 가능
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JSONParser jsonParser = new JSONParser();
    
    public static void main(String[] args) {
        try {
            System.out.println("\n========== REST API 클라이언트 테스트 시작 ==========\n");
            
            // 1. GET 요청 테스트
            System.out.println("📌 1.1 GET 요청 - JsonSimple 사용");
            System.out.println("─────────────────────────────────");
            testGetRequest_JsonSimple();
            
            System.out.println("\n📌 1.2 GET 요청 - Jackson 사용");
            System.out.println("─────────────────────────────────");
            testGetRequest_Jackson();
            
            // 2. POST 요청 테스트
            System.out.println("\n📌 2.1 POST 요청 - JsonSimple 사용");
            System.out.println("─────────────────────────────────");
            testPostRequest_JsonSimple();
            
            System.out.println("\n📌 2.2 POST 요청 - Jackson 사용");
            System.out.println("─────────────────────────────────");
            testPostRequest_Jackson();
            
            System.out.println("\n========== 모든 테스트 완료 ==========");
            
        } catch (Exception e) {
            System.err.println("❌ 에러 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 1.1 GET 요청 - JsonSimple 라이브러리 사용
     * 간단한 JSON 파싱에 적합
     */
    public static void testGetRequest_JsonSimple() throws Exception {
        String url = BASE_URL + "/getUser?name=홍길동&age=10";
        
        // try-with-resources: 자동으로 close() 호출
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            
            HttpGet httpGet = createGetRequest(url);
            
            // 요청 실행 및 응답 받기
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                
                // 응답 JSON 파싱
                String jsonResponse = EntityUtils.toString(response.getEntity(), CHARSET);
                JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonResponse);
                
                // 결과 출력
                printJsonResponse("GET (JsonSimple)", jsonObject);
                System.out.println("   userId: " + jsonObject.get("userId"));
                System.out.println("   userName: " + jsonObject.get("userName"));
                System.out.println("   age: " + jsonObject.get("age"));
            }
        }
    }
    
    /**
     * 1.2 GET 요청 - Jackson 라이브러리 사용
     * 복잡한 객체 매핑에 적합
     */
    public static void testGetRequest_Jackson() throws Exception {
        String url = BASE_URL + "/getUserMore/user01?name=홍길동&age=10";
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            
            HttpGet httpGet = createGetRequest(url);
            
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                
                String jsonResponse = EntityUtils.toString(response.getEntity(), CHARSET);
                
                // Jackson으로 Map으로 변환
                Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);
                
                // Map에서 User 객체 추출
                Object userJson = responseMap.get("user");
                User user = objectMapper.convertValue(userJson, User.class);
                
                // 결과 출력
                System.out.println("✅ GET (Jackson) 응답 수신");
                System.out.println("   User 객체: " + user);
                System.out.println("   Message: " + responseMap.get("message"));
            }
        }
    }
    
    /**
     * 2.1 POST 요청 - JsonSimple 사용
     * JSON 데이터를 Body에 담아 전송
     */
    @SuppressWarnings("unchecked")
    public static void testPostRequest_JsonSimple() throws Exception {
        String url = BASE_URL + "/getUserPost";
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            
            HttpPost httpPost = createPostRequest(url);
            
            // 전송할 JSON 데이터 생성
            JSONObject requestJson = new JSONObject();
            requestJson.put("userId", "test001");
            requestJson.put("userName", "김철수");
            requestJson.put("age", 25);
            
            // JSON을 Body에 설정
            setJsonBody(httpPost, requestJson.toJSONString());
            
            // 요청 실행
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                
                String jsonResponse = EntityUtils.toString(response.getEntity(), CHARSET);
                JSONObject responseJson = (JSONObject) jsonParser.parse(jsonResponse);
                
                // 결과 출력
                printJsonResponse("POST (JsonSimple)", responseJson);
            }
        }
    }
    
    /**
     * 2.2 POST 요청 - Jackson 사용
     * Java 객체를 직접 JSON으로 변환하여 전송
     */
    public static void testPostRequest_Jackson() throws Exception {
        String url = BASE_URL + "/getUserMorePost/user01";
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            
            HttpPost httpPost = createPostRequest(url);
            
            // User 객체 생성 및 JSON 변환
            User requestUser = new User("test002", "이영희", "password123", 30, 5);
            String jsonRequest = objectMapper.writeValueAsString(requestUser);
            
            System.out.println("📤 전송할 User 객체: " + requestUser);
            
            // JSON을 Body에 설정
            setJsonBody(httpPost, jsonRequest);
            
            // 요청 실행
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                
                String jsonResponse = EntityUtils.toString(response.getEntity(), CHARSET);
                Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);
                
                // User 객체로 변환
                Object userJson = responseMap.get("user");
                User responseUser = objectMapper.convertValue(userJson, User.class);
                
                // 결과 출력
                System.out.println("✅ POST (Jackson) 응답 수신");
                System.out.println("   응답 User 객체: " + responseUser);
                System.out.println("   Message: " + responseMap.get("message"));
            }
        }
    }
    
    //================== 헬퍼 메서드들 ==================//
    
    /**
     * GET 요청 객체 생성 (공통 헤더 설정)
     */
    private static HttpGet createGetRequest(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");
        System.out.println("🔗 GET 요청 URL: " + url);
        return httpGet;
    }
    
    /**
     * POST 요청 객체 생성 (공통 헤더 설정)
     */
    private static HttpPost createPostRequest(String url) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        System.out.println("🔗 POST 요청 URL: " + url);
        return httpPost;
    }
    
    /**
     * POST 요청에 JSON Body 설정
     */
    private static void setJsonBody(HttpPost httpPost, String jsonString) throws Exception {
        StringEntity entity = new StringEntity(jsonString, StandardCharsets.UTF_8);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        System.out.println("📤 전송 JSON: " + jsonString);
    }
    
    /**
     * JSON 응답 출력 (공통 포맷)
     */
    private static void printJsonResponse(String methodName, JSONObject jsonObject) {
        System.out.println("✅ " + methodName + " 응답 수신");
        System.out.println("   JSON 응답: " + jsonObject.toJSONString());
    }
    
    //================== 추가 유틸리티 메서드 ==================//
    
    /**
     * 응답 상태 확인
     */
    private static boolean isSuccessResponse(HttpResponse response) {
        int statusCode = response.getStatusLine().getStatusCode();
        return statusCode >= 200 && statusCode < 300;
    }
    
    /**
     * 에러 응답 처리
     */
    private static void handleErrorResponse(HttpResponse response) throws Exception {
        int statusCode = response.getStatusLine().getStatusCode();
        String reason = response.getStatusLine().getReasonPhrase();
        
        System.err.println("❌ 에러 응답");
        System.err.println("   상태 코드: " + statusCode);
        System.err.println("   이유: " + reason);
        
        if (response.getEntity() != null) {
            String errorBody = EntityUtils.toString(response.getEntity(), CHARSET);
            System.err.println("   에러 내용: " + errorBody);
        }
    }
    
    /**
     * 성능 측정을 위한 메서드 (옵션)
     */
    private static void measureRequestTime(Runnable request, String requestName) {
        long startTime = System.currentTimeMillis();
        
        request.run();
        
        long endTime = System.currentTimeMillis();
        System.out.println("⏱️ " + requestName + " 소요 시간: " + (endTime - startTime) + "ms");
    }
}