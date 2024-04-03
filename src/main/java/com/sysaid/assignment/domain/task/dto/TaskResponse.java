package com.sysaid.assignment.domain.task.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskResponse {
     Long id;
     String   activity;
     String   type;
     Integer  participants;
     Float    price;
     String   link;
     String   key;
     Float    accessibility;
}
