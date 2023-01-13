package de.kaiserpfalzedv.fileserver.client;

import de.kaiserpfalzedv.fileserver.model.client.File;
import io.quarkus.oidc.token.propagation.reactive.AccessTokenRequestReactiveFilter;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

/**
 * FileManagementClient --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2.0.0  2023-01-13
 */
@RegisterRestClient(configKey = "file-management-api")
@RegisterProvider(AccessTokenRequestReactiveFilter.class)
@Path("/api/files")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface FileManagementClient {
    @GET
    public List<File> index();

    @GET
    @Path("/{id}")
    public File index(
            @PathParam("id")
            final UUID id
    );

    @GET
    @Path("/{namespace}/{name}")
    public File index(
            @PathParam("namespace")
            final String nameSpace,

            @PathParam("name")
            final String name
    );
}
