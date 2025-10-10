package json.test;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONEncodingTestApp {
	
	/// main Method
	public static void main(String[] args) {
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//==> Data 입력(CSV)  {"address":"서울","age":1000,"name":"홍길동"}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", "홍길동");
		jsonObj.put("age", new Integer(1000));
		jsonObj.put("address", "서울");
		//==> 입력값 확인
		System.out.println("JSON Object 확인 : " +jsonObj);
		//==> Data 추출
		System.out.println("==>JSON Object Data 추출 ");
		System.out.println(jsonObj.get("address"));
		System.out.println("\n\n");
		
	
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//==> Data 입력(CSV)  {"address":"서울","age":1000,"name":"홍길동",
		//										"innerJson":{"address":"inner서울","age":1000,"name":"inner홍길동"}}
		JSONObject innerJsonObj = new JSONObject();
		innerJsonObj.put("name", "inner홍길동");
		innerJsonObj.put("age", new Integer(1000));
		innerJsonObj.put("address", "inner서울");
		jsonObj.put("innerJson", innerJsonObj);
		//==> 입력값 확인
		System.out.println("JSON Object 확인 : " +jsonObj);
		//==> Data 추출
		System.out.println("==>JSON Object Data 추출 ");
		System.out.println(jsonObj.get("address"));
		JSONObject returnJsonObj = (JSONObject)jsonObj.get("innerJson");
		System.out.println(returnJsonObj.get("address"));
		System.out.println("\n\n");
		
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//==> Data 입력(CSV)  { "address":"서울","age":1000,"name":"홍길동",
		//							 			"innerJson":{"address":"inner서울","age":1000,"name":"inner홍길동"}}
		//										"career":{"직급2":"대리","직급3":"과장","직급1":"사원"}}
		Map<String, String> map = new HashMap<String, String>();
		map.put("직급1", "사원");
		map.put("직급2", "대리");
		map.put("직급3", "과장");
		jsonObj.put("career", map);
		//==> 입력값 확인
		System.out.println("JSON Object 확인 : " +jsonObj);
		//==> Data 추출
		System.out.println("==>JSON Object Data 추출 ");
		System.out.println(jsonObj.get("career"));
		Map returnMap = (Map)jsonObj.get("career");
		System.out.println(returnMap.get("직급1"));
		System.out.println("\n\n");
		
	
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//==> Data 입력(CSV)  { "address":"서울","age":1000,"name":"홍길동",
		//							 			"innerJson":{"address":"inner서울","age":1000,"name":"inner홍길동"}}
		//										"career":{"직급2":"대리","직급3":"과장","직급1":"사원"},
		//										"number":[1,"2","3"] }
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(new Integer(1));
		jsonArray.add("2");
		jsonArray.add("3");
		jsonObj.put("number", jsonArray);
		//==> 입력값 확인
		System.out.println("JSON Object 확인 : " +jsonObj);
		//==> Data 추출
		System.out.println("==>JSON Object Data 추출 ");
		System.out.println(jsonObj.get("number"));
		JSONArray returnJsonArray=(JSONArray)jsonObj.get("number");
		System.out.println(returnJsonArray.get(0));
	}
}