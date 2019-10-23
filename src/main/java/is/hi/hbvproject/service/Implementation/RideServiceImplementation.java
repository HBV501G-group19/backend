package is.hi.hbvproject.service.Implementation;
import is.hi.hbvproject.service.RideService;

import java.util.List;
import java.util.Optional;

import org.geolatte.geom.G2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import is.hi.hbvproject.persistence.entities.Ride;
import is.hi.hbvproject.persistence.entities.User;
import is.hi.hbvproject.persistence.repositories.RideRepository;
import is.hi.hbvproject.persistence.repositories.UserRepository;

@Service
public class RideServiceImplementation implements RideService {
	
	RideRepository rideRepository;
	UserRepository userRepository;
	
	@Autowired
	public RideServiceImplementation(RideRepository rideRepository, UserRepository userRepository) {
		this.rideRepository = rideRepository;
		this.userRepository = userRepository;
	}
	
	@Override
	public List<Ride> findNearby(org.geolatte.geom.Point<G2D> origin, org.geolatte.geom.Point<G2D> destination) {
		return rideRepository.findNearby(origin, destination);
	};
	
	@Override
	public void addPassenger(long rideId, User passenger) {
		Ride ride = rideRepository.findById(rideId).get();
		ride.addPassenger(passenger);
	};
	
	@Override
	public void	delete(Ride ride) {
		rideRepository.delete(ride);
	};
	
	@Override
	public void deleteAll() {
		rideRepository.deleteAll();
	};
	
	@Override
	public long count() {
		return rideRepository.count();
	};
	
	@Override
	public void	deleteById(Long id) {
		rideRepository.deleteById(id);
	};
	
	@Override
	public boolean existsById(Long id) {
		return rideRepository.existsById(id);
	};
	
	@Override
	public List<Ride> findAll() {
		return rideRepository.findAll();
	};
	
	@Override
	public List<Ride> findAllById(Iterable<Long> ids) {
		return rideRepository.findAllById(ids);
	};
	
	@Override
	public Optional<Ride> findById(Long id) {
		return rideRepository.findById(id);
	};
	
	@Override
	public Ride save(Ride ride) {
		return rideRepository.save(ride);
	};
	
	@Override
	public void deleteAll(Iterable<? extends Ride> rides) {
		rideRepository.deleteAll(rides);
	};
}
