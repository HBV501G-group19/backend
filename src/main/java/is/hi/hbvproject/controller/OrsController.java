package is.hi.hbvproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;


import is.hi.hbvproject.service.OrsService;

@RestController
@CrossOrigin
public class OrsController {
  OrsService orsService;
  
	@Autowired
	public OrsController(OrsService orsService) {
		this.orsService = orsService;
  }
  
  @RequestMapping(
    value = "/ors",
    method = RequestMethod.GET,
    produces = "application/json"
  )
  public String getGeoCodes(@RequestParam(value = "geocode") String geoCode) {
    return orsService.getGeoCodes(geoCode).toString();
  }
}
