package net.qvarford.homeenvmcp.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodeSampleServiceTest {

  private CodeSampleService codeSampleService;

  @BeforeEach
  void setUp() {
    codeSampleService = new CodeSampleService();
  }

  @Test
  void testGetAllSampleTypes() throws IOException {
    Map<String, List<String>> allTypes = codeSampleService.getAllSampleTypes();

    assertNotNull(allTypes);
    assertTrue(allTypes.containsKey("java"));
    assertTrue(allTypes.containsKey("bash"));
    assertTrue(allTypes.containsKey("docker"));

    List<String> javaExamples = allTypes.get("java");
    assertTrue(javaExamples.contains("hello-world"));
    assertTrue(javaExamples.contains("rest-client"));
    assertTrue(javaExamples.contains("unit-test"));

    List<String> bashExamples = allTypes.get("bash");
    assertTrue(bashExamples.contains("file-operations"));
    assertTrue(bashExamples.contains("git-workflow"));

    List<String> dockerExamples = allTypes.get("docker");
    assertTrue(dockerExamples.contains("multi-stage"));
    assertTrue(dockerExamples.contains("compose"));
  }

  @Test
  void testGetSpecificSample() throws IOException {
    Optional<String> sample = codeSampleService.getSample("java", "hello-world");

    assertTrue(sample.isPresent());
    String content = sample.get();
    assertTrue(content.contains("public class HelloWorld"));
    assertTrue(content.contains("Hello, World!"));
  }

  @Test
  void testGetNonExistentSample() throws IOException {
    Optional<String> sample = codeSampleService.getSample("java", "non-existent");

    assertFalse(sample.isPresent());
  }

  @Test
  void testGetNonExistentType() throws IOException {
    Optional<String> sample = codeSampleService.getSample("non-existent-type", "example");

    assertFalse(sample.isPresent());
  }

  @Test
  void testGetAllSamplesForType() throws IOException {
    Map<String, String> javaSamples = codeSampleService.getAllSamplesForType("java");

    assertNotNull(javaSamples);
    assertEquals(3, javaSamples.size());
    assertTrue(javaSamples.containsKey("hello-world"));
    assertTrue(javaSamples.containsKey("rest-client"));
    assertTrue(javaSamples.containsKey("unit-test"));

    String helloWorldContent = javaSamples.get("hello-world");
    assertTrue(helloWorldContent.contains("public class HelloWorld"));
  }

  @Test
  void testGetAllSamplesForNonExistentType() throws IOException {
    Map<String, String> samples = codeSampleService.getAllSamplesForType("non-existent");

    assertNotNull(samples);
    assertTrue(samples.isEmpty());
  }

  @Test
  void testBashSampleContent() throws IOException {
    Optional<String> sample = codeSampleService.getSample("bash", "file-operations");

    assertTrue(sample.isPresent());
    String content = sample.get();
    assertTrue(content.contains("#!/bin/bash"));
    assertTrue(content.contains("create_dir_if_not_exists"));
    assertTrue(content.contains("backup_file"));
  }

  @Test
  void testDockerSampleContent() throws IOException {
    Optional<String> sample = codeSampleService.getSample("docker", "multi-stage");

    assertTrue(sample.isPresent());
    String content = sample.get();
    assertTrue(content.contains("FROM maven:3.9-eclipse-temurin-17 AS build"));
    assertTrue(content.contains("WORKDIR /app"));
  }

  @Test
  void testServiceIsCachingResults() throws IOException {
    // First call should initialize cache
    Map<String, List<String>> firstCall = codeSampleService.getAllSampleTypes();

    // Second call should use cached data
    Map<String, List<String>> secondCall = codeSampleService.getAllSampleTypes();

    // Results should be identical
    assertEquals(firstCall, secondCall);

    // Test individual sample caching
    Optional<String> firstSample = codeSampleService.getSample("java", "hello-world");
    Optional<String> secondSample = codeSampleService.getSample("java", "hello-world");

    assertEquals(firstSample, secondSample);
  }
}
