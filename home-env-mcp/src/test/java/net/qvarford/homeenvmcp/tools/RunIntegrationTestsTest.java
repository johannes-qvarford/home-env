package net.qvarford.homeenvmcp.tools;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import io.modelcontextprotocol.spec.McpSchema;
import net.qvarford.homeenvmcp.tools.RunIntegrationTests.Arguments;
import net.qvarford.homeenvmcp.util.CommandExecutor;
import net.qvarford.homeenvmcp.util.ToolHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RunIntegrationTestsTest {

  @Mock private ToolHelper toolHelper;
  @Mock private CommandExecutor commandExecutor;

  private RunIntegrationTests runIntegrationTests;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    runIntegrationTests = new RunIntegrationTests(toolHelper, commandExecutor);
  }

  @Test
  void testName() {
    assertEquals("RunIntegrationTests", runIntegrationTests.name());
  }

  @Test
  void testSchema() {
    assertEquals(Arguments.class, runIntegrationTests.schema());
  }

  @Test
  void testDescription() {
    assertNotNull(runIntegrationTests.description());
    assertTrue(runIntegrationTests.description().contains("Cucumber"));
  }

  @Test
  void testRunWithNoArguments() throws Exception {
    Arguments arguments = new Arguments(null, null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runIntegrationTests.run(null, result, arguments);

    verify(toolHelper).runCommand(eq("Cucumber Integration Tests"), any());
  }

  @Test
  void testRunWithFeatureFile() throws Exception {
    Arguments arguments = new Arguments("login", null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runIntegrationTests.run(null, result, arguments);

    verify(toolHelper).runCommand(eq("Cucumber Integration Tests"), any());
  }

  @Test
  void testRunWithFeatureFileAlreadyHasExtension() throws Exception {
    Arguments arguments = new Arguments("login.feature", null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runIntegrationTests.run(null, result, arguments);

    verify(toolHelper).runCommand(eq("Cucumber Integration Tests"), any());
  }

  @Test
  void testRunWithScenarioName() throws Exception {
    Arguments arguments = new Arguments(null, "User login with valid credentials");
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runIntegrationTests.run(null, result, arguments);

    verify(toolHelper).runCommand(eq("Cucumber Integration Tests"), any());
  }

  @Test
  void testRunWithBothFeatureAndScenario() throws Exception {
    Arguments arguments = new Arguments("login", "User login with valid credentials");
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runIntegrationTests.run(null, result, arguments);

    verify(toolHelper).runCommand(eq("Cucumber Integration Tests"), any());
  }

  @Test
  void testRunWithEmptyFeatureFile() throws Exception {
    Arguments arguments = new Arguments("", "scenario");
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runIntegrationTests.run(null, result, arguments);

    verify(toolHelper).runCommand(eq("Cucumber Integration Tests"), any());
  }

  @Test
  void testRunWithWhitespaceFeatureFile() throws Exception {
    Arguments arguments = new Arguments("   ", "scenario");
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runIntegrationTests.run(null, result, arguments);

    verify(toolHelper).runCommand(eq("Cucumber Integration Tests"), any());
  }

  @Test
  void testDefaultConstructor() {
    RunIntegrationTests defaultRunIntegrationTests = new RunIntegrationTests();
    assertEquals("RunIntegrationTests", defaultRunIntegrationTests.name());
  }

  @Test
  void testToolHelperIsCalled() throws Exception {
    Arguments arguments = new Arguments(null, null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runIntegrationTests.run(null, result, arguments);

    verify(toolHelper).runCommand(eq("Cucumber Integration Tests"), any());
  }
}
