package com.sysaid.assignment.domain.task.db;

import com.sysaid.assignment.domain.task.dto.TaskType;
import com.sysaid.assignment.domain.user.db.User;
import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Task.TABLE_NAME)
public class Task implements Serializable {

	public static final String TABLE_NAME = "task";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@Column(name = "activity")
	String activity;

	@Column(name = "accessibility")
	Float accessibility;

	@Column(name = "type")
	String type;

	@Column(name = "participants")
	Integer amountOfParticipants;

	@Column(name = "price")
	Float price;

	@Column(name = "link")
	String link;

	@Column(name = "key")
	String key;

	@Column(name = "assigned")
	LocalDateTime assigned;

	@Column(name = "in_wishlist", columnDefinition = "boolean default false")
	Boolean inWishlist;

	@Column(name = "completed", columnDefinition = "boolean default false")
	Boolean completed;

	@Column(name = "rating")
	Integer rating;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User user;
}

