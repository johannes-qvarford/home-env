package net.qvarford.homeenvmcp.util;

import net.qvarford.homeenvmcp.exceptions.ToolCallException;
import net.qvarford.homeenvmcp.exceptions.ToolException;
import net.qvarford.homeenvmcp.exceptions.ToolFailureException;

public class ToolHelper {

  public void runCommand(String name, ExceptionThrowingSupplier<ProcessResult> supplier)
      throws ToolException {
    ProcessResult result;
    try {
      result = supplier.get();
    } catch (Exception e) {
      throw new ToolFailureException("Failed to run command '%s'".formatted(name), e);
    }

    if (!result.isSuccess()) {
      throw new ToolCallException("Call to command '%s' failed\nResult:%s".formatted(name, result));
    }
  }

  public interface ExceptionThrowingSupplier<T> {
    T get() throws Exception;
  }
}
