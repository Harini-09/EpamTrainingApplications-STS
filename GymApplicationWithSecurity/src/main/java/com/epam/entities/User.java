package com.epam.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User implements UserDetails{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(nullable=false)
	private String firstName;
	@Column(nullable=false)
	private String lastName;
	@Column(nullable=false,unique=true)
	private String userName;
	@Column(nullable=false)
	private String password;
	@Column(nullable=false)
	private boolean isActive;
	@Column(nullable=false)
	private String email;
	@Column(nullable=false)
	private LocalDate createDate = LocalDate.now();
	
	
	
	@OneToOne(mappedBy="user",cascade=CascadeType.ALL)
	Trainee trainee;
	
	@OneToOne(mappedBy="user",cascade=CascadeType.ALL)
	Trainer trainer;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("USER"));

        return authorities;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userName;
	}
	
	
	public String getUserName() {
		// TODO Auto-generated method stub
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public User(int id, String firstName, String lastName, String userName, String password, boolean isActive,
			String email, LocalDate createDate) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.isActive = isActive;
		this.email = email;
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", userName=" + userName
				+ ", password=" + password + ", isActive=" + isActive + ", email=" + email + ", createDate="
				+ createDate + "]";
	}

	
}
