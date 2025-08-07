package net.qvarford.homeenvmcp.tools;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import io.modelcontextprotocol.spec.McpSchema;
import net.qvarford.homeenvmcp.tools.RunUnitTests.Arguments;
import net.qvarford.homeenvmcp.util.CommandExecutor;
import net.qvarford.homeenvmcp.util.ProcessResult;
import net.qvarford.homeenvmcp.util.ToolHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RunUnitTestsTest {

  @Mock private ToolHelper toolHelper;
  @Mock private CommandExecutor commandExecutor;

  private RunUnitTests runUnitTests;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    runUnitTests = new RunUnitTests(toolHelper, commandExecutor);
  }

  @Test
  void testName() {
    assertEquals("RunUnitTests", runUnitTests.name());
  }

  @Test
  void testSchema() {
    assertEquals(Arguments.class, runUnitTests.schema());
  }

  @Test
  void testDescription() {
    assertNotNull(runUnitTests.description());
    assertTrue(runUnitTests.description().contains("Maven"));
    assertTrue(runUnitTests.description().contains("JUnit5"));
  }

  @Test
  void testRunWithPackageName() throws Exception {
    Arguments arguments = new Arguments("com.example.test", null, null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runUnitTests.run(null, result, arguments);

    ArgumentCaptor<ToolHelper.ExceptionThrowingSupplier<ProcessResult>> captor =
        ArgumentCaptor.forClass(ToolHelper.ExceptionThrowingSupplier.class);

    verify(toolHelper).runCommand(eq("Maven Unit Tests"), captor.capture());

    ToolHelper.ExceptionThrowingSupplier<ProcessResult> supplier = captor.getValue();
    assertNotNull(supplier);
  }

  @Test
  void testRunWithClassName() throws Exception {
    Arguments arguments = new Arguments("com.example.test", "TestClass", null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runUnitTests.run(null, result, arguments);

    verify(toolHelper).runCommand(eq("Maven Unit Tests"), any());
  }

  @Test
  void testRunWithMethodName() throws Exception {
    Arguments arguments = new Arguments("com.example.test", "TestClass", "testMethod");
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runUnitTests.run(null, result, arguments);

    verify(toolHelper).runCommand(eq("Maven Unit Tests"), any());
  }

  @Test
  void testRunWithNoArguments() throws Exception {
    Arguments arguments = new Arguments(null, null, null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runUnitTests.run(null, result, arguments);

    ArgumentCaptor<ToolHelper.ExceptionThrowingSupplier<ProcessResult>> captor =
        ArgumentCaptor.forClass(ToolHelper.ExceptionThrowingSupplier.class);

    verify(toolHelper).runCommand(eq("Maven Unit Tests"), captor.capture());

    ToolHelper.ExceptionThrowingSupplier<ProcessResult> supplier = captor.getValue();
    assertNotNull(supplier);
  }

  @Test
  void testBuildTestFilterWithPackageOnly() throws Exception {
    Arguments arguments = new Arguments("com.example.test", null, null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runUnitTests.run(null, result, arguments);

    verify(toolHelper).runCommand(eq("Maven Unit Tests"), any());
  }

  @Test
  void testBuildTestFilterWithClassAndMethod() throws Exception {
    Arguments arguments = new Arguments("com.example.test", "TestClass", "testMethod");
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    runUnitTests.run(null, result, arguments);

    verify(toolHelper).runCommand(eq("Maven Unit Tests"), any());
  }

  @Test
  void testDefaultConstructor() {
    RunUnitTests defaultRunUnitTests = new RunUnitTests();
    assertEquals("RunUnitTests", defaultRunUnitTests.name());
  }
}
