package net.qvarford.homeenvmcp.exceptions;

public class ToolFailureException extends ToolException {

  public ToolFailureException(String message, Throwable cause) {
    super(message, cause);
  }

  public ToolFailureException(String message) {
    super(message, null);
  }
}
