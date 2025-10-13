package spring.web.domain;

import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {


	///Field
	private String id;
	private String pwd;
	private int age;
	
	///Constructor
	
	///Method

	
}
