package is.hi.hbvproject.service;

import java.util.List;
import java.util.Optional;

import org.geolatte.geom.G2D;

import is.hi.hbvproject.persistence.entities.Ride;
import is.hi.hbvproject.persistence.entities.User;

public interface RideService {
	List<Ride> findNearby(org.geolatte.geom.Point<G2D> origin, org.geolatte.geom.Point<G2D> destination);
	void addPassenger(long rideId, User passenger);
	void delete(Ride ride);
	void deleteAll();
	long count();
	void deleteById(Long id);
	boolean existsById(Long id);
	List<Ride> findAll();
	List<Ride> findAllById(Iterable<Long> ids);
	Optional<Ride> findById(Long id);
	Ride save(Ride ride);
	void deleteAll(Iterable<? extends Ride> rides);
}
