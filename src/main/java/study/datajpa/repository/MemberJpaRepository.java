package study.datajpa.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import study.datajpa.entity.Member;

@Repository
public class MemberJpaRepository {

    // 해당 어노테이션을 사용하면 spring container가 jpa의 영속성 컨텍스트 안에 Entity Manager를 반환함
    @PersistenceContext
    private EntityManager em;

    /*
     저장
     */
    public Member save(Member member){
        em.persist(member);
        return member;
    }

    /*
    id로 찾기
     */
    public Member find(Long id) {
        return em.find(Member.class,id);
    }

    /*
    삭제
     */
    public void delete(Member member){
        em.remove(member);
    }

    /*
    모두 조희
     */
    public List<Member> findAll(){
        //JPQL -> 객체를 대상으로 하는 쿼리
        return em.createQuery("select m from Member m",Member.class).getResultList();

    }

    /*
    Optional로 조회
     */
    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    /*
    갯수 조회
     */
    public long count(){
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    /*
    벌크성 수정 쿼리
     */
    public int bulkAgePlus(int age) {
        return em.createQuery("update  Member m set m.age = m.age+1 where m.age>=:age")
            .setParameter("age", age)
            .executeUpdate();
    }

    /*
    페이징
     */
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery(("select m from Member m where m.age = :age order by m.username desc "))
            .setParameter("age", age)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }

    /*
    Total Count
     */
    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age =: age", Long.class)
            .setParameter("age", age)
            .getSingleResult();
    }

}
