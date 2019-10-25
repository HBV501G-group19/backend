package is.hi.hbvproject.utils;

import java.util.ArrayList;
import java.util.List;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.PositionSequenceBuilder;
import org.geolatte.geom.PositionSequenceBuilders;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.wololo.geojson.LineString;
import org.wololo.geojson.Point;

public class WololoGeolatteConverter {
	public static LineString toWololoLineString(org.geolatte.geom.LineString<G2D> lineString) {
		List<double[]> coords = new ArrayList<>();
		double[][] coordArray = new double[lineString.getNumPositions()][lineString.getCoordinateDimension()];
		lineString.getPositions().forEach(position -> {
			double[] coord = new double[2];
			position.toArray(coord);
			coords.add(coord);
		});
		
		coordArray = coords.toArray(coordArray);
		return new LineString(coordArray);
	}
	
	public static org.geolatte.geom.LineString<G2D> toGeolatteLineString(LineString lineString) {
		double[][] coords = lineString.getCoordinates();
		PositionSequenceBuilder<G2D> x = 
			PositionSequenceBuilders.fixedSized(
				coords.length,
				org.geolatte.geom.G2D.class
			);
		
		for(double[] coord : coords) {
			x.add(coord[0], coord[1]);
		}
		org.geolatte.geom.LineString<G2D> geolatteLineString = 
			new org.geolatte.geom.LineString<G2D>(
				x.toPositionSequence(),
				CoordinateReferenceSystems.WGS84
			);
		return geolatteLineString;
	}
	
	public static Point toWololoPoint(org.geolatte.geom.Point<G2D> point) {
		double[] coords = new double[2];
		point.getPositions().getCoordinates(0, coords);
		return new Point(coords);
	}
	
	public static org.geolatte.geom.Point<G2D> toGeolattePoint(Point point) {
		double[] coords = point.getCoordinates();
		org.geolatte.geom.Point<G2D> geolattePoint = Geometries.mkPoint(new G2D(coords[0], coords[1]), CoordinateReferenceSystems.WGS84);
		return geolattePoint;
	}
}
