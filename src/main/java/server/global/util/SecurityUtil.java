package server.global.util;

import org.springframework.security.core.context.SecurityContextHolder;
import server.global.security.domain.CustomUserDetails;

import java.util.Optional;

public class SecurityUtil {

	private static final String ANONYMOUS_USER = "anonymousUser";

	public static Optional<String> getLoginMemberId(){
		if (isAnonymousMember()) {
			return Optional.of(ANONYMOUS_USER);
		}
		CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return Optional.ofNullable(user.getMemberId());
	}

	public static boolean isAnonymousMember() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals(ANONYMOUS_USER);
	}

}
