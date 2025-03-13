//package com.cyfrifpro.DTO;
//
//import java.time.LocalDateTime;
//
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Document(indexName = "temples")
//public class TempleDocument {
//
//	@Id
//	private Long id;
//
//	@Field(type = FieldType.Text)
//	private String name;
//
//	@Field(type = FieldType.Keyword)
//	private String location;
//
//	@Field(type = FieldType.Text)
//	private String description;
//
//	@Field(type = FieldType.Keyword)
//	private String districtName;
//
//	// Optionally include image URL or other fields
//	@Field(type = FieldType.Keyword)
//	private String imageUrl;
//
//	@Field(type = FieldType.Date)
//	private LocalDateTime createdAt;
//}
