
package com.QuickBites.app.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="pending_user")
public class PendingUser {

public PendingUser() {}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(updatable=false)
	private int id;

	private String firstName;
	private String lastName;
	
	private String userName;
	private String password;
	
	private String email;

	private String phone;
	
	private String address;
	
	private String citizenshipPhotoFront;
	
	private String citizenshipPhotoBack;
	
	private String driverLicense;
	
	@Enumerated(EnumType.STRING)
	private RoleName roleName;
	
	
	@CreationTimestamp
	private LocalDateTime createdAt;

	private boolean isOtpVerified;
	
	private boolean isAdminApproved;
	
	@PrePersist
	public void prePersist() {
		this.createdAt= LocalDateTime.now();
		this.isOtpVerified = false;
		this.setAdminApproved(false);
		
	}
	
	public String getCitizenshipPhotoFront() {
		return citizenshipPhotoFront;
	}

	public void setCitizenshipPhotoFront(String citizenshipPhotoFront) {
		this.citizenshipPhotoFront = citizenshipPhotoFront;
	}
	public String getCitizenshipPhotoBack() {
		return citizenshipPhotoBack;
	}

	public void setCitizenshipPhotoBack(String citizenshipPhotoBack) {
		this.citizenshipPhotoBack = citizenshipPhotoBack;
	}

	public String getDriverLicense() {
		return driverLicense;
	}

	public void setDriverLicense(String driverLicense) {
		this.driverLicense = driverLicense;
	}

	public boolean isOTPVerified() {
		return isOtpVerified;
	}

	public void setOtpVerified(boolean isVerified) {
		this.isOtpVerified = isVerified;
	}

	public RoleName getRoleName() {
		return roleName;
	}

	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}

	public void setId(int id) {
		this.id = id;
	}

	
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

	public boolean isAdminApproved() {
		return isAdminApproved;
	}

	public void setAdminApproved(boolean isAdminApproved) {
		this.isAdminApproved = isAdminApproved;
	}
	
	
}
