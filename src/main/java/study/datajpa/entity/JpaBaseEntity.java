package study.datajpa.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Getter;

@MappedSuperclass //실제 상속관계는 아니고 클래스의 속성들만 상속받는 클래스(테이블)에 같이 쓸 수 있게 해줌
@Getter
public class JpaBaseEntity {

	@Column(updatable = false) //생성날짜는 변경되지 못하게함, updatable=false는 DB에 값이 변경되지 못하도록 막아줌
	private LocalDateTime createDate;

	private LocalDateTime updateDate;

	@PrePersist //persist가 일어나기전에 실행됨
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		createDate = now;
		updateDate = now;

	}
	@PreUpdate
	public void preUpdate(){
		updateDate=LocalDateTime.now();

	}

	}


