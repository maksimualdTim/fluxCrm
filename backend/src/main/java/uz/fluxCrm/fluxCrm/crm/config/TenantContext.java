package uz.fluxCrm.fluxCrm.crm.config;

import org.springframework.stereotype.Component;

@Component
public class TenantContext {

    private static final ThreadLocal<Long> CURRENT_TENANT = new InheritableThreadLocal<>();

    public static Long getCurrentAccountId() {
        return CURRENT_TENANT.get();
    }

    public static void setCurrentAccountId(Long accountId) {
        CURRENT_TENANT.set(accountId);
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
