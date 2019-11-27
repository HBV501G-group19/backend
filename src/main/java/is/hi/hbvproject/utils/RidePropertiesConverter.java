package is.hi.hbvproject.utils;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RidePropertiesConverter implements AttributeConverter<Map<String, Object>, String> {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Map<String, Object> rideProperties) {

      String ridePropertiesJson = null;
      try {
          ridePropertiesJson = objectMapper.writeValueAsString(rideProperties);
      } catch (final JsonProcessingException e) {
          throw new Error("JSON writing error", e);
      }

      return ridePropertiesJson;
  }

  @Override
  public Map<String, Object> convertToEntityAttribute(String ridePropertiesJSON) {

      Map<String, Object> rideProperties = null;
      try {
          rideProperties= objectMapper.readValue(ridePropertiesJSON, Map.class);
      } catch (final IOException e) {
          throw new Error("JSON reading error", e);
      }

      return rideProperties;
  }
}