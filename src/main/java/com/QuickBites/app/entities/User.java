
package com.QuickBites.app.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



@Entity
@Table(name="_user")
public class User {
	
	public User() {}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(updatable=false)
	private int id;
	
	@NotNull
	@NotBlank
	private String firstName;
	private String lastName;
	
	@Column(unique=true,nullable=false)
	private String userName;
	private String password;
	
	@Column(unique=true)
	@NotNull
	@NotBlank
	private String email;
	
	@Column(unique=true, nullable=true)
	private String phone;
	
	@Column(nullable=true)
	private String address;
	
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="user_roles",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="role_id")
			)
	private Set<UserRole> roles = new HashSet<>();
	
	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String address) {
		this.address  = address;
	}
	
	public Set<UserRole> getRoles(){
		return this.roles;
	}
	
	public void setRoles(Set<UserRole> role) {
		this.roles = role;
	}
	
	
	
}
