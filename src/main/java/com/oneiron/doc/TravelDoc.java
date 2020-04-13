package com.oneiron.doc;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
public class TravelDoc implements Serializable{
	
	private static final long serialVersionUID = 15684178956478L;

	@Id
	Integer id;
	
//	@Field
	String callsign;
//	@Field
	String country;
//	@Field
	String iata;
//	@Field
	String icao;
//	@Field
	String name;
//	@Field
	String type;
//	@Field
	String airline;
//	@Field
	String airlineid;
	
//	@Field
	String destinationairport;
//	@Field
	String distance;
//	@Field
	String equipment;
	
//	@Field
	String sourceairport;
//	@Field
	String stops;

//	@Field
	List<TravelDoc> schedule;
//	@Field
	String day;
//	@Field
	String flight;
//	@Field
	String utc;
	
	String image;
	String hours;
	String address;
	String activity;
	String city;
	String alt;
	String title;
	String content;
	String tollfree;
	String url;
	String geo;
}
