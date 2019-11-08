package is.hi.hbvproject.service.Implementation;

import java.util.ArrayList;
import java.util.List;

import org.geolatte.geom.G2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.geojson.Point;
import org.wololo.geojson.Polygon;

import is.hi.hbvproject.service.OrsService;
import is.hi.hbvproject.utils.WololoGeolatteConverter;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

@Service
public class OrsServiceImplementation implements OrsService {
  @Value("${ors_key}") 
  private String apiKey;

  @Value("${ors_baseurl}")
  private String baseUrl;

  @Autowired
  public OrsServiceImplementation() {}

	@Override
	public List<org.geolatte.geom.Polygon<G2D>> getIsochrones(JSONArray locations, JSONArray range) {
		JSONObject isochroneJSON = new JSONObject();
		isochroneJSON.put("locations", locations);
		isochroneJSON.put("range", range);


		JSONObject response = Unirest.post(baseUrl + "/v2/isochrones/foot-walking")
			      .header("Authorization", apiKey)
			      .header("Content-type", "application/json")
			      .header("mode", "no-cors")
			      .body(isochroneJSON).asJson().getBody().getObject();

		if (response.has("error")) {
			JSONObject e = response.getJSONObject("error");
			String m = e.getString("message");
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "OpenRouteService error: " + m); 
		}
		FeatureCollection featureCollection = 
			(FeatureCollection) GeoJSONFactory.create(response.toString());
		
		Feature[] features = featureCollection.getFeatures();
		
		Polygon aroundOrigin = (Polygon) features[0].getGeometry();
		
		Polygon aroundDestination = (Polygon) features[1].getGeometry();

		org.geolatte.geom.Polygon<G2D> geolatteAroundOrigin = 
			WololoGeolatteConverter.toGeolattePolygon(aroundOrigin);
		
		org.geolatte.geom.Polygon<G2D> geolatteAroundDestination = 
			WololoGeolatteConverter.toGeolattePolygon(aroundDestination);
		
		List<org.geolatte.geom.Polygon<G2D>> isochrones = new ArrayList<>();
		isochrones.add(0, geolatteAroundOrigin);
		isochrones.add(1, geolatteAroundDestination);
		
		return isochrones;
	}

	@Override
	public FeatureCollection getGeoCodes(String param) {
		String geocode = param.trim().replace(" ", "%20");
		String boundary = "boundary.country=is";
    String api = "api_key=" + apiKey;
    try {
    JsonNode response = Unirest.get(baseUrl + "/geocode/search?" + api + "&text=" + geocode + "&" + boundary)
    .accept("application/json")
    .asJson().getBody();
    
    FeatureCollection features = (FeatureCollection) GeoJSONFactory.create(response.toString());
    return features;
    } catch (Exception e) {
      throw e;
    }
	}
	

	@Override
	public Feature getDirections(Point origin, Point destination, String profile) {
		JSONArray coordinates = new JSONArray();
		JSONObject body = new JSONObject();

		double[] originCoordsArray = origin.getCoordinates();
		JSONArray originCoords = new JSONArray();
		
		// Need to do this because the geocode API(which will provide most of these coords)
		// is inconsistent with lat/long or long/lat format
		if (originCoordsArray[0] < 0) {
			originCoords.put(originCoordsArray[0]);
			originCoords.put(originCoordsArray[1]);
		} else {
			originCoords.put(originCoordsArray[1]);
			originCoords.put(originCoordsArray[0]);
		}

		double[] destinationCoordsArray = destination.getCoordinates();
		JSONArray destinationCoords = new JSONArray();
		
		if (originCoordsArray[0] < 0) {
			destinationCoords.put(destinationCoordsArray[0]);
			destinationCoords.put(destinationCoordsArray[1]);
		} else {
			destinationCoords.put(destinationCoordsArray[1]);
			destinationCoords.put(destinationCoordsArray[0]);
		}

		coordinates.put(originCoords);
		coordinates.put(destinationCoords);
		body.put("coordinates", coordinates);
		// Makes sure we don't get unnecessary info
		body.put("instructions", false);
		body.put("preference", "recommended");

		JsonNode response = Unirest.post
			(baseUrl + "/v2/directions/" + profile + "/geojson")
			.header("Authorization", apiKey)
			.header("Content-type", "application/json")
			.accept("application/json, application/geo+json")
			.body(body)
			.asJson()
			.getBody();

		FeatureCollection featureCollection = (FeatureCollection) GeoJSONFactory.create(response.toString());
		Feature feature = featureCollection.getFeatures()[0];

		return feature;
	}
}