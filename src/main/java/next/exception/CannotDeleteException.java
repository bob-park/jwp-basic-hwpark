package next.exception;

public class CannotDeleteException extends RuntimeException {

  public CannotDeleteException(String message) {
    super(message);
  }
}
