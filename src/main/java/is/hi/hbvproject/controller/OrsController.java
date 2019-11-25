package is.hi.hbvproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.geojson.Point;
import org.wololo.jts2geojson.GeoJSONWriter;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import is.hi.hbvproject.service.OrsService;
import is.hi.hbvproject.service.RideService;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

@RestController
@CrossOrigin
public class OrsController {
  OrsService orsService;
  RideService rideService;
  
	@Autowired
	public OrsController(OrsService orsService, RideService rideService) {
    this.orsService = orsService;
    this.rideService = rideService;
  }
  
  @RequestMapping(
    value = "/ors/geocode",
    method = RequestMethod.POST,
    produces = "application/json",
    consumes = "application/json"
  )
  public FeatureCollection getGeocodes(@RequestBody String json) {
    JSONObject body = new JSONObject(json);
    JSONArray focus = body.getJSONArray("focus");

    double[] focusCoords = {
      focus.getDouble(0), focus.getDouble(1)
    };

    Point focusPoint = new Point(focusCoords);
    String geocode = body.getString("geocode");
    return orsService.getGeoCodes(geocode, focusPoint);
  }

  @RequestMapping(
    value = "/ors/geoname",
    method = RequestMethod.POST,
    produces = "application/json",
    consumes = "application/json"
  )
  public Feature getGeoname(@RequestBody String json) {
    JSONObject body = new JSONObject(json);
    JSONArray coordinatesJson = body.getJSONArray("coordinates");
    JSONObject props = body.optJSONObject("properties");

    double[] coordinates = {
      coordinatesJson.getDouble(0), coordinatesJson.getDouble(1)
    };

    Point coordinatesPoint = new Point(coordinates);
    return orsService.getGeoNames(coordinatesPoint, props);
  }

  @RequestMapping(
    value = "/ors/directions",
    method = RequestMethod.POST,
    produces = "application/json",
    consumes = "application/json"
  )
  public List<FeatureCollection> getDirections(@RequestBody String json) {
    JSONArray body = new JSONArray(json);

    List<FeatureCollection> results = new ArrayList<>();
    for(int i=0; i < body.length(); i++) {
      String endpointsString = body.get(i).toString();
      JSONObject endpointsJson = new JSONObject(endpointsString);

      JSONObject o = endpointsJson.getJSONObject("origin");
      Point origin = (Point) GeoJSONFactory.create(o.toString());

      JSONObject d = endpointsJson.getJSONObject("destination");
      Point destination = (Point) GeoJSONFactory.create(d.toString());

      JSONObject properties = endpointsJson.getJSONObject("properties");


      List<Feature> directions = new ArrayList<>();
      directions.addAll(makeDirectionFeatures(origin, destination, properties));

      FeatureCollection direcionsCollection = new GeoJSONWriter().write(directions);
      results.add(direcionsCollection);
    }
    return results;
  }

  private List<Feature> makeDirectionFeatures(Point start, Point end, JSONObject properties) {
    Feature startNames = orsService.getGeoNames(start, properties);
    Feature endNames = orsService.getGeoNames(end, properties);

    String profile = properties.getString("profile");
    Feature directions = orsService.getDirections(start, end, profile, properties);

    List<Feature> res = new ArrayList<>();
    res.add(startNames);
    res.add(endNames);
    res.add(directions);
    return res;
  }
}
