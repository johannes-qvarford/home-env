package net.qvarford.homeenvmcp;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import net.qvarford.homeenvmcp.tools.GitAddAndCommit;
import net.qvarford.homeenvmcp.tools.RunIntegrationTests;
import net.qvarford.homeenvmcp.tools.RunUnitTests;

public class McpApplication {
  public static void main(String[] args) throws InterruptedException {
    McpSyncServer syncServer =
        McpServer.sync(new StdioServerTransportProvider())
            .serverInfo("home-env-mcp", "1.0.0")
            .capabilities(ServerCapabilities.builder().tools(true).logging().build())
            .build();
    ToolService toolService = new ToolService();

    syncServer.addTool(toolService.specification(new GitAddAndCommit()));
    syncServer.addTool(toolService.specification(new RunUnitTests()));
    syncServer.addTool(toolService.specification(new RunIntegrationTests()));

    while (true) {
      Thread.sleep(1_000);
    }
  }
}
