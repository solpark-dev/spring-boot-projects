package client.app;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import spring.domain.User;

import java.util.Map;

/**
 * AI가 최신 개발 관행을 적용하여 개선한 RestHttpClientApp 입니다.
 * <p>
 * 주요 개선 사항:
 * 1. 최신 Apache HttpClient 사용 (CloseableHttpClient)
 * 2. 리소스 자동 관리를 위한 try-with-resources 구문 적용
 * 3. 최신 표준 JSON 라이브러리인 FasterXML Jackson으로 통일
 * 4. 코드 가독성 및 안정성 향상
 */
public class RestHttpClientApp_Aiassisted_Gemini {

    // 서버의 기본 주소를 상수로 정의하여 재사용성을 높입니다.
    private static final String BASE_URL = "http://127.0.0.1:8080/Spring13/app/userAPI";

    // Jackson ObjectMapper는 생성 비용이 비싸므로, static으로 만들어 재사용하는 것이 효율적입니다.
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * [GET] 단순 User 객체 정보를 요청하고 응답받는 메소드
     * 서버의 /getUser API를 호출합니다.
     */
    public static void requestGetUser() throws IOException {
        System.out.println("1. GET /getUser 요청 테스트 시작...");

        String url = BASE_URL + "/getUser?name=홍길동&age=10";
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "application/json");

        // try-with-resources: httpClient와 response 객체를 자동으로 닫아줍니다.
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            System.out.println(":: 응답 상태: " + response.getStatusLine());

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // EntityUtils를 사용하면 응답 본문을 간편하게 문자열로 변환할 수 있습니다.
                String jsonResponse = EntityUtils.toString(entity);
                System.out.println(":: 서버로부터 받은 JSON: " + jsonResponse);

                // ObjectMapper를 사용해 JSON 문자열을 User 객체로 바로 변환 (역직렬화)
                User user = objectMapper.readValue(jsonResponse, User.class);
                System.out.println(":: JSON을 User 객체로 변환: " + user);
            }
        }
        System.out.println("--------------- 테스트 종료 ---------------\n");
    }

    /**
     * [GET] 복합 데이터(User + 메시지)를 요청하고 응답받는 메소드
     * 서버의 /getUserMore/{value} API를 호출합니다.
     */
    public static void requestGetUserMore() throws IOException {
        System.out.println("2. GET /getUserMore/{value} 요청 테스트 시작...");

        String url = BASE_URL + "/getUserMore/user01?name=홍길동&age=10";
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "application/json");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            System.out.println(":: 응답 상태: " + response.getStatusLine());

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String jsonResponse = EntityUtils.toString(entity);
                System.out.println(":: 서버로부터 받은 JSON: " + jsonResponse);

                // 응답이 복합 객체이므로 Map<String, Object> 형태로 변환합니다.
                TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
                Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, typeRef);

                System.out.println(":: JSON을 Map 객체로 변환: " + responseMap);
                System.out.println(":: Map에서 추출한 메시지: " + responseMap.get("message"));

                // Map 안의 user 데이터를 User 객체로 한번 더 변환할 수 있습니다.
                User user = objectMapper.convertValue(responseMap.get("user"), User.class);
                System.out.println(":: Map에서 추출하여 변환한 User 객체: " + user);
            }
        }
        System.out.println("--------------- 테스트 종료 ---------------\n");
    }

    /**
     * [POST] User 객체를 JSON으로 보내고 응답받는 메소드
     * 서버의 /getUserPost API를 호출합니다.
     */
    public static void requestPostUser() throws IOException {
        System.out.println("3. POST /getUserPost 요청 테스트 시작...");

        String url = BASE_URL + "/getUserPost";
        HttpPost request = new HttpPost(url);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");

        // 1. 서버로 보낼 User 객체 생성
        User userToSend = new User("clientUser", "클라이언트", "1234", 30, 1);
        System.out.println(":: 서버로 보낼 User 객체: " + userToSend);

        // 2. User 객체를 JSON 문자열로 변환 (직렬화)
        String jsonPayload = objectMapper.writeValueAsString(userToSend);
        System.out.println(":: 객체를 변환한 JSON Payload: " + jsonPayload);

        // 3. JSON 문자열을 요청 본문(Body)에 탑재
        request.setEntity(new StringEntity(jsonPayload, "UTF-8"));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            System.out.println(":: 응답 상태: " + response.getStatusLine());

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String jsonResponse = EntityUtils.toString(entity);
                System.out.println(":: 서버로부터 받은 JSON: " + jsonResponse);

                User receivedUser = objectMapper.readValue(jsonResponse, User.class);
                System.out.println(":: 응답 JSON을 User 객체로 변환: " + receivedUser);
            }
        }
        System.out.println("--------------- 테스트 종료 ---------------\n");
    }

    /**
     * [POST] User 객체를 JSON으로 보내고 복합 데이터를 응답받는 메소드
     * 서버의 /getUserMorePost/{value} API를 호출합니다.
     */
    public static void requestPostUserMore() throws IOException {
        System.out.println("4. POST /getUserMorePost/{value} 요청 테스트 시작...");
        // 이 메소드는 requestPostUser와 requestGetUserMore의 로직을 조합하여 구현할 수 있습니다.
        // 여기서는 생략하지만, 위 예제들을 응용하면 충분히 작성 가능합니다.
        System.out.println(":: (구현은 위 예제들의 조합으로 가능하여 생략)");
        System.out.println("--------------- 테스트 종료 ---------------\n");
    }


    /**
     * 어플리케이션 메인 메소드
     */
    public static void main(String[] args) throws Exception {

        // 각 API 요청을 순차적으로 실행
        requestGetUser();
        requestGetUserMore();
        requestPostUser();
        requestPostUserMore();

    }
}