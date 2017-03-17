/**
 * 
 */
package cn.hm.oil.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Administrator
 *
 */
public class LoginUserInfo extends SysUsers implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean enabled;
	
	private Collection<GrantedAuthority> authorities;
	
	public LoginUserInfo() {
		super();
	}

	public LoginUserInfo(String username, String password, Collection<GrantedAuthority> authorities) {
		super();
		this.setUsername(username);
		this.setPassword(password);
		this.authorities = authorities;
	}
	
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
