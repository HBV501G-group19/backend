package is.hi.hbvproject.service;

import java.util.List;

import org.geolatte.geom.G2D;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.Point;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public interface OrsService {
  List<org.geolatte.geom.Polygon<G2D>> getIsochrones(JSONArray locations, JSONArray range);
  FeatureCollection getGeoCodes(String geocode, Point focus);
  Feature getGeoNames(Point coordinates, JSONObject properties);
  Feature getDirections(Point origin, Point destination, String profile, JSONObject properties);
}