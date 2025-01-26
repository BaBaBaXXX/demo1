package com.example.demo1.mapper;

import com.example.demo1.dto.JobReminderDto;
import com.example.demo1.entity.Reminder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReminderMapper {

    @Mapping(target = "email", qualifiedByName = "getEmailFromReminder", source = "reminder")
    @Mapping(target = "telegramId", qualifiedByName = "getTelegramIdFromReminder", source = "reminder")
    JobReminderDto toJobReminderDto(Reminder reminder);

    List<JobReminderDto> toJobReminderDtoList(List<Reminder> reminders);

    @Named("getEmailFromReminder")
    default String getEmailFromReminder(Reminder reminder) {
        return reminder.getUser().getEmail();
    }

    @Named("getTelegramIdFromReminder")
    default Long getTelegramIdFromReminder(Reminder reminder) {
        return reminder.getUser().getTelegramId();
    }
}
