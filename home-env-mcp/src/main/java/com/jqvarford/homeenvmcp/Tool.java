package com.jqvarford.homeenvmcp;

import com.jqvarford.homeenvmcp.exceptions.ToolException;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

public interface Tool<T> {
  String description();

  default String name() {
    return schema().getName();
  }

  Class<T> schema();

  McpSchema.CallToolResult run(McpSyncServerExchange exchange, T request) throws ToolException;
}
