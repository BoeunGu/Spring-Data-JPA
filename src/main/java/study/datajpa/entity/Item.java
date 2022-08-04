package study.datajpa.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {

	@Id
	@GeneratedValue
	private String id;

	@CreatedDate
	private LocalDateTime createdDate; //persist전에 호출된다.

	@Override
	public String getId() {
		return id;

	}

	@Override
	public boolean isNew() {
		return createdDate == null;
	}
}
