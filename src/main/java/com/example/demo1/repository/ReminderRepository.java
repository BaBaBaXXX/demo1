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



//    static Specification<Reminder> likeTitle(String title) {
//        return (reminder, cq, cb) -> {
//            if (title != null) {
//                return cb.like(reminder.get("title"), "%" + title.toLowerCase() + "%");
//            }
//            else {
//                return null;
//            }
//        };
//    }
//
//
//    static Specification<Reminder> likeDescription(String description) {
//        return (reminder, cq, cb) -> {
//            if (description != null) {
//
//                return cb.like(reminder.get("description"), "%" + description.toLowerCase() + "%");
//            }
//            else {
//                return null;
//            }
//        };
//    }

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


    // И восстали машины из пепла ядерного огня... (украл у искусственного интеллекта)
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
