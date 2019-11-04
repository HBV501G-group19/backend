package is.hi.hbvproject.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import is.hi.hbvproject.persistence.entities.Ride;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.geolatte.geom.G2D;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long>{
	@Query(value = "select r from Ride r where within(r.origin, :origin) = true "
			+ "and within(r.destination, :destination) = true "
			+ "and r.departureTime > :min_time and r.departureTime < :max_time")
	List<Ride> findNearby(
		@Param("origin") org.geolatte.geom.Polygon<G2D> origin, 
		@Param("destination") org.geolatte.geom.Polygon<G2D> destination,
		@Param("min_time") Timestamp minTime,
		@Param("max_time") Timestamp maxTime
	);
	public void	delete(Ride ride);
	public void deleteAll();
	public long count();
	public void	deleteById(Long id);
	public boolean existsById(Long id);
	public List<Ride> findAll();
	public List<Ride> findAllById(Iterable<Long> ids);
	public Optional<Ride> findById(Long id);
	public Ride save(Ride ride);
	public void deleteAll(Iterable<? extends Ride> rides);
}
