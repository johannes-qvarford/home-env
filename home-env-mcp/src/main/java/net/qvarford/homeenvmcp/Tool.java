package net.qvarford.homeenvmcp;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import net.qvarford.homeenvmcp.exceptions.ToolException;

public interface Tool<T> {
  String description();

  default String name() {
    return schema().getName();
  }

  Class<T> schema();

  McpSchema.CallToolResult run(
      McpSyncServerExchange exchange, McpSchema.CallToolResult.Builder result, T request)
      throws ToolException;
}
