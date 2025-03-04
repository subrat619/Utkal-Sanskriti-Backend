package com.cyfrifpro.config;

import java.util.Collections;
import java.util.Map;

import com.cyfrifpro.model.Role;

public class AppConstants {

	public static final String[] PUBLIC_URLS = { "/**" };

	public static final Map<Role, String[]> ROLE_URLS = Collections.unmodifiableMap(
			Map.of(Role.MASTER_ADMIN, new String[] {}, Role.ADMIN, new String[] {}, Role.TOP_LEVEL, new String[] {},
					Role.MID_LEVEL, new String[] {}, Role.TEAM_LEADER, new String[] {}, Role.CLIENT, new String[] {}));

}