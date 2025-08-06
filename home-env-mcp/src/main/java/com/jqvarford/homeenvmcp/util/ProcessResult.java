package com.jqvarford.homeenvmcp.util;

public record ProcessResult(int exitCode, String stdout, String stderr) {
  public boolean isSuccess() {
    return exitCode == 0;
  }
}
