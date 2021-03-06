package com.oneiron.login.doc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.couchbase.client.java.repository.annotation.Id;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
public class UserDoc implements UserDetails{

	private static final long serialVersionUID = -4557490710114025505L;

	@Id
	String id; // id
	
	String uid;
	
	String name; // name
	
	String password; // pw
	String role;	// spring role
	
	String useAt; // 사용 여부
	List<String> deskList; // deskList
	
	// google login info 
	
	String joinKinds; // google join
	
	Map<String ,Object> attributes; // google login attributes;	
	String picture; // user picture
	String locale; // locale
	String docType; // documentType
	
	
	private List<GrantedAuthority> authorities;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
