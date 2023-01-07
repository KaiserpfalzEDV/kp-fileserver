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

import de.kaiserpfalzedv.commons.core.jpa.AbstractJPAEntity;
import de.kaiserpfalzedv.commons.core.resources.Metadata;
import de.kaiserpfalzedv.commons.core.resources.Pointer;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

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
@Jacksonized
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class File extends AbstractJPAEntity implements de.kaiserpfalzedv.fileserver.model.File {
    public static final int MAX_FILE_LENGTH = 16_777_215;

    @Column(name = "NAMESPACE", length = VALID_NAME_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(min = VALID_NAME_MIN_LENGTH, max = VALID_NAME_MAX_LENGTH)
    private String nameSpace;

    @Column(name = "NAME", length = VALID_NAME_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(min = VALID_NAME_MIN_LENGTH, max = VALID_NAME_MAX_LENGTH)
    private String name;

    @Column(name = "OWNER", length = VALID_NAME_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(min = VALID_NAME_MIN_LENGTH, max = VALID_NAME_MAX_LENGTH)
    private String owner;

    @Column(name = "GRP", length = VALID_NAME_MAX_LENGTH, nullable = false)
    @Builder.Default
    @Size(min = VALID_NAME_MIN_LENGTH, max = VALID_NAME_MAX_LENGTH)
    private String group = "unspecified";

    @Column(name = "PERMISSIONS", length = 3, nullable = false)
    @Builder.Default
    @Size(min = 3, max = 3)
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

        file = new FileDescription(orig.getFile().getFile());

        if (orig.getFile().getPreview() != null) {
            preview = new FileDescription(orig.getFile().getPreview());
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
}
