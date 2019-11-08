package is.hi.hbvproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.geojson.Point;
import org.wololo.geojson.Feature;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import is.hi.hbvproject.service.OrsService;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

@RestController
@CrossOrigin
public class OrsController {
  OrsService orsService;
  
	@Autowired
	public OrsController(OrsService orsService) {
		this.orsService = orsService;
  }
  
  @RequestMapping(
    value = "/ors/geocode",
    method = RequestMethod.GET,
    produces = "application/json"
  )
  public String getGeoCodes(@RequestParam(value = "geocode") String geoCode) {
    return orsService.getGeoCodes(geoCode).toString();
  }

  @RequestMapping(
    value = "/ors/directions",
    method = RequestMethod.POST,
    produces = "application/json",
    consumes = "application/json"
  )
  public List<Feature> getDirections(@RequestBody String json) {
    JSONObject body = new JSONObject(json);
    JSONArray endpointsArray = body.getJSONArray("endpoints");
    List<Feature> paths = new ArrayList<>();
    for (int i = 0; i < endpointsArray.length(); i++) {
      
      String eString = endpointsArray.get(i).toString();
      JSONObject endpoints = new JSONObject(eString);

 
      String o = endpoints.get("origin").toString();
      Point origin = (Point) GeoJSONFactory.create(o);
      String d = endpoints.get("destination").toString();
      Point destination = (Point) GeoJSONFactory.create(d);
      String profile = endpoints.getString("profile");

      Feature path = orsService.getDirections(origin, destination, profile);
      paths.add(path);
    }

    return paths;
  }
}
