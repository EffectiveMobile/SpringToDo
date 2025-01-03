package emobile.by.smertex.springtodo.database.entity.realisation;

import emobile.by.smertex.springtodo.database.entity.interfaces.BaseEntity;
import emobile.by.smertex.springtodo.database.entity.realisation.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User implements BaseEntity<UUID> {

    private UUID id;

    private String email;

    private String password;

    private Role role;
}
