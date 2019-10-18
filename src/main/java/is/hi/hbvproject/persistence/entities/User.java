package is.hi.hbvproject.persistence.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="users") // "user" is a reserved word in postgres
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotNull
	@Column(unique = true)
	private String username;
	@NotNull
	private String password;
	
	@OneToMany(
			mappedBy = "driver",
			fetch = FetchType.LAZY
			)
	private Set<Ride> drives = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "passengers",
			joinColumns = @JoinColumn(name = "passenger_id"),
			inverseJoinColumns = @JoinColumn(name = "ride_id")
			)
	private Set<Ride> rides = new HashSet<>();
	
	public User() {}
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public long getId() {
		return id;
	}
	
	public Set<Ride> getDrives() {
		return drives;
	}
	
	public Set<Ride> getRides() {
		return rides;
	}
}
