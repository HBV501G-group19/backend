package is.hi.hbvproject.models.requestObjects.ors;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.wololo.geojson.Point;

import kong.unirest.json.JSONObject;

public class DirectionsRequest {
  @JsonProperty("origin")
  private Point origin;

  @JsonProperty("destination")
  private Point destination;

  @JsonProperty("properties")
  private JSONObject properties;
  
  private DirectionsRequest() {}

  public DirectionsRequest(Point origin, Point destination) {
    this.origin = origin;
    this.destination = destination;
  }

  @JsonProperty("properties")
  public void unpackProperties(Map<String, Object> props) {
    JSONObject json = new JSONObject(props);
    this.properties = json;
  }

  public Point getOrigin() {
    return origin;
  }

  public Point getDestination() {
    return destination;
  }

  public JSONObject getProperties() {
    return properties;
  }
}
