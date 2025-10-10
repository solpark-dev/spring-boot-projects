package json.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import spring.domain.Search;
import spring.domain.UserHasASearch;

public class UserHasASearchObjectMapperTestApp {
	
	/// main Method
	public static void main(String[] args) throws Exception {
		
		// ObjectMapper : Jackson 라이브러리의 핵심 클래스
		ObjectMapper objectMapper = new ObjectMapper();
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 1. UserHasASearch 인스턴스 => JSON Object로 변경
		System.out.println("1. UserHasASearch 인스턴스 => JSON Object로 변경");
		System.out.println("==================================================");
		
		// UserHasASearch 인스턴스 생성 (제공된 패턴 사용)
		UserHasASearch user = new UserHasASearch("user01", "홍길동", "1111", null, 10);
		System.out.println("변환 전 UserHasASearch 객체 : " + user);
		
		// UserHasASearch => JSON 변환
		String jsonString = objectMapper.writeValueAsString(user);
		System.out.println("UserHasASearch => JSON 변환 결과 : " + jsonString);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 2. JSON Object => UserHasASearch 인스턴스 생성
		System.out.println("2. JSON Object => UserHasASearch 인스턴스 생성");
		System.out.println("==================================================");
		
		// JSON => UserHasASearch 역변환
		UserHasASearch userFromJson = objectMapper.readValue(jsonString, UserHasASearch.class);
		System.out.println("JSON => UserHasASearch 변환 결과 : " + userFromJson);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 3. UserHasASearch + Search 포함 => JSON Object로 변경
		System.out.println("3. UserHasASearch (Search 포함) => JSON Object로 변경");
		System.out.println("==================================================");
		
		// UserHasASearch 인스턴스 생성
		UserHasASearch userWithSearch = new UserHasASearch("user02", "이순신", "2222", 30, 20);
		
		// Search 객체 생성 및 설정
		Search search = new Search();
		search.setSearchCondition("이름검색");
		search.setUserName(new String[]{"홍길동", "이순신", "김유신"});
		
		ArrayList<String> userIdList = new ArrayList<String>();
		userIdList.add("user01");
		userIdList.add("user02");
		search.setUserId(userIdList);
		
		// UserHasASearch에 Search 설정
		userWithSearch.setSearch(search);
		
		// JSON 변환
		String jsonWithSearch = objectMapper.writeValueAsString(userWithSearch);
		System.out.println("Search를 포함한 JSON : " + jsonWithSearch);
		
		// Pretty Print
		String prettyJson = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(userWithSearch);
		System.out.println("\nPretty JSON : \n" + prettyJson);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 4. JSON Object => UserHasASearch (Search 포함) 역변환
		System.out.println("4. JSON Object => UserHasASearch (Search 포함) 역변환");
		System.out.println("==================================================");
		
		UserHasASearch restoredUser = objectMapper.readValue(jsonWithSearch, UserHasASearch.class);
		System.out.println("복원된 UserHasASearch : " + restoredUser);
		System.out.println("복원된 Search : " + restoredUser.getSearch());
		System.out.println("  - SearchCondition : " + restoredUser.getSearch().getSearchCondition());
		System.out.println("  - UserName Array : " + Arrays.toString(restoredUser.getSearch().getUserName()));
		System.out.println("  - UserId List : " + restoredUser.getSearch().getUserId());
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 5. List<UserHasASearch> => JSON Object로 변경
		System.out.println("5. List<UserHasASearch> => JSON Object로 변경");
		System.out.println("==================================================");
		
		List<UserHasASearch> userList = new ArrayList<UserHasASearch>();
		
		// 첫 번째 UserHasASearch (Search 없음)
		userList.add(new UserHasASearch("user01", "홍길동", "1111", null, 10));
		
		// 두 번째 UserHasASearch (Search 없음)
		userList.add(new UserHasASearch("user02", "이순신", "2222", 45, 20));
		
		// 세 번째 UserHasASearch (Search 포함)
		UserHasASearch user03 = new UserHasASearch("user03", "김유신", "3333", 35, 30);
		Search search03 = new Search();
		search03.setSearchCondition("VIP검색");
		user03.setSearch(search03);
		userList.add(user03);
		
		// List => JSON 변환
		String jsonList = objectMapper.writeValueAsString(userList);
		System.out.println("List<UserHasASearch> => JSON : " + jsonList);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 6. JSON Object => List<UserHasASearch> 역변환
		System.out.println("6. JSON Object => List<UserHasASearch> 역변환");
		System.out.println("==================================================");
		
		List<UserHasASearch> restoredList = objectMapper.readValue(jsonList,
				new TypeReference<List<UserHasASearch>>() {});
		
		System.out.println("복원된 List 크기 : " + restoredList.size());
		for (int i = 0; i < restoredList.size(); i++) {
			UserHasASearch u = restoredList.get(i);
			System.out.println("[" + i + "] userId=" + u.getUserId() + 
					", userName=" + u.getUserName() + 
					", hasSearch=" + (u.getSearch() != null));
		}
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 7. Map<String, UserHasASearch> => JSON Object로 변경
		System.out.println("7. Map<String, UserHasASearch> => JSON Object로 변경");
		System.out.println("==================================================");
		
		Map<String, UserHasASearch> userMap = new HashMap<String, UserHasASearch>();
		
		// Map에 UserHasASearch 추가
		userMap.put("일반회원", new UserHasASearch("user01", "홍길동", "1111", null, 10));
		userMap.put("실버회원", new UserHasASearch("user02", "이순신", "2222", 45, 20));
		
		// VIP 회원 (Search 포함)
		UserHasASearch vipUser = new UserHasASearch("vip01", "김유신", "9999", 35, 99);
		Search vipSearch = new Search();
		vipSearch.setSearchCondition("VIP전용검색");
		vipSearch.setUserName(new String[]{"VIP"});
		vipUser.setSearch(vipSearch);
		userMap.put("VIP회원", vipUser);
		
		// Map => JSON 변환
		String jsonMap = objectMapper.writeValueAsString(userMap);
		System.out.println("Map<String, UserHasASearch> => JSON : " + jsonMap);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 8. JSON Object => Map<String, UserHasASearch> 역변환
		System.out.println("8. JSON Object => Map<String, UserHasASearch> 역변환");
		System.out.println("==================================================");
		
		Map<String, UserHasASearch> restoredMap = objectMapper.readValue(jsonMap,
				new TypeReference<Map<String, UserHasASearch>>() {});
		
		System.out.println("복원된 Map의 키 : " + restoredMap.keySet());
		for (Map.Entry<String, UserHasASearch> entry : restoredMap.entrySet()) {
			UserHasASearch u = entry.getValue();
			System.out.println("  " + entry.getKey() + " : " + u.getUserName() + 
					" (Search: " + (u.getSearch() != null ? "있음" : "없음") + ")");
		}
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 9. 다양한 UserHasASearch 인스턴스 패턴
		System.out.println("9. 다양한 UserHasASearch 인스턴스 생성 패턴");
		System.out.println("==================================================");
		
		// 패턴 1: 기본 (age가 null)
		UserHasASearch pattern1 = new UserHasASearch("pattern01", "패턴1", "1111", null, 10);
		System.out.println("패턴1 (age=null) : " + objectMapper.writeValueAsString(pattern1));
		
		// 패턴 2: age 값 있음
		UserHasASearch pattern2 = new UserHasASearch("pattern02", "패턴2", "2222", 25, 20);
		System.out.println("패턴2 (age=25) : " + objectMapper.writeValueAsString(pattern2));
		
		// 패턴 3: Search 추가
		UserHasASearch pattern3 = new UserHasASearch("pattern03", "패턴3", "3333", 30, 30);
		Search searchPattern = new Search();
		searchPattern.setSearchCondition("패턴검색");
		pattern3.setSearch(searchPattern);
		System.out.println("패턴3 (Search포함) : " + objectMapper.writeValueAsString(pattern3));
		
		// 패턴 4: 복잡한 Search
		UserHasASearch pattern4 = new UserHasASearch("pattern04", "패턴4", "4444", null, 40);
		Search complexSearch = new Search();
		complexSearch.setSearchCondition("복합검색");
		complexSearch.setUserName(new String[]{"김", "이", "박"});
		ArrayList<String> idList = new ArrayList<String>();
		idList.add("id1");
		idList.add("id2");
		complexSearch.setUserId(idList);
		pattern4.setSearch(complexSearch);
		
		String pattern4Json = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(pattern4);
		System.out.println("패턴4 (복잡한 Search) : \n" + pattern4Json);
		
		System.out.println("\n==================================================");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 10. 실전 예제: 검색 결과 저장
		System.out.println("10. 실전 예제: 사용자별 검색 설정 저장/복원");
		System.out.println("==================================================");
		
		// 홍길동의 검색 설정
		UserHasASearch hongSearch = new UserHasASearch("user01", "홍길동", "1111", null, 10);
		Search hongSetting = new Search();
		hongSetting.setSearchCondition("최신순정렬");
		hongSetting.setUserName(new String[]{"서울", "부산"});  // 지역 필터
		hongSearch.setSearch(hongSetting);
		
		// 이순신의 검색 설정
		UserHasASearch leeSearch = new UserHasASearch("user02", "이순신", "2222", 45, 20);
		Search leeSetting = new Search();
		leeSetting.setSearchCondition("인기순정렬");
		ArrayList<String> categories = new ArrayList<String>();
		categories.add("IT");
		categories.add("경제");
		leeSetting.setUserId(categories);  // 카테고리로 활용
		leeSearch.setSearch(leeSetting);
		
		// 각 사용자의 검색 설정을 JSON으로 저장
		Map<String, String> savedSettings = new HashMap<String, String>();
		savedSettings.put("홍길동_설정", objectMapper.writeValueAsString(hongSearch));
		savedSettings.put("이순신_설정", objectMapper.writeValueAsString(leeSearch));
		
		System.out.println("저장된 검색 설정:");
		for (Map.Entry<String, String> entry : savedSettings.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		
		// 검색 설정 복원
		System.out.println("\n검색 설정 복원:");
		for (Map.Entry<String, String> entry : savedSettings.entrySet()) {
			UserHasASearch loaded = objectMapper.readValue(entry.getValue(), UserHasASearch.class);
			System.out.println(loaded.getUserName() + "님의 검색 설정: " + 
					loaded.getSearch().getSearchCondition());
		}
		
		System.out.println("\n==================================================");
		System.out.println("프로그램 종료 - 지정된 인스턴스 패턴 학습 완료!");
		System.out.println("==================================================");
	}
}