package oil;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Test {
	public static void main(String [] args) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		//$2a$10$W4YjFXTyVFS97N4MH4G7aOSmMY0FtY5dkvCuZelHpZ6tK6WkjpLRK
//		System.out.print(passwordEncoder.encode("1111"));
		System.out.println(passwordEncoder.matches("1111","$2a$10$W4YjFXTyVFS97N4MH4G7aOSmMY0FtY5dkvCuZelHpZ6tK6WkjpLRK"));
	}
}
