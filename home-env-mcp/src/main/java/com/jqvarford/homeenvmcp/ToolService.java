package com.jqvarford.homeenvmcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;

public class ToolService {
  private final SchemaGenerator generator;
  private final ObjectMapper objectMapper;

  public ToolService() {
    objectMapper = new ObjectMapper();
    SchemaGeneratorConfigBuilder configBuilder =
        new SchemaGeneratorConfigBuilder(
            objectMapper, SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON);

    JacksonModule jacksonModule = new JacksonModule();
    configBuilder.with(jacksonModule);

    JakartaValidationModule jakartaValidationModule = new JakartaValidationModule();
    configBuilder.with(jakartaValidationModule);

    SchemaGeneratorConfig schemaGeneratorConfig = configBuilder.build();

    generator = new SchemaGenerator(schemaGeneratorConfig);
  }

  public <T, U extends Tool<T>> SyncToolSpecification specification(U tool) {
    return new SyncToolSpecification(
        new McpSchema.Tool(
            tool.name(),
            tool.description(),
            generator.generateSchema(tool.schema()).toPrettyString()),
        (exchange, call) -> {
          McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();
          try {
            return tool.run(exchange, result, objectMapper.convertValue(call, tool.schema()));
          } catch (Exception e) {
            result.isError(true);
            result.addTextContent(e.toString());
            return result.build();
          }
        });
  }
}
