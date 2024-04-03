package com.sysaid.assignment.domain.task.db;

import com.sysaid.assignment.domain.user.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    int countByTypeAndUser(String taskType, User user);
    List<Task> findByCompletedAndUser(Boolean completed, User user);
    List<Task> findByCompletedAndUserAndType(String taskType, Boolean completed, User user);
    List<Task> findByInWishlistAndUser(Boolean completed, User user);
    List<Task> findAllByUser(User user);

    @Query(
            value = "select distinct(t.rating) from Task t where t.user =: user order by t.rating desc"
    )
    List<Task> fetchDistingByRating(
            @Param("user") User user
    );

    List<Task> getTaskByRatingAndUser(Integer rating, User user);
}
