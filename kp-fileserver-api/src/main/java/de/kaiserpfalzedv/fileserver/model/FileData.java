package de.kaiserpfalzedv.fileserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.kaiserpfalzedv.commons.core.resources.HasData;
import de.kaiserpfalzedv.commons.core.resources.HasName;

import java.io.Serializable;

/**
 * FileData --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2.0.0  2023-01-07
 */
public interface FileData extends HasName, HasData, HasOutputStream, HasPreview, Serializable, Cloneable {
    @Override
    String getName();

    @Override
    byte[] getData();

    @JsonIgnore
    @Override
    byte[] getPreviewData();

    FileDescription getFile();

    FileDescription getPreview();
}
