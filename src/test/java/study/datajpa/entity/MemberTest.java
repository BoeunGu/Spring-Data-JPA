package study.datajpa.entity;

import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import study.datajpa.repository.MemberRepository;

@SpringBootTest
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamA");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //초기화
        em.flush();//DB에 SQL날림
        em.clear();//영속성 컨텍스트에 있는 캐쉬 초기화

        //확인
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("-> member.team = " + member.getTeam());
                    
                    
        }


    }

    @Test
    public void JpaEventBaseEntity() throws Exception{
        //given
        Member member = new Member("member1");
        memberRepository.save(member); //@PrePersist가 발생

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush(); // @Preupdate qkftod
        em.clear();

        //when

        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        System.out.println("findMember.gerCreateDate = " + findMember.getCreateDate());
        System.out.println("findMember.gerupdateDate = " + findMember.getUpdateDate());
    }

}