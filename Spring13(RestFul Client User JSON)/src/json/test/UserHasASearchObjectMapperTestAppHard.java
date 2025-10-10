package json.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import spring.domain.Search;
import spring.domain.UserHasASearch;

public class UserHasASearchObjectMapperTestAppHard {
	
	/// main Method
	public static void main(String[] args) throws Exception {
		
		// ObjectMapper : Jackson 라이브러리의 핵심 클래스
		ObjectMapper objectMapper = new ObjectMapper();
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 1. 기본 : UserHasASearch 인스턴스 => JSON Object로 변경
		System.out.println("1. 기본 : UserHasASearch (Has-A 관계) => JSON 변환");
		System.out.println("==================================================");
		
		// Search 객체 생성
		Search search = new Search();
		search.setUserName(new String[]{"홍길동", "이순신", "김유신"});
		
		ArrayList<String> userIdList = new ArrayList<String>();
		userIdList.add("user01");
		userIdList.add("user02");
		userIdList.add("user03");
		search.setUserId(userIdList);
		search.setSearchCondition("이름검색");
		
		// UserHasASearch 객체 생성 (Search 포함)
		UserHasASearch userHasASearch = new UserHasASearch("user01", "홍길동", "1111", 25, 10);
		userHasASearch.setActive(true);
		userHasASearch.setRegDate(new Timestamp(System.currentTimeMillis()));
		userHasASearch.setSearch(search);  // Has-A 관계 설정
		
		System.out.println("변환 전 UserHasASearch : ");
		System.out.println(userHasASearch);
		
		// UserHasASearch => JSON 변환
		String jsonString = objectMapper.writeValueAsString(userHasASearch);
		System.out.println("\nJSON 변환 결과 : ");
		System.out.println(jsonString);
		
		// Pretty Print로 구조 확인
		String prettyJson = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(userHasASearch);
		System.out.println("\nPretty JSON (구조 확인) : ");
		System.out.println(prettyJson);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 2. JSON => UserHasASearch 역변환
		System.out.println("2. JSON => UserHasASearch 역변환 (중첩된 객체 복원)");
		System.out.println("==================================================");
		
		// JSON을 UserHasASearch로 역변환
		UserHasASearch restoredUser = objectMapper.readValue(jsonString, UserHasASearch.class);
		System.out.println("복원된 UserHasASearch : ");
		System.out.println(restoredUser);
		
		// 중첩된 Search 객체가 제대로 복원되었는지 확인
		System.out.println("\n중첩된 Search 객체 확인 : ");
		Search restoredSearch = restoredUser.getSearch();
		System.out.println("  - SearchCondition : " + restoredSearch.getSearchCondition());
		System.out.println("  - UserName 배열 : " + Arrays.toString(restoredSearch.getUserName()));
		System.out.println("  - UserId 리스트 : " + restoredSearch.getUserId());
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 3. List<UserHasASearch> => JSON 배열
		System.out.println("3. List<UserHasASearch> => JSON 배열 변환");
		System.out.println("==================================================");
		
		List<UserHasASearch> userList = new ArrayList<UserHasASearch>();
		
		// 첫 번째 사용자
		Search search1 = new Search();
		search1.setUserName(new String[]{"홍길동"});
		search1.setSearchCondition("이름검색");
		UserHasASearch user1 = new UserHasASearch("user01", "홍길동", "1111", 25, 10);
		user1.setSearch(search1);
		userList.add(user1);
		
		// 두 번째 사용자 
		Search search2 = new Search();
		search2.setUserName(new String[]{"이순신", "강감찬"});
		ArrayList<String> idList2 = new ArrayList<String>();
		idList2.add("admin01");
		idList2.add("admin02");
		search2.setUserId(idList2);
		search2.setSearchCondition("관리자검색");
		UserHasASearch user2 = new UserHasASearch("user02", "이순신", "2222", 30, 20);
		user2.setSearch(search2);
		userList.add(user2);
		
		// List => JSON 변환
		String jsonListString = objectMapper.writeValueAsString(userList);
		System.out.println("List<UserHasASearch> => JSON : ");
		System.out.println(jsonListString);
		
		// Pretty Print
		String prettyListJson = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(userList);
		System.out.println("\nPretty JSON 배열 : ");
		System.out.println(prettyListJson);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 4. JSON 배열 => List<UserHasASearch> 역변환
		System.out.println("4. JSON 배열 => List<UserHasASearch> 역변환");
		System.out.println("==================================================");
		
		List<UserHasASearch> restoredList = objectMapper.readValue(jsonListString,
				new TypeReference<List<UserHasASearch>>() {});
		
		System.out.println("복원된 List 크기 : " + restoredList.size());
		for (int i = 0; i < restoredList.size(); i++) {
			UserHasASearch u = restoredList.get(i);
			System.out.println("\n[" + i + "] " + u.getUserName() + "의 정보:");
			System.out.println("  - User ID: " + u.getUserId());
			System.out.println("  - Search Condition: " + 
					(u.getSearch() != null ? u.getSearch().getSearchCondition() : "없음"));
		}
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 5. Map<String, UserHasASearch> => JSON
		System.out.println("5. Map<String, UserHasASearch> => JSON 변환");
		System.out.println("==================================================");
		
		Map<String, UserHasASearch> userMap = new HashMap<String, UserHasASearch>();
		userMap.put("일반회원", user1);
		userMap.put("관리자", user2);
		
		// Map => JSON 변환
		String jsonMapString = objectMapper.writeValueAsString(userMap);
		System.out.println("Map<String, UserHasASearch> => JSON : ");
		System.out.println(jsonMapString);
		
		// JSON => Map 역변환
		Map<String, UserHasASearch> restoredMap = objectMapper.readValue(jsonMapString,
				new TypeReference<Map<String, UserHasASearch>>() {});
		System.out.println("\n복원된 Map의 키 : " + restoredMap.keySet());
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 6. 복잡한 중첩 구조 : Map<String, List<UserHasASearch>>
		System.out.println("6. 복잡한 중첩 : Map<String, List<UserHasASearch>>");
		System.out.println("==================================================");
		
		Map<String, List<UserHasASearch>> complexMap = new HashMap<String, List<UserHasASearch>>();
		
		// 일반 회원 리스트
		List<UserHasASearch> normalUsers = new ArrayList<UserHasASearch>();
		normalUsers.add(new UserHasASearch("normal01", "일반1", "1111", 20, 1));
		normalUsers.add(new UserHasASearch("normal02", "일반2", "2222", 25, 1));
		
		// VIP 회원 리스트
		List<UserHasASearch> vipUsers = new ArrayList<UserHasASearch>();
		Search vipSearch = new Search();
		vipSearch.setSearchCondition("VIP검색");
		UserHasASearch vipUser = new UserHasASearch("vip01", "VIP회원", "9999", 35, 99);
		vipUser.setSearch(vipSearch);
		vipUsers.add(vipUser);
		
		complexMap.put("일반회원", normalUsers);
		complexMap.put("VIP회원", vipUsers);
		
		// 복잡한 Map => JSON 변환
		String complexJson = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(complexMap);
		System.out.println("복잡한 중첩 구조 JSON : ");
		System.out.println(complexJson);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 7. null 처리 및 빈 객체 처리
		System.out.println("7. null과 빈 객체 처리");
		System.out.println("==================================================");
		
		// Search가 null인 경우
		UserHasASearch userWithNullSearch = new UserHasASearch("user99", "널테스트", "0000", null, 0);
		userWithNullSearch.setSearch(null);  // Search가 null
		
		String nullJson = objectMapper.writeValueAsString(userWithNullSearch);
		System.out.println("Search가 null인 경우 : ");
		System.out.println(nullJson);
		
		// 빈 Search 객체인 경우
		UserHasASearch userWithEmptySearch = new UserHasASearch("user88", "빈객체", "8888", 88, 88);
		userWithEmptySearch.setSearch(new Search());  // 빈 Search 객체
		
		String emptyJson = objectMapper.writeValueAsString(userWithEmptySearch);
		System.out.println("\nSearch가 빈 객체인 경우 : ");
		System.out.println(emptyJson);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 8. 부분 JSON 파싱 - 특정 필드만 추출
		System.out.println("8. 부분 JSON 파싱 - JsonNode 사용");
		System.out.println("==================================================");
		
		// JsonNode를 사용한 부분 파싱
		org.codehaus.jackson.JsonNode rootNode = objectMapper.readTree(prettyJson);
		
		// 특정 필드 추출
		String extractedUserId = rootNode.get("userId").asText();
		String extractedUserName = rootNode.get("userName").asText();
		System.out.println("추출된 userId : " + extractedUserId);
		System.out.println("추출된 userName : " + extractedUserName);
		
		// 중첩된 객체에서 필드 추출
		org.codehaus.jackson.JsonNode searchNode = rootNode.get("search");
		if (searchNode != null) {
			String searchCondition = searchNode.get("searchCondition").asText();
			System.out.println("추출된 searchCondition : " + searchCondition);
			
			// 배열 필드 추출
			org.codehaus.jackson.JsonNode userNameArray = searchNode.get("userName");
			if (userNameArray != null && userNameArray.isArray()) {
				System.out.println("userName 배열 요소:");
				for (int i = 0; i < userNameArray.size(); i++) {
					System.out.println("  [" + i + "] " + userNameArray.get(i).asText());
				}
			}
		}
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 9. JSON 병합 (Merge) - 두 개의 JSON 합치기
		System.out.println("9. JSON 병합 - 두 객체의 정보 합치기");
		System.out.println("==================================================");
		
		// 첫 번째 객체
		UserHasASearch original = new UserHasASearch("merge01", "원본", "1234", 20, 5);
		Search originalSearch = new Search();
		originalSearch.setSearchCondition("원본검색");
		original.setSearch(originalSearch);
		
		// 두 번째 객체 (업데이트할 정보)
		UserHasASearch update = new UserHasASearch("merge01", "수정됨", "5678", 25, 10);
		Search updateSearch = new Search();
		updateSearch.setSearchCondition("수정된검색");
		updateSearch.setUserName(new String[]{"추가된이름"});
		update.setSearch(updateSearch);
		
		System.out.println("원본 객체 : " + original.getUserName());
		System.out.println("수정 객체 : " + update.getUserName());
		
		// JSON으로 변환 후 병합 시뮬레이션
		String originalJson = objectMapper.writeValueAsString(original);
		String updateJson = objectMapper.writeValueAsString(update);
		
		System.out.println("\n원본 JSON : " + originalJson);
		System.out.println("수정 JSON : " + updateJson);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 10. 성능 테스트 - 대량 데이터 변환
		System.out.println("10. 성능 테스트 - 1000개 객체 변환");
		System.out.println("==================================================");
		
		List<UserHasASearch> largeList = new ArrayList<UserHasASearch>();
		for (int i = 0; i < 1000; i++) {
			UserHasASearch tempUser = new UserHasASearch(
				"user" + i, 
				"사용자" + i, 
				"pass" + i, 
				20 + (i % 30), 
				i % 100
			);
			
			if (i % 10 == 0) {  // 10개마다 Search 객체 추가
				Search tempSearch = new Search();
				tempSearch.setSearchCondition("검색조건" + i);
				tempUser.setSearch(tempSearch);
			}
			largeList.add(tempUser);
		}
		
		// 변환 시간 측정
		long startTime = System.currentTimeMillis();
		String largeJson = objectMapper.writeValueAsString(largeList);
		long endTime = System.currentTimeMillis();
		
		System.out.println("1000개 객체 => JSON 변환 시간 : " + (endTime - startTime) + "ms");
		System.out.println("생성된 JSON 크기 : " + largeJson.length() + " characters");
		
		// 역변환 시간 측정
		startTime = System.currentTimeMillis();
		List<UserHasASearch> restoredLargeList = objectMapper.readValue(largeJson,
				new TypeReference<List<UserHasASearch>>() {});
		endTime = System.currentTimeMillis();
		
		System.out.println("JSON => 1000개 객체 역변환 시간 : " + (endTime - startTime) + "ms");
		System.out.println("복원된 객체 수 : " + restoredLargeList.size());
		
		System.out.println("\n==================================================");
		System.out.println("프로그램 종료 - Has-A 관계 JSON 변환 학습 완료!");
		System.out.println("==================================================");
	}
}