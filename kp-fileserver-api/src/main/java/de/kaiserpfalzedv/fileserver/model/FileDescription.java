package de.kaiserpfalzedv.fileserver.model;

import de.kaiserpfalzedv.commons.core.resources.HasData;
import de.kaiserpfalzedv.commons.core.resources.HasName;

import java.io.Serializable;

/**
 * FileDescription --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2.0.0  2023-01-07
 */
public interface FileDescription extends HasName, HasData, Serializable, Cloneable {
    String getName();

    String getMediaType();

    byte[] getData();
}
