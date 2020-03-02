package is.hi.hbvproject.models.requestObjects.ors.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.wololo.geojson.Point;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Constraint(validatedBy = PointValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ValidPoint {
  String message() default "Must be a legal geojson point";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}

class PointValidator implements ConstraintValidator<ValidPoint, Point> {
  @Override
  public boolean isValid(Point point, ConstraintValidatorContext context) {
    if (point == null || !(point instanceof Point)) {
      return false;
    }

    if (point.getType().toLowerCase() == "point") {
      return false;
    }

    if (!(point.getCoordinates() instanceof double[])) {
      return false;
    }

    if (point.getCoordinates().length != 2) {
      return false;
    }

    return true;
  }
}