package de.kaiserpfalzedv.fileserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.kaiserpfalzedv.commons.core.resources.HasMetadata;
import de.kaiserpfalzedv.commons.core.resources.ResourcePointer;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;

/**
 * File --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2.0.0  2023-01-07
 */
public interface File extends ResourcePointer, HasMetadata {
    String KIND = "File";
    String API_VERSION = "v1";
    String ANNOTATION_OWNER = "de.kaiserpfalz-edv.files/owner";
    String ANNOTATION_GROUP = "de.kaiserpfalz-edv.files/group";
    String ANNOTATION_PERMISSIONS = "de.kaiserpfalz-edv.files/permissions";
    int READ = 2;
    int WRITE = 4;
    int READ_WRITE = 6;

    String DEFAULT_PERMISSION = "222";

    FileData getFile();

    FileData getPreview();

    @JsonIgnore
    Optional<String> getOwner();

    @JsonIgnore
    Optional<String> getGroup();

    @JsonIgnore
    int[] getPermissions();

    @Override
    default String getKind() {
        return KIND;
    }

    default String getApiVersion() {
        return API_VERSION;
    }

    /**
     * Checks if the user has access to this file.
     * <p>
     * The owning user is always granted access to the file. And if no owner is set, the file is always accessable.
     *
     * @param user       The requesting user.
     * @param groups     The groups this user belongs to.
     * @param permission The requested permission ({@link #READ}, {@link #WRITE} or {@link #READ_WRITE}).
     * @return TRUE if the access is granted, FALSE if the access is denied.
     */
    @JsonIgnore
    default boolean hasAccess(@NotNull final String user, @NotNull final Set<String> groups, final int permission) {
        boolean result;
        int[] permissions = getPermissions();

        if (user == null) {
            return false;
        }

        if (getOwner().isEmpty() || getOwner().get().equalsIgnoreCase(user)) {
            return true;
        }

        result = permissions[2] == permission || permissions[1] == READ_WRITE;

        if (getGroup().isPresent()) {
            String group = getGroup().get();

            if (groups.contains(group)) {
                result = permissions[1] == permission || permissions[1] == READ_WRITE;
            }
        }
        return result;
    }
}
