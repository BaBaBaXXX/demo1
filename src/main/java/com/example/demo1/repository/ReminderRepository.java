package com.example.demo1.repository;

import com.example.demo1.entity.Reminder;
import com.example.demo1.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long>, JpaSpecificationExecutor<Reminder> {

    static Specification<Reminder> belongsToUser(User user) {
        return (reminder, cq, cb) -> {
            if (user != null) {
                return cb.equal(reminder.get("user"), user);
            }
            return cb.conjunction();
        };
    }



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




    static Specification<Reminder> likeTitleOrDescription(String query) {
        return (reminder, cq, cb) -> {
            if (query != null && !query.isEmpty()) {
                String pattern = "%" + query + "%";
                Predicate titlePredicate = cb.like(cb.lower(reminder.get("title")), pattern);
                Predicate descriptionPredicate = cb.like(cb.lower(reminder.get("description")), pattern);

                return cb.or(titlePredicate, descriptionPredicate);
            } else {
                return null;
            }
        };
    }



    default Page<Reminder> findAllWithFilter(String query,
                                             LocalDateTime firstRemind,
                                             LocalDateTime secondRemind,
                                             Pageable pageable,
                                             User user) {
        Specification<Reminder> spec = Specification
                .where(belongsToUser(user))
                .and(likeTitleOrDescription(query))
                .and(betweenDates(firstRemind, secondRemind));

        return findAll(spec, pageable);
    }



}
