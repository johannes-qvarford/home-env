package net.qvarford.homeenvmcp.tools;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.qvarford.homeenvmcp.Tool;
import net.qvarford.homeenvmcp.exceptions.ToolException;
import net.qvarford.homeenvmcp.exceptions.ToolFailureException;
import net.qvarford.homeenvmcp.util.CodeSampleService;

public class ListCodeSamples implements Tool<ListCodeSamples.Arguments> {
  private final CodeSampleService codeSampleService;

  public ListCodeSamples() {
    this.codeSampleService = new CodeSampleService();
  }

  public ListCodeSamples(CodeSampleService codeSampleService) {
    this.codeSampleService = codeSampleService;
  }

  public String name() {
    return "ListCodeSamples";
  }

  @Override
  public Class<Arguments> schema() {
    return Arguments.class;
  }

  public String description() {
    return "Lists all available code sample types and their example names without returning the sample content.";
  }

  public McpSchema.CallToolResult run(
      McpSyncServerExchange ignored, McpSchema.CallToolResult.Builder result, Arguments arguments)
      throws ToolException {

    try {
      Map<String, List<String>> allSamples = codeSampleService.getAllSampleTypes();

      if (allSamples.isEmpty()) {
        result.addTextContent("No code samples available.");
        return result.build();
      }

      result.addTextContent("## Available Code Samples");
      result.addTextContent("");

      int totalSamples = 0;
      for (Map.Entry<String, List<String>> entry : allSamples.entrySet()) {
        String type = entry.getKey();
        List<String> examples = entry.getValue();
        totalSamples += examples.size();

        result.addTextContent(
            "### "
                + type
                + " ("
                + examples.size()
                + " sample"
                + (examples.size() == 1 ? "" : "s")
                + ")");

        for (String example : examples) {
          result.addTextContent("- " + example);
        }
        result.addTextContent("");
      }

      result.addTextContent(
          "**Total: "
              + totalSamples
              + " code sample"
              + (totalSamples == 1 ? "" : "s")
              + " across "
              + allSamples.size()
              + " type"
              + (allSamples.size() == 1 ? "" : "s")
              + "**");

      result.addTextContent("");
      result.addTextContent(
          "Use `GetCodeSample` with a type and optional example to retrieve specific samples.");

      return result.build();
    } catch (IOException e) {
      throw new ToolFailureException("Failed to load code sample catalog: " + e.getMessage());
    }
  }

  public record Arguments() {}
}
