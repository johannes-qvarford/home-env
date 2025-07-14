package net.qvarford;

import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("")
public class GreetingResource {
    @Path("/filesystem/read-file")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "read-file", description = """
        Read a file using an absolute path.
        My user is "jq".
        My home directory is "/home/jq/"
        If you need a file from the home directory try "/home/jq/<filename>" or "/home/jq/<sub directories>/<filename>"
        """)
    public ReadFileResponse readFile(@Valid ReadFileRequest request) throws IOException {
        String content = Files.readString(Paths.get(URI.create("file://" + request.path())));
        return new ReadFileResponse(content);
    }

    public record ReadFileRequest(String path) {}

    public record ReadFileResponse(String content) {}
}
