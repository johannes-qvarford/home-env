package net.qvarford.homeenvmcp.util;

import java.util.HashMap;
import java.util.Map;

public class MockEnvironmentProvider implements EnvironmentProvider {
  private final Map<String, String> environmentVariables = new HashMap<>();

  public void setEnvironmentVariable(String name, String value) {
    environmentVariables.put(name, value);
  }

  public void clearEnvironmentVariables() {
    environmentVariables.clear();
  }

  @Override
  public String getEnvironmentVariable(String name) {
    return environmentVariables.get(name);
  }
}
