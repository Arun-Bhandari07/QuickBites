package com.QuickBites.app.DTO;

public class AddressResponseDTO {

    private Long id;
    private String title;
    private String fullAddress;
    private Double latitude;
    private Double longitude;

    public AddressResponseDTO(Long id, String title, String fullAddress,
                              Double latitude, Double longitude) {
        this.id = id;
        this.title = title;
        this.fullAddress = fullAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
    
    
}
