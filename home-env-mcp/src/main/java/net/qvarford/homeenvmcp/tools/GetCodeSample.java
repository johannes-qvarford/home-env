package net.qvarford.homeenvmcp.tools;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import net.qvarford.homeenvmcp.Tool;
import net.qvarford.homeenvmcp.exceptions.ToolException;
import net.qvarford.homeenvmcp.exceptions.ToolFailureException;
import net.qvarford.homeenvmcp.util.CodeSampleService;

public class GetCodeSample implements Tool<GetCodeSample.Arguments> {
  private final CodeSampleService codeSampleService;

  public GetCodeSample() {
    this.codeSampleService = new CodeSampleService();
  }

  public GetCodeSample(CodeSampleService codeSampleService) {
    this.codeSampleService = codeSampleService;
  }

  public String name() {
    return "GetCodeSample";
  }

  @Override
  public Class<Arguments> schema() {
    return Arguments.class;
  }

  public String description() {
    return "Retrieves code samples by type and optional example. If example is not provided, returns all samples for the given type.";
  }

  public McpSchema.CallToolResult run(
      McpSyncServerExchange ignored, McpSchema.CallToolResult.Builder result, Arguments arguments)
      throws ToolException {

    String type = arguments.type();
    String example = arguments.example();

    try {
      if (example != null && !example.trim().isEmpty()) {
        // Get specific example
        Optional<String> sample = codeSampleService.getSample(type, example.trim());
        if (sample.isPresent()) {
          result.addTextContent("## Code Sample: " + type + "/" + example);
          result.addTextContent("```" + getLanguageHint(type));
          result.addTextContent(sample.get());
          result.addTextContent("```");
        } else {
          throw new ToolFailureException("Sample not found: " + type + "/" + example);
        }
      } else {
        // Get all samples for type
        Map<String, String> samples = codeSampleService.getAllSamplesForType(type);
        if (samples.isEmpty()) {
          throw new ToolFailureException("No samples found for type: " + type);
        }

        result.addTextContent("## All Code Samples for Type: " + type);
        result.addTextContent("Found " + samples.size() + " sample(s):");

        for (Map.Entry<String, String> entry : samples.entrySet()) {
          result.addTextContent("");
          result.addTextContent("### " + entry.getKey());
          result.addTextContent("```" + getLanguageHint(type));
          result.addTextContent(entry.getValue());
          result.addTextContent("```");
        }
      }

      return result.build();
    } catch (IOException e) {
      throw new ToolFailureException("Failed to load code samples: " + e.getMessage());
    }
  }

  /** Get syntax highlighting hint based on sample type. */
  private String getLanguageHint(String type) {
    return switch (type.toLowerCase()) {
      case "java" -> "java";
      case "bash" -> "bash";
      case "docker" -> "dockerfile";
      case "yaml", "yml" -> "yaml";
      case "json" -> "json";
      case "xml" -> "xml";
      case "python" -> "python";
      case "javascript", "js" -> "javascript";
      case "typescript", "ts" -> "typescript";
      default -> "";
    };
  }

  public record Arguments(@NotNull String type, String example) {}
}
