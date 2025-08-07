package net.qvarford.homeenvmcp.tools;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.qvarford.homeenvmcp.exceptions.ToolFailureException;
import net.qvarford.homeenvmcp.tools.GetCodeSample.Arguments;
import net.qvarford.homeenvmcp.util.CodeSampleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GetCodeSampleTest {

  @Mock private CodeSampleService codeSampleService;

  private GetCodeSample getCodeSample;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    getCodeSample = new GetCodeSample(codeSampleService);
  }

  @Test
  void testName() {
    assertEquals("GetCodeSample", getCodeSample.name());
  }

  @Test
  void testSchema() {
    assertEquals(Arguments.class, getCodeSample.schema());
  }

  @Test
  void testDescription() {
    assertNotNull(getCodeSample.description());
    assertTrue(getCodeSample.description().contains("Retrieves code samples"));
  }

  @Test
  void testGetSpecificSample() throws Exception {
    Arguments arguments = new Arguments("java", "hello-world");
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    String sampleContent = "public class HelloWorld {\n    // code\n}";
    when(codeSampleService.getSample("java", "hello-world")).thenReturn(Optional.of(sampleContent));

    McpSchema.CallToolResult callResult = getCodeSample.run(null, result, arguments);

    verify(codeSampleService).getSample("java", "hello-world");
    assertNotNull(callResult);

    // Verify result is not null and operation completed successfully
    assertNotNull(callResult);
  }

  @Test
  void testGetSpecificSampleNotFound() throws Exception {
    Arguments arguments = new Arguments("java", "non-existent");
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    when(codeSampleService.getSample("java", "non-existent")).thenReturn(Optional.empty());

    assertThrows(
        ToolFailureException.class,
        () -> {
          getCodeSample.run(null, result, arguments);
        });

    verify(codeSampleService).getSample("java", "non-existent");
  }

  @Test
  void testGetAllSamplesForType() throws Exception {
    Arguments arguments = new Arguments("java", null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    Map<String, String> samples = new HashMap<>();
    samples.put("hello-world", "public class HelloWorld { }");
    samples.put("rest-client", "public class RestClient { }");

    when(codeSampleService.getAllSamplesForType("java")).thenReturn(samples);

    McpSchema.CallToolResult callResult = getCodeSample.run(null, result, arguments);

    verify(codeSampleService).getAllSamplesForType("java");
    assertNotNull(callResult);

    // Verify result is not null and operation completed successfully
    assertNotNull(callResult);
  }

  @Test
  void testGetAllSamplesForTypeWithEmptyExample() throws Exception {
    Arguments arguments = new Arguments("java", "");
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    Map<String, String> samples = new HashMap<>();
    samples.put("hello-world", "public class HelloWorld { }");

    when(codeSampleService.getAllSamplesForType("java")).thenReturn(samples);

    getCodeSample.run(null, result, arguments);

    verify(codeSampleService).getAllSamplesForType("java");
  }

  @Test
  void testGetAllSamplesForTypeNotFound() throws Exception {
    Arguments arguments = new Arguments("non-existent", null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    when(codeSampleService.getAllSamplesForType("non-existent")).thenReturn(new HashMap<>());

    assertThrows(
        ToolFailureException.class,
        () -> {
          getCodeSample.run(null, result, arguments);
        });

    verify(codeSampleService).getAllSamplesForType("non-existent");
  }

  @Test
  void testIOExceptionHandling() throws Exception {
    Arguments arguments = new Arguments("java", "hello-world");
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    when(codeSampleService.getSample(anyString(), anyString()))
        .thenThrow(new IOException("Test IO error"));

    ToolFailureException exception =
        assertThrows(
            ToolFailureException.class,
            () -> {
              getCodeSample.run(null, result, arguments);
            });

    assertTrue(exception.getMessage().contains("Failed to load code samples"));
    assertTrue(exception.getMessage().contains("Test IO error"));
  }

  @Test
  void testDefaultConstructor() {
    GetCodeSample defaultGetCodeSample = new GetCodeSample();
    assertEquals("GetCodeSample", defaultGetCodeSample.name());
  }

  @Test
  void testDifferentLanguageHints() throws Exception {
    // Test bash language hint
    Arguments bashArguments = new Arguments("bash", "test-script");
    McpSchema.CallToolResult.Builder bashResult = McpSchema.CallToolResult.builder();

    when(codeSampleService.getSample("bash", "test-script"))
        .thenReturn(Optional.of("#!/bin/bash\necho test"));

    McpSchema.CallToolResult bashCallResult = getCodeSample.run(null, bashResult, bashArguments);

    assertNotNull(bashCallResult);

    // Test docker language hint
    Arguments dockerArguments = new Arguments("docker", "dockerfile");
    McpSchema.CallToolResult.Builder dockerResult = McpSchema.CallToolResult.builder();

    when(codeSampleService.getSample("docker", "dockerfile"))
        .thenReturn(Optional.of("FROM ubuntu:20.04"));

    McpSchema.CallToolResult dockerCallResult =
        getCodeSample.run(null, dockerResult, dockerArguments);

    assertNotNull(dockerCallResult);
  }
}
