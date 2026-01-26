package my.authservice.domain.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class NotFoundException extends AbstractThrowableProblem {

  public NotFoundException(String entityName, Long entityId) {

    super(
        null,
        "Not found",
        Status.NOT_FOUND,
        String.format(entityName + " with id %s not found", entityId));
  }

  public NotFoundException(String entityName, String field, String value) {

    super(
        null,
        "Not found",
        Status.NOT_FOUND,
        String.format(entityName + " with field %s: %s not found", field, value));
  }
}
