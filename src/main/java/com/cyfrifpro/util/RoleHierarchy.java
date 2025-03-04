package com.cyfrifpro.util;

import com.cyfrifpro.model.Role;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class RoleHierarchy {

	private static final Map<Role, Set<Role>> roleHierarchy = new EnumMap<>(Role.class);

	static {
		roleHierarchy.put(Role.MASTER_ADMIN, EnumSet.of(Role.ADMIN));
		roleHierarchy.put(Role.ADMIN, EnumSet.of(Role.TOP_LEVEL));
		roleHierarchy.put(Role.TOP_LEVEL, EnumSet.of(Role.MID_LEVEL, Role.GOVERMENT_MANAGEMENT));
		roleHierarchy.put(Role.MID_LEVEL, EnumSet.of(Role.TEAM_LEADER));
		roleHierarchy.put(Role.TEAM_LEADER, EnumSet.of(Role.SUPPORT_SERVICE));
		roleHierarchy.put(Role.GOVERMENT_MANAGEMENT, EnumSet.of(Role.GOVERMENT));
		roleHierarchy.put(Role.GOVERMENT, EnumSet.of(Role.TEMPLE_ADMIN));
		roleHierarchy.put(Role.TEMPLE_ADMIN, EnumSet.of(Role.GUIDE));
	}

	public static boolean canCreateRole(Role creatorRole, Role targetRole) {
		return roleHierarchy.getOrDefault(creatorRole, EnumSet.noneOf(Role.class)).contains(targetRole);
	}
}
