package is.hi.hbvproject.utils;

import java.util.ArrayList;
import java.util.List;

import org.geolatte.geom.G2D;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.geojson.Polygon;

import is.hi.hbvproject.utils.WololoGeolatteConverter;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

public class ORSUtils{	
	public static List<org.geolatte.geom.Polygon<G2D>> getIsochrones(List<double[]> locations, double[] range, String apiKey) {
		JSONObject isochroneJSON = new JSONObject();
		isochroneJSON.put("locations", locations);
		isochroneJSON.put("range", range);
				
		JSONObject response = Unirest.post("https://api.openrouteservice.org/v2/isochrones/foot-walking")
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
}
