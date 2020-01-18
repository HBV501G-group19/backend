package is.hi.hbvproject.models.requestObjects.ors;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.wololo.geojson.Point;

public class GeocodeRequest {
  @JsonProperty("focus")
  @NotEmpty
  private Point focus;
  
  @JsonProperty("geocode")
  @NotEmpty
  private String geocode;

  private GeocodeRequest() {}

  public GeocodeRequest(Point focus, String geocode) {
    this.focus = focus;
    this.geocode = geocode;
  }

  public Point getFocus() {
    return focus;
  }

  public String getGeocode() {
    return geocode;
  }
}