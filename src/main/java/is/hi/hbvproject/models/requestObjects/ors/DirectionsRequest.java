package is.hi.hbvproject.models.requestObjects.ors;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.wololo.geojson.Point;

import kong.unirest.json.JSONObject;

public class DirectionsRequest {
  @JsonProperty("origin")
  Point origin;
  @JsonProperty("destination")
  Point destination;
  @JsonProperty("properties")
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