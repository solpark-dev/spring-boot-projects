package json.test;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JSONDecodingTestApp {

	/// main Method
	public static void main(String[] args) {
	
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//JSONValue.
		String data =  "{\"address\":\"서울\",\"age\":1000,\"name\":\"홍길동\"}";
		JSONObject jsonObj = (JSONObject)JSONValue.parse(data);
		//==> 입력값 확인
		System.out.println("JSON Object 확인 : " +jsonObj);
		//==> Data 추출
		System.out.println("==>JSON Object Data 추출 ");
		System.out.println(jsonObj.get("address"));
		System.out.println("\n\n");
		
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//JSONValue Array.
		String arrayData =  "[\"서울\",1000,\"홍길동\"]";
		JSONArray jsonArray = (JSONArray)JSONValue.parse(arrayData);
		//==> 입력값 확인
		System.out.println("JSON Object 확인 : " +jsonObj);
		//==> Data 추출
		System.out.println("==>JSON Object Data 추출 ");
		System.out.println(jsonArray.get(0));
	}
}