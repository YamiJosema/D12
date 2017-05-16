package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
	
	@Query("select sum(r.cost) from Reward r join r.crowns where r.project.id=?1")
	Double getCurrentGoal(int projectId);
	
	@Query("select p from Project p where p.banned=false and month(current_date)<=month(p.ttl) and year(current_date)=year(p.ttl)")
	Collection<Project> findAvailableProjects();

}