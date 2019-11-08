package is.hi.hbvproject.service;

import java.util.List;

import org.geolatte.geom.G2D;
import org.wololo.geojson.FeatureCollection;

import kong.unirest.json.JSONArray;

public interface OrsService {
  List<org.geolatte.geom.Polygon<G2D>> getIsochrones(JSONArray locations, JSONArray range);
  FeatureCollection getGeoCodes(String geocode);
}