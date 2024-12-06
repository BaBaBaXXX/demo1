package com.example.demo1.repository;

import com.example.demo1.entity.Reminder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;


public interface ReminderRepository extends JpaRepository<Reminder, Long>, JpaSpecificationExecutor<Reminder> {

    List<Reminder> findAllByUserId (Long userId);



    static Specification<Reminder> likeTitle(String title) {
        return (reminder, cq, cb) -> {
            if (title != null) {
                return cb.like(reminder.get("title"), "%" + title.toLowerCase() + "%");
            }
            else {
                return null;
            }
        };
    }


    static Specification<Reminder> likeDescription(String description) {
        return (reminder, cq, cb) -> {
            if (description != null) {
                return cb.like(reminder.get("description"), "%" + description.toLowerCase() + "%");
            }
            else {
                return null;
            }
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


    default List<Reminder> findAllWithFilter(String title, String description, LocalDateTime firstRemind, LocalDateTime secondRemind) {
        return findAll(Specification.where(likeTitle(title)).
                and(likeDescription(description)).
                and(betweenDates(firstRemind, secondRemind)));
    }


}
