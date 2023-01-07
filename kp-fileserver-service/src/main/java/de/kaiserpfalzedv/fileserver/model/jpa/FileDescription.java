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

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

/**
 * FileData -- An embedded file (byte coded).
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 2.0.0  2021-12-31
 * @since 2.0.0  2022-01-16
 */
@Embeddable
@RegisterForReflection
@Jacksonized
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
public class FileDescription implements de.kaiserpfalzedv.fileserver.model.FileDescription {
    @Column(name = "FILE_NAME", length = VALID_NAME_MAX_LENGTH)
    @ToString.Include
    private String name;

    @Lob
    @Column(name = "FILE_DATA", length = File.MAX_FILE_LENGTH)
    private byte[] data;

    @Column(name = "FILE_MEDIATYPE")
    @ToString.Include
    private String mediaType;


    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public FileDescription clone() {
        return toBuilder().build();
    }

    public FileDescription(de.kaiserpfalzedv.fileserver.model.FileDescription orig) {
        name = orig.getName();
        mediaType = orig.getMediaType();
        data = orig.getData();
    }
}
