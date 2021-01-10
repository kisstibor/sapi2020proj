//package ro.sapientia2015.story.model;
//
//import java.util.List;
//
//import javax.persistence.Column;
//import javax.persistence.ElementCollection;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.PrePersist;
//import javax.persistence.PreUpdate;
//import javax.persistence.Table;
//import javax.persistence.Version;
//
//import org.apache.commons.lang.builder.ToStringBuilder;
//
//@Entity
//@Table(name = "team")
//public class Team {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;
//
//	@Column(name = "scrum_master", nullable = false, length = 100)
//	private String scrumMaster;
//
//	@Column(name = "members")
//	@ElementCollection
//	private List<String> members;
//
//	@Version
//	private long version;
//
//	public Team() {
//
//	}
//
//	public static Builder getBuilder(String scrumMaster) {
//		return new Builder(scrumMaster);
//	}
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getScrumMaster() {
//		return scrumMaster;
//	}
//
//	public void setScrumMaster(String scrumMaster) {
//		this.scrumMaster = scrumMaster;
//	}
//
//	public List<String> getMembers() {
//		return members;
//	}
//
//	public void setMembers(List<String> members) {
//		this.members = members;
//	}
//
//	public long getVersion() {
//		return version;
//	}
//
//	@PrePersist
//	public void prePersist() {
//	}
//
//	@PreUpdate
//	public void preUpdate() {
//	}
//
//	public void update(String scrumMaster, List<String> members) {
//		this.scrumMaster = scrumMaster;
//		this.members = members;
//	}
//
//	public static class Builder {
//
//		private Team built;
//
//		public Builder() {
//			built = new Team();
//		}
//
//		public Builder setScrumMaster(String scrumMaster) {
//			this.built.scrumMaster = scrumMaster;
//			return this;
//		}
//
//		public Builder(String scrumMaster) {
//			built = new Team();
//			built.scrumMaster = scrumMaster;
//		}
//
//		public Team build() {
//			return built;
//		}
//
//		public Builder members(List<String> members) {
//			built.members = members;
//			return this;
//		}
//	}
//
//	@Override
//	public String toString() {
//		return ToStringBuilder.reflectionToString(this);
//	}
//}
