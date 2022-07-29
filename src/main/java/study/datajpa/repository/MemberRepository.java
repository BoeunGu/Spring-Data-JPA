package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {

 //구현체가 없어도 spring data Jpa가 Jpa관련 인터페이스를 상속받은 인터페이스의 구현클래스를(proxy 객체) 만들어서 요청하는 곳에 주입시킴

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    // 복잡한 JPQL인 경우에 아래에 바로 짜고 사용할 수 있다. @Query를 사용하면 Applicaton 로딩 시점에 JPQL에러가 있으면 알려준다.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser (@Param("username") String username, @Param("age") int age);

    //DTO조회, Member의 이름만 조회하고 싶을 때
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //DTO로 조회하기 -> new operation for DTO 사용, 엔티티의 생성자에 있는 생성자를 사용하여야한다.
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto(); // 만약 해당조건에 맞는 객체가 없어도 null을 반환하지 않고 empty Collection을 반환함. 하지만 java8부터는 optional(단건조회!)이 있어서 이걸 사용권장


    //Collection 조회 -> 파라미터 바인딩으로 변수로 넘겨줄 수 있다.
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);


    //bulk쿼리는 영속성컨텍스트를 거치지 않고 바로 db에 전달됨 (주의해야함)
    @Modifying(clearAutomatically = true) //select가 아니라 무언가를 변경하는 것(update 쿼리)을 알려줌
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


}
