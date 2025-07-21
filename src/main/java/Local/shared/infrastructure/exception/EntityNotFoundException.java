package Local.shared.infrastructure.exception;

public class EntityNotFoundException extends DomainException {

  public EntityNotFoundException(String entityName, Object id) {
      super(String.format("%s with id %s not found", entityName, id),
              "ENTITY_NOT_FOUND");
  }

}
