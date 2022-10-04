package account.app.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.EnumSet;

public enum ROLE  {
    ROLE_ACCOUNTANT,
    ROLE_AUDITOR,
    ROLE_ANONYMOUS,
    ROLE_ADMINISTRATOR,
    ROLE_USER;
    public static EnumSet<ROLE> getBusinessGroup(){
        return EnumSet.of(ROLE_ACCOUNTANT, ROLE_AUDITOR, ROLE_ANONYMOUS, ROLE_USER);
    }
    public static EnumSet<ROLE> getNonBusinessGroup(){
        return EnumSet.of(ROLE_ADMINISTRATOR);
    }
}
