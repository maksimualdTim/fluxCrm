package uz.fluxCrm.fluxCrm.crm.service;


import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import uz.fluxCrm.fluxCrm.crm.exception.UserAlreadyExistsException;

@Service
public class KeycloakService {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public KeycloakService (Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public UUID createUser(String firstName, String lastName, String email, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEmailVerified(true);
        user.setEnabled(true);

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);

        user.setCredentials(Collections.singletonList(credentials));

        Response response = null;
        try {
            response = keycloak.realm(realm).users().create(user);
    
            if (response.getStatus() == 201) {
                String location = response.getHeaderString("Location");
                String uuid = location.substring(location.lastIndexOf("/") + 1);
                return UUID.fromString(uuid);
            } else if (response.getStatus() == 409) {
                throw new UserAlreadyExistsException("User already exists with email " + email);
            } else {
                throw new RuntimeException("Ошибка при создании пользователя: " + response.getStatus());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while creating user: " + e.getMessage(), e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public void createGroup(String groupName) {
        GroupRepresentation group = new GroupRepresentation();
        group.setName(groupName);

        keycloak.realm(realm).groups().add(group);
    }

    public void addUserToGroup(String username, String groupName) {
        String userId = keycloak.realm(realm).users().search(username).get(0).getId();
        List<GroupRepresentation> groups = keycloak.realm(realm).groups().groups(groupName, 0, 1);

        if (groups.isEmpty()) {
            throw new RuntimeException("Group not found: " + groupName);
        }

        GroupRepresentation group = groups.get(0);

        GroupResource groupResource = keycloak.realm(realm).groups().group(group.getId());
        groupResource.members().stream()
            .filter(member -> member.getId().equals(userId))
            .findAny()
            .ifPresent(member -> {
                throw new RuntimeException("User already in group");
            });

        keycloak.realm(realm).users().get(userId).joinGroup(group.getId());
    }
}
