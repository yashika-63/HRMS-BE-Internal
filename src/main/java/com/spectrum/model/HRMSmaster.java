package com.spectrum.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name ="HRMSmaster")
public class HRMSmaster {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
@Column(length = 100)
private String keyvalue;
@Column(length = 100)
private String data;
@Column(length = 100)
private String type;
@Column(length = 100)
private String category;
}
