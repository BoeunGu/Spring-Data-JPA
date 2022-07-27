package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

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

    //new operation for DTO
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();


}
