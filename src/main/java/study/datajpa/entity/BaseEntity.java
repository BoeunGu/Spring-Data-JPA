package study.datajpa.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@EntityListeners(AuditingEntityListener.class) //이벤트기반으로 뭔가 동작한다는 것을 나타내는 애노테이션
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {


	@Column(updatable = false)
	@CreatedBy
	private String createdBy;

	@LastModifiedBy
	private String lastModifiedBy;

}
