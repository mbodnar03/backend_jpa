package com.jpacourse.persistance.entity;

import java.time.LocalDateTime;
import java.util.Collection;

import jakarta.persistence.*;
import org.hibernate.FetchMode;
import org.hibernate.annotations.Fetch;

@Entity
@Table(name = "VISIT")
public class VisitEntity {


	@OneToMany(
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER
	)
	@JoinColumn(name = "VISIT_ID")
	private Collection<MedicalTreatmentEntity> medicalTreatmentEntityCollection; //jednostronna od strony dziecka


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	@Column(nullable = false)
	private LocalDateTime time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

}
