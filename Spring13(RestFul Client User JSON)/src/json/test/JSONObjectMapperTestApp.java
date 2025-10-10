package json.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import spring.domain.User;

public class JSONObjectMapperTestApp {
	
	/// main Method
	public static void main(String[] args) throws Exception {
		
		// ObjectMapper : Jackson 라이브러리의 핵심 클래스
		ObjectMapper objectMapper = new ObjectMapper();
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 1. User 인스턴스 => JSON Object로 변경
		System.out.println("1. User 인스턴스 => JSON Object로 변경");
		System.out.println("==================================================");
		
		// User 인스턴스 생성
		User user = new User("user01", "홍길동", "1111", null, 10);
		System.out.println("변환 전 User 객체 : " + user);
		
		// User 객체를 JSON 문자열로 변환 (Serialization)
		String jsonString = objectMapper.writeValueAsString(user);
		System.out.println("User => JSON 변환 결과 : " + jsonString);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 2. JSON Object => User 인스턴스 생성
		System.out.println("2. JSON Object => User 인스턴스 생성");
		System.out.println("==================================================");
		
		// JSON 문자열을 User 객체로 변환 (Deserialization)
		User userFromJson = objectMapper.readValue(jsonString, User.class);
		System.out.println("JSON => User 변환 결과 : " + userFromJson);
		
		// 변환된 객체의 각 필드값 확인
		System.out.println("  - userId : " + userFromJson.getUserId());
		System.out.println("  - userName : " + userFromJson.getUserName());
		System.out.println("  - password : " + userFromJson.getPassword());
		System.out.println("  - age : " + userFromJson.getAge());
		System.out.println("  - grade : " + userFromJson.getGrade());
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 3. List<User> => JSON Object로 변경
		System.out.println("3. List<User> => JSON Object로 변경");
		System.out.println("==================================================");
		
		// List<User> 생성 및 User 인스턴스 2개 추가
		List<User> userList = new ArrayList<User>();
		userList.add(new User("user01", "홍길동", "1111", 25, 10));
		userList.add(new User("user02", "이순신", "2222", 30, 20));
		System.out.println("변환 전 List<User> 크기 : " + userList.size());
		System.out.println("변환 전 List<User> : " + userList);
		
		// List<User>를 JSON 문자열로 변환
		String jsonListString = objectMapper.writeValueAsString(userList);
		System.out.println("List<User> => JSON 변환 결과 : " + jsonListString);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 4. JSON Object => List<User> 다시 생성
		System.out.println("4. JSON Object => List<User> 다시 생성");
		System.out.println("==================================================");
		
		// JSON 문자열을 List<User>로 변환
		// TypeReference를 사용하여 제네릭 타입 정보 전달
		List<User> userListFromJson = objectMapper.readValue(jsonListString, 
				new TypeReference<List<User>>() {});
		System.out.println("JSON => List<User> 변환 결과 크기 : " + userListFromJson.size());
		System.out.println("JSON => List<User> 변환 결과 : " + userListFromJson);
		
		// List 내의 각 User 객체 확인
		for(int i = 0; i < userListFromJson.size(); i++) {
			System.out.println("  List[" + i + "] : " + userListFromJson.get(i));
		}
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 5. Map<String, User> => JSON Object로 변경
		System.out.println("5. Map<String, User> => JSON Object로 변경");
		System.out.println("==================================================");
		
		// Map<String, User> 생성 및 User 객체 2개 추가
		Map<String, User> userMap = new HashMap<String, User>();
		userMap.put("첫번째사용자", new User("user01", "홍길동", "1111", 25, 10));
		userMap.put("두번째사용자", new User("user02", "이순신", "2222", 30, 20));
		System.out.println("변환 전 Map<String, User> 크기 : " + userMap.size());
		System.out.println("변환 전 Map<String, User> : " + userMap);
		
		// Map<String, User>를 JSON 문자열로 변환
		String jsonMapString = objectMapper.writeValueAsString(userMap);
		System.out.println("Map<String, User> => JSON 변환 결과 : " + jsonMapString);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 6. JSON Object => Map<String, User> 다시 생성
		System.out.println("6. JSON Object => Map<String, User> 다시 생성");
		System.out.println("==================================================");
		
		// JSON 문자열을 Map<String, User>로 변환
		// TypeReference를 사용하여 제네릭 타입 정보 전달
		Map<String, User> userMapFromJson = objectMapper.readValue(jsonMapString, 
				new TypeReference<Map<String, User>>() {});
		System.out.println("JSON => Map<String, User> 변환 결과 크기 : " + userMapFromJson.size());
		System.out.println("JSON => Map<String, User> 변환 결과 : " + userMapFromJson);
		
		// Map의 각 Entry 확인
		for(Map.Entry<String, User> entry : userMapFromJson.entrySet()) {
			System.out.println("  Key: " + entry.getKey() + ", Value: " + entry.getValue());
		}
		
		System.out.println("\n==================================================");
		System.out.println("추가 학습 내용");
		System.out.println("==================================================");
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 추가1: Pretty Print (보기 좋게 정렬된 JSON 출력)
		System.out.println("\n[추가1] Pretty Print - 보기 좋게 정렬된 JSON");
		String prettyJson = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(new User("user03", "김유신", "3333", 35, 30));
		System.out.println("Pretty JSON:\n" + prettyJson);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 추가2: null 값 처리
		System.out.println("\n[추가2] null 값이 있는 User 객체의 JSON 변환");
		User userWithNull = new User("user04", "강감찬", "4444", null, 40);
		String jsonWithNull = objectMapper.writeValueAsString(userWithNull);
		System.out.println("null을 포함한 JSON : " + jsonWithNull);
		System.out.println("=> age 필드가 null로 표시됨");
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 추가3: JSON 파싱 에러 처리 예시
		System.out.println("\n[추가3] 잘못된 JSON 파싱 시 에러 처리");
		String wrongJson = "{\"userId\":\"user05\", \"wrongField\":\"wrongValue\"}";
		try {
			User wrongUser = objectMapper.readValue(wrongJson, User.class);
			System.out.println("파싱 성공 : " + wrongUser);
			System.out.println("=> Jackson은 존재하지 않는 필드를 무시하고, 없는 필드는 기본값으로 처리");
		} catch (Exception e) {
			System.out.println("파싱 실패 : " + e.getMessage());
		}
		
		System.out.println("\n==================================================");
		System.out.println("프로그램 종료");
		System.out.println("==================================================");
	}
}