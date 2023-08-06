package com.box.challenge.model.audit;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
@JsonIgnoreProperties(value = { "createdBy", "updatedBy" }, allowGetters = true)
public abstract class UserDateAudit extends DateAudit {
	@CreatedBy
	@Column(name = "CREATED_BY", updatable = false)
	@NumberFormat
	@JsonProperty("created_by")
	private Long createdBy;

	@LastModifiedBy
	@Column(name = "UPDATED_BY")
	@NumberFormat
	@JsonProperty("updated_by")
	private Long updatedBy;

	public UserDateAudit() {
		super();
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
}
