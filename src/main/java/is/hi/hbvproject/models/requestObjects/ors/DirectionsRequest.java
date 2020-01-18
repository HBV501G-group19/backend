package is.hi.hbvproject.models.requestObjects.ors;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.wololo.geojson.Point;

import kong.unirest.json.JSONObject;

public class DirectionsRequest {
  @JsonProperty("origin")
  @NotEmpty
  Point origin;

  @JsonProperty("destination")
  @NotEmpty
  Point destination;

  @JsonProperty("properties")
  @NotEmpty
  String properties;
  
  private DirectionsRequest() {}

  public DirectionsRequest(Point origin, Point destination, String properties) {
    this.origin = origin;
    this.destination = destination;
    this.properties = properties;
  }

  public Point getOrigin() {
    return origin;
  }

  public Point getDestination() {
    return destination;
  }

  public JSONObject getProperties() {
    JSONObject json = new JSONObject(properties);
    return json;
  }
}