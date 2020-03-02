package is.hi.hbvproject.models.requestObjects.ors.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = DirectionsPropertiesValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface HasProfile {
  String message() default "Must have a profile attribute with the value 'foot-walking' or 'driving-car'";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}

class DirectionsPropertiesValidator implements ConstraintValidator<HasProfile, JSONObject> {
  @Override
  public boolean isValid(JSONObject props, ConstraintValidatorContext context) {
    if (props == null || !(props instanceof JSONObject)) {
      return false;
    }

    try {
      String profile = props.getString("profile");
      if (profile.toLowerCase().equals("foot-walking") || profile.toLowerCase().equals("driving-car"))
      return true;
      else return false;
    } catch (JSONException e) {
      return false;
    }
  }
}