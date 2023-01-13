/*
 * Copyright (c) 2022 Kaiserpfalz EDV-Service, Roland T. Lichti
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.fileserver.model.jpa;

import de.kaiserpfalzedv.commons.core.resources.Resource;
import de.kaiserpfalzedv.commons.core.resources.Status;
import de.kaiserpfalzedv.commons.jpa.AbstractJPAEntity;
import de.kaiserpfalzedv.commons.core.resources.Metadata;
import de.kaiserpfalzedv.commons.core.resources.Pointer;
import de.kaiserpfalzedv.fileserver.model.FileData;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * JPAFile -- A file saved inside the database (normally image files).
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-03-26
 */
@RegisterForReflection
@Entity
@Table(
        schema = "FILESTORE",
        name = "FILES",
        uniqueConstraints = {
                @UniqueConstraint(name = "FILES_ID_UK", columnNames = "ID"),
                @UniqueConstraint(name = "FILES_NAME_UK", columnNames = {"NAMESPACE", "NAME"})
        }
)
@Schema(
        title = "File",
        description = "A single file with its meta data"
)
@Jacksonized
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class File extends AbstractJPAEntity implements de.kaiserpfalzedv.fileserver.model.File {
    public static final int MAX_FILE_LENGTH = 16_777_215;

    @Schema(
            description = "A namespace of the resource",
            required = true,
            minLength = VALID_NAME_MIN_LENGTH,
            maxLength = VALID_NAME_MAX_LENGTH,
            example = VALID_NAME_EXAMPLE,
            pattern = VALID_NAME_PATTERN
    )
    @Column(name = "NAMESPACE", length = VALID_NAME_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(min = VALID_NAME_MIN_LENGTH, max = VALID_NAME_MAX_LENGTH, message = VALID_NAME_LENGTH_MSG)
    @Pattern(regexp = VALID_NAME_PATTERN, message = VALID_NAME_PATTERN_MSG)
    private String nameSpace;

    @Schema(
            description = "The name of the resource",
            required = true,
            minLength = VALID_NAME_MIN_LENGTH,
            maxLength = VALID_NAME_MAX_LENGTH,
            example = VALID_NAME_EXAMPLE,
            pattern = VALID_NAME_PATTERN
    )
    @Column(name = "NAME", length = VALID_NAME_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(min = VALID_NAME_MIN_LENGTH, max = VALID_NAME_MAX_LENGTH, message = VALID_NAME_LENGTH_MSG)
    @Pattern(regexp = VALID_NAME_PATTERN, message = VALID_NAME_PATTERN_MSG)
    private String name;

    @Schema(
            description = "The owner of the resource",
            required = true,
            minLength = VALID_NAME_MIN_LENGTH,
            maxLength = VALID_NAME_MAX_LENGTH,
            example = VALID_NAME_EXAMPLE,
            pattern = VALID_NAME_PATTERN
    )
    @Column(name = "OWNER", length = VALID_NAME_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(min = VALID_NAME_MIN_LENGTH, max = VALID_NAME_MAX_LENGTH, message = VALID_NAME_LENGTH_MSG)
    @Pattern(regexp = VALID_NAME_PATTERN, message = VALID_NAME_PATTERN_MSG)
    private String owner;

    @Schema(
            description = "The group of the owner of the resource",
            required = true,
            minLength = VALID_NAME_MIN_LENGTH,
            maxLength = VALID_NAME_MAX_LENGTH,
            example = VALID_NAME_EXAMPLE,
            pattern = VALID_NAME_PATTERN
    )
    @Column(name = "GRP", length = VALID_NAME_MAX_LENGTH, nullable = false)
    @Size(min = VALID_NAME_MIN_LENGTH, max = VALID_NAME_MAX_LENGTH, message = VALID_NAME_LENGTH_MSG)
    @Pattern(regexp = VALID_NAME_PATTERN, message = VALID_NAME_PATTERN_MSG)
    @Builder.Default
    private String group = "unspecified";

    @Schema(
            description = "The permissions as a string with 3 numbers following the posix idea.",
            minLength = 3,
            maxLength = 3,
            example = "620",
            pattern= "^[0246]{3}"
    )
    @Column(name = "PERMISSIONS", length = 3, nullable = false)
    @Size(min = 3, max = 3)
    @Pattern(regexp = "^[0246]{3}", message = "The variable must consist of the digits 0, 2, 4, or 6.")
    @Builder.Default
    private String permissions = "620";

    @Embedded
    @NotNull
    private FileDescription file;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "PREVIEW_NAME")),
            @AttributeOverride(name = "data", column = @Column(name = "PREVIEW_DATA")),
            @AttributeOverride(name = "mediaType", column = @Column(name = "PREVIEW_MEDIATYPE"))
    })
    private FileDescription preview;

    public File(de.kaiserpfalzedv.fileserver.model.File orig) {
        id = orig.getMetadata().getUid();
        version = orig.getMetadata().getGeneration();
        created = orig.getMetadata().getCreated();
        modified = orig.getMetadata().getModified();

        nameSpace = orig.getNameSpace();
        name = orig.getName();
        owner = orig.getMetadata().getAnnotation(ANNOTATION_OWNER).orElse(null);
        group = orig.getMetadata().getAnnotation(ANNOTATION_GROUP).orElse(null);
        permissions = orig.getMetadata().getAnnotation(ANNOTATION_PERMISSIONS).orElse(DEFAULT_PERMISSION);

        file = new FileDescription(orig.getFile());

        if (orig.getPreview() != null) {
            preview = new FileDescription(orig.getPreview());
        }
    }

    @Override
    public Metadata getMetadata() {
        return Metadata.builder()
                .uid(id)
                .generation(version)
                .created(created)
                .modified(modified)
                .identity(Pointer.builder()
                        .kind(KIND)
                        .apiVersion(API_VERSION)
                        .nameSpace(nameSpace)
                        .name(name)
                        .build())
                .annotations(generateAnnotations())
                .build();
    }

    public int[] getPermissions() {
        return new int[] {
                Integer.valueOf(permissions.substring(0,1)),
                Integer.valueOf(permissions.substring(1,1)),
                Integer.valueOf(permissions.substring(2,1))
        };
    }

    public Optional<String> getGroup() {
        return Optional.ofNullable(group);
    }


    private Map<String, String> generateAnnotations() {
        HashMap<String, String> result = new HashMap<>();

        result.put(de.kaiserpfalzedv.fileserver.model.File.ANNOTATION_OWNER, owner);
        result.put(de.kaiserpfalzedv.fileserver.model.File.ANNOTATION_GROUP, group);
        result.put(de.kaiserpfalzedv.fileserver.model.File.ANNOTATION_PERMISSIONS, permissions);

        return result;
    }

    @Override
    public File clone() {
        return toBuilder().build();
    }

    @Override
    public Resource<FileData> increaseGeneration() {
        return toBuilder()
                .version(version++)
                .build();
    }

    @Override
    public FileData getSpec() {
        return de.kaiserpfalzedv.fileserver.model.jpa.FileData.builder()
                .file(file)
                .preview(preview)
                .build();
    }

    @Override
    public Status getStatus() {
        return Status.builder()
                .observedGeneration(version)
                .build();
    }
}
