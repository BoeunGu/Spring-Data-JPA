package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {

 //구현체가 없어도 spring data Jpa가 Jpa관련 인터페이스를 상속받은 인터페이스의 구현클래스를(proxy 객체) 만들어서 요청하는 곳에 주입시킴

}
