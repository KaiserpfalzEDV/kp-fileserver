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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * FileData -- A generic file.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 2.0.0  2021-12-31
 * @since 2.0.0  2021-12-31
 */
@RegisterForReflection
@Jacksonized
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"file", "preview"})
@Schema(description = "Files saved on the server.")
public class FileData implements de.kaiserpfalzedv.fileserver.model.FileData {
    @Schema(
            description = "Data of the file.",
            example = "{\"name\": \"test.text\", \"mediaType\": \"text/plain\", \"data\": \"Das hier ist Text.\"}"
    )
    @NotNull
    @ToString.Include
    private FileDescription file;

    @Schema(
            description = "Data of the preview.",
            example = "{\"name\": \"test.svg\", \"mediaType\": \"image/svg\", \"data\": \"[...]\"}",
            nullable = true
    )
    private FileDescription preview;

    public FileData(de.kaiserpfalzedv.fileserver.model.FileData orig) {
        file = new FileDescription(orig.getFile());
        preview = new FileDescription(orig.getPreview());
    }

    @JsonIgnore
    @Override
    public String getName() {
        return file.getName();
    }

    @JsonIgnore
    @Override
    public byte[] getData() {
        return file.getData();
    }

    @JsonIgnore
    @Override
    public byte[] getPreviewData() {
        return (preview != null) ? preview.getData() : new byte[0];
    }


    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public FileData clone() {
        return toBuilder().build();
    }
}
