package uz.fluxCrm.fluxCrm.crm.config;

import java.util.Map;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

/**
 * Resolver for translating the current tenant-id into the schema to be used for the data source.
 */
@Component
public class CurrentTenantResolver implements CurrentTenantIdentifierResolver<Long>, HibernatePropertiesCustomizer {

    @Override
    public Long resolveCurrentTenantIdentifier() {
        Long accountId = TenantContext.getCurrentAccountId() != null ? TenantContext.getCurrentAccountId() : 0;
        return accountId;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
    
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}
