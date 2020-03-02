package is.hi.hbvproject.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wololo.geojson.Point;
import org.wololo.jts2geojson.GeoJSONWriter;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import is.hi.hbvproject.models.requestObjects.ors.DirectionsRequest;
import is.hi.hbvproject.models.requestObjects.ors.GeocodeRequest;
import is.hi.hbvproject.service.OrsService;
import is.hi.hbvproject.service.RideService;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

@RestController
@Validated
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
  public FeatureCollection getGeocodes(@RequestBody @Valid GeocodeRequest body) {
    return orsService.getGeoCodes(body.getGeocode(), body.getFocus());
  }

  @RequestMapping(
    value = "/ors/geoname",
    method = RequestMethod.POST,
    produces = "application/json",
    consumes = "application/json"
  )
  public Feature getGeoname(@RequestBody String json) {
    // TODO: better define properties object
    JSONObject body = new JSONObject(json);
    JSONArray coordinatesJson = body.getJSONArray("coordinates");
    JSONObject props = body.optJSONObject("properties");

    double[] coordinates = {
      coordinatesJson.getDouble(0), coordinatesJson.getDouble(1)
    };

    Point coordinatesPoint = new Point(coordinates);
    Feature feature = orsService.getGeoNames(coordinatesPoint, props);

    if (feature == null) {
      return null;
    }
    return feature;
  }

  @RequestMapping(
    value = "/ors/directions",
    method = RequestMethod.POST,
    produces = "application/json",
    consumes = "application/json"
  )
  public List<FeatureCollection> getDirections(@RequestBody List<@Valid DirectionsRequest> queries) {
    List<FeatureCollection> results = new ArrayList<>();
    for (DirectionsRequest d : queries) {
      List<Feature> directions = new ArrayList<>();
      directions.addAll(makeDirectionFeatures(d.getOrigin(), d.getDestination(), d.getProperties()));

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
