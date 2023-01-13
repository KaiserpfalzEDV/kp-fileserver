/*
 * Copyright (c) 2022 Kaiserpfalz EDV-Service, Roland T. Lichti.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.fileserver.model.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.kaiserpfalzedv.commons.core.resources.ResourceImpl;
import de.kaiserpfalzedv.fileserver.model.FileData;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Optional;

/**
 * FileResource -- An image or any other file saved for the system.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 2.0.0  2021-12-31
 * @since 2.0.0  2022-01-16
 */
@Jacksonized
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Slf4j
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(description = "A file saved in the system.")
public class File extends ResourceImpl<FileData> implements de.kaiserpfalzedv.fileserver.model.File {

    private static final int[] FULL_PERMISSION = {6, 6, 6};
    private static final int[] NO_PERMISSION = {0, 0, 0};

    @Override
    public de.kaiserpfalzedv.fileserver.model.FileDescription getFile() {
        return getSpec().getFile();
    }

    @Override
    public de.kaiserpfalzedv.fileserver.model.FileDescription getPreview() {
        return getSpec().getPreview();
    }

    @Override
    @JsonIgnore
    public String getOwner() {
        return getMetadata().getAnnotation(ANNOTATION_OWNER).orElse("unspecified");
    }

    @Override
    @JsonIgnore
    public Optional<String> getGroup() {
        return getMetadata().getAnnotation(ANNOTATION_GROUP);
    }

    @Override
    @JsonIgnore
    public int[] getPermissions() {
        if (getMetadata().getAnnotation(ANNOTATION_PERMISSIONS).isEmpty()) {
            return FULL_PERMISSION;
        }

        String permissions = getMetadata().getAnnotation(ANNOTATION_PERMISSIONS).get();
        if (permissions.length() != 3) {
            return NO_PERMISSION;
        }

        return new int[]{
                Integer.parseInt(permissions.substring(0, 1), 10),
                Integer.parseInt(permissions.substring(1, 2), 10),
                Integer.parseInt(permissions.substring(2, 3), 10)
        };
    }
}
