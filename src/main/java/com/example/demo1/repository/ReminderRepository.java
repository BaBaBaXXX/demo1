package com.example.demo1.repository;

import com.example.demo1.entity.Reminder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;


public interface ReminderRepository extends JpaRepository<Reminder, Long>, JpaSpecificationExecutor<Reminder> {

    List<Reminder> findAllByUserId (Long userId);



    static Specification<Reminder> betweenDates(LocalDateTime firstRemind, LocalDateTime secondRemind) {
        return (reminder, cq, cb) -> {
            if (firstRemind != null && secondRemind != null) {
                return cb.between(reminder.get("remind"), firstRemind, secondRemind);
            }
            else {
                return null;
            }
        };
    }


    //todo lower + toLowerCase ??? чзх????
    static Specification<Reminder> likeTitleOrDescription(String query) {
        return (reminder, cq, cb) -> {
            if (query != null && !query.isEmpty()) {
                String pattern = "%" + query.toLowerCase() + "%";
                Predicate titlePredicate = cb.like(cb.lower(reminder.get("title")), pattern);
                Predicate descriptionPredicate = cb.like(cb.lower(reminder.get("description")), pattern);

                return cb.or(titlePredicate, descriptionPredicate);
            } else {
                return null;
            }
        };
    }



    default List<Reminder> findAllWithFilter(String query, LocalDateTime firstRemind, LocalDateTime secondRemind) {
        return findAll(Specification.where(likeTitleOrDescription(query)).
                and(betweenDates(firstRemind, secondRemind)));
    }


}
