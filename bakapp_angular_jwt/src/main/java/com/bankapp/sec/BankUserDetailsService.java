package com.bankapp.sec;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BankUserDetailsService implements UserDetailsService {

	private final Map<String, UserDetails> users = new HashMap<>();

	public BankUserDetailsService(PasswordEncoder passwordEncoder) {
		users.put("ekta", User.withUsername("ekta").password(passwordEncoder.encode("ekta123")).roles("CLERK").build());
		users.put("raj", User.withUsername("raj").password(passwordEncoder.encode("raj123")).roles("MGR").build());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = users.get(username);

		if (user == null) {
			throw new UsernameNotFoundException("User not found: " + username);
		}
		return user;
	}
}