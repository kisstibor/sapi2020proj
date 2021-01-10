//package ro.sapientia2015.story.model;
//
//import java.util.Arrays;
//import java.util.List;
//
//import javax.persistence.Column;
//import javax.persistence.ElementCollection;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
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
//@Table(name = "project")
//public class Project {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;
//
//	@Column(name = "name", nullable = false, length = 100)
//	private String name;
//
//	@Column(name = "product_owner", nullable = false, length = 100)
//	private String productOwner;
//
//	@Column(name = "scrum_master", nullable = false, length = 100)
//	private String scrumMaster;
//
//	@Column(name = "members")
//	@ElementCollection(fetch = FetchType.EAGER)
//	private List<String> members;
//
////	@Column(name = "team")
////	private Team team;
//
//	@Version
//	private long version;
//
//	public Project() {
//
//	}
//
//	public static Builder getBuilder(String name) {
//		return new Builder(name);
//	}
//
//	public Long getId() {
//		return id;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public String getProductOwner() {
//		return productOwner;
//	}
//
//	public String getScrumMaster() {
//		return scrumMaster;
//	}
//
//	public List<String> getMembers() {
//		return members;
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
//	public void update(String name, String productOwner, String scrumMaster, String members) {
//		this.name = name;
//		this.productOwner = productOwner;
//		this.scrumMaster = scrumMaster;
//		String[] names = members.split("\n");
//		this.members = Arrays.asList(names);
//	}
//
//	public static class Builder {
//
//		private Project built;
//
//		public Builder(String name) {
//			built = new Project();
//			built.name = name;
//		}
//
//		public Project build() {
//			return built;
//		}
//
//		public Builder productOwner(String productOwner) {
//			built.productOwner = productOwner;
//			return this;
//		}
//
//		public Builder scrumMaster(String scrumMaster) {
//			built.scrumMaster = scrumMaster;
//			return this;
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
