package ro.sapientia2015.project.dto;

import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.project.model.Project;

public class ProjectDTO {

	private Long id;

	@Length(max = Project.MAX_LENGTH_NAME)
	@NotEmpty
	private String name;

	@Length(max = Project.MAX_LENGTH_PRODUCTOWNER)
	private String productOwner;

	@Length(max = Project.MAX_LENGTH_SCRUMMASTER)
	private String scrumMaster;

	private String members;

	@Version
	private long version;

	public ProjectDTO() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductOwner() {
		return productOwner;
	}

	public void setProductOwner(String productOwner) {
		this.productOwner = productOwner;
	}

	public String getScrumMaster() {
		return scrumMaster;
	}

	public void setScrumMaster(String scrumMaster) {
		this.scrumMaster = scrumMaster;
	}

	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
