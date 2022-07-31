package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;


@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    //실제 spring data jpa인터페이스에 해당 클래스의 인터페이스를 implements하면 해당 클래스가 실행됨
    //주로 복잡한 querydsl을 사용할 때 custom 인터페이스,클래스를 사용
    //MemberRepositoryImpl 네이밍 규칙을 지켜야함 -> "spring-data-jpa인터페이스 이름" + "Impl"
    //비즈니스로직과 분리된 쿼리들은 아예 클래스를 분리하는것이 더 좋다 -> 가독성

    private final EntityManager em; //커넥션을 얻어옴
    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
