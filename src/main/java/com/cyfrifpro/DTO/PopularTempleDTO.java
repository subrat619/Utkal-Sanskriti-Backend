package com.cyfrifpro.DTO;

import java.util.Base64;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopularTempleDTO {
	private Long templeId;
	private String templeName;
	private String location;
	private String imageData;
	private String districtName;
	private Long bookingCount;
	
	public PopularTempleDTO(Long templeId, String templeName, String location, String districtName, byte[] imageData, Long bookingCount) {
        this.templeId = templeId;
        this.templeName = templeName;
        this.location = location;
        this.districtName = districtName;
        if (imageData != null) {
            this.imageData = Base64.getEncoder().encodeToString(imageData);
        } else {
            this.imageData = null;
        }
        this.bookingCount = bookingCount;
    }
}
