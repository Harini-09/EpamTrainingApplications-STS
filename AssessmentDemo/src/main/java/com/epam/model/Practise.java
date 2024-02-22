package com.epam.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name="practise")
public class Practise {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="practise_id")
	int id;
	@Column(name="practise_name")
	String name;
	@OneToMany(mappedBy = "practise",cascade = CascadeType.ALL)
	List<Batch> batches;
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setBatches(List<Batch> batches) {
		batches.forEach(batch->batch.setPractise(this));
		this.batches = batches;
	}
	
	
}
