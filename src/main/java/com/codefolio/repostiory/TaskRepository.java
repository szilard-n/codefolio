package com.codefolio.repostiory;

import com.codefolio.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query("select t from Task t " +
            "where t.projectId = :projectId and (t.createdBy = :workerId or t.createdBy = :creatorId) " +
            "order by t.index")
    List<Task> findByCreatorOrWorkerId(UUID projectId, UUID workerId, UUID creatorId);

}
