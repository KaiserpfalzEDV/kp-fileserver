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

package de.kaiserpfalzedv.fileserver.service;

import de.kaiserpfalzedv.commons.core.api.BaseException;
import de.kaiserpfalzedv.fileserver.model.jpa.File;

/**
 * InvalidFileException -- The file has no data set.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-24
 */
public class InvalidFileException extends BaseException {
    /**
     * The file data is invalid. No spec is defined.
     *
     * @param file The invalid file
     */
    public InvalidFileException(final File file) {
        super(String.format("The file '%s/%s' has no data set!", file.getNameSpace(), file.getName()));
    }
}
