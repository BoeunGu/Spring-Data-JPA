package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team,Long> {
    //JpaRepository<T,ID> -> T는 @Entity 타입, ID는 @Entity에 매핑된 PK
}
