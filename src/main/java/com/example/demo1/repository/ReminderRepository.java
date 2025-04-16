package com.example.demo1.repository;

import com.example.demo1.dto.RequestReminderDto;
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
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long>, JpaSpecificationExecutor<Reminder> {

    String USER = "user";
    String REMIND = "remind";
    String TITLE = "title";
    String DESCRIPTION = "description";

    static Specification<Reminder> isReminderInFuture() {
        return (reminder, cq, cb) -> {
            LocalDateTime now = LocalDateTime.now();
            return cb.greaterThan(reminder.get(REMIND), now);
        };
    }

    static Specification<Reminder> belongsToUser(User user) {
        return (reminder, cq, cb) -> {
            if (user != null) {
                return cb.equal(reminder.get(USER), user);
            }
            return cb.conjunction();
        };
    }

    static Specification<Reminder> betweenDates(LocalDateTime firstRemind, LocalDateTime secondRemind) {
        return (reminder, cq, cb) -> {
            if (firstRemind != null && secondRemind != null) {
                return cb.between(reminder.get(REMIND), firstRemind, secondRemind);
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
                Predicate titlePredicate = cb.like(cb.lower(reminder.get(TITLE)), pattern);
                Predicate descriptionPredicate = cb.like(cb.lower(reminder.get(DESCRIPTION)), pattern);

                return cb.or(titlePredicate, descriptionPredicate);
            } else {
                return null;
            }
        };
    }

    default List<Reminder> findAllFutureReminders() {
        return findAll(isReminderInFuture());
    }

    default Page<Reminder> findAllWithFilter(RequestReminderDto requestReminderDto,
                                             Pageable pageable,
                                             User user) {
        Specification<Reminder> spec = Specification
                .where(belongsToUser(user))
                .and(likeTitleOrDescription(requestReminderDto.query()))
                .and(betweenDates(requestReminderDto.firstRemind(), requestReminderDto.secondRemind()));

        return findAll(spec, pageable);
    }
}
