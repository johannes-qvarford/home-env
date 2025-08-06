package com.jqvarford.homeenvmcp.exceptions;

public class ToolException extends Exception {
  public ToolException(String message, Throwable cause) {
    super(message, cause);
  }

  public ToolException(String message) {
    super(message, null);
  }
}
