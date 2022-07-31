package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager entityManager;//이게 영속성 컨텍스트, 같은 transaction이면 같은 엔티티매니저가 동작 함

    @Test
    public void testMember(){
        Member member= new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);

    }


    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findMemberDto(){
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }


    @Test
    public void findByNames(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }

    }

    @Test
    @DisplayName("벌크성 쿼리")
    public void bulkUpdate(){

        //given
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",20));
        memberRepository.save(new Member("member3",30));
        memberRepository.save(new Member("member4",40));
        memberRepository.save(new Member("member5",50));

        //when
        int resultcount = memberRepository.bulkAgePlus(20); //모든 member의 나이가 1살 증가됨, 하지만 bulk성 쿼리는 영속성 컨텍스트를 거치지 않고
        //바로 DB로 접근하기 때문에 영속성컨텍스트의 객체와 실제 DB의 내용이 동일하지 않음

        //그렇기때문에 엔티티매니저를 날려주어야한다.
        entityManager.flush(); //혹시 모를 데이터변경이 있을 수 있기에 DB에 쿼리를 날리고
        entityManager.clear();//엔티티매니저를 날린다. -> 영속성컨텍스트안에 데이터를 완전히 날려버림(같은 transaction안에서 뒤에 로직이 있는경우)
        //@Modifying(clearAutomatically=true)를 해주면 해당 JPQL실행 후 엔티티매니저를 알아서 날려준다.


        List<Member> result = memberRepository.findByUsername("member5"); //영속성컨텍스트가 없기 때문에 DB에서 다시 데이터를 조회해온다.
        Member member5 = result.get(0);//엔티티 매니저를 날라지 않으면 여전히 age가 50이다. //날리면 51로 정상



        //then
        assertThat(resultcount).isEqualTo(3);

    }


    @Test// N+1 문제 (member를 조회하는 쿼리 1번 + member 안에 연관된 테이블 조회용 쿼리 N번 (team table에 대한 쿼리 1번) 각각 네트워크를 타기때문에 성능에 좋지 않다.
    public void findMemberLazy(){
        //given
        //member1 -> teamA를 참조
        //member2 -> teamB를 참조

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();//영속성 컨텍스트의 정보를 DB에 완전히 동기화함
        entityManager.clear();//영속성 컨텍스트를 완전히 날려버림

        //when

        //select Member -> Member에 대한 데이터만 DB에서 들고옴, team은 프록시객체를 만들어 둠
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.team = " + member.getTeam().getName()); //실제 DB에 team에 대한 sql을 날려 데이터를 들고옴
        }

    }

    @Test
    public void queryHint(){

        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        entityManager.flush();
        entityManager.clear();

        //when1
        Member findMember = memberRepository.findById(member1.getId()).get();
        findMember.setUsername("member2"); //Dirty Checking 발생, Update쿼리가 날라감 => 원본 객체(스냅샷)와 바뀐객체 2개가 존재하고 이 두개를 비교하는 프로세스가 필요하다. 즉, 메모리를 먹는다.
        //만약 조회용으로만 사용한다면 두개의 객체가 영속성 컨텍스트에 있는 것은 낭비이다. 이때 -> Hibernate는 해결책을 줌 => @QueryHints

        //when2
        Member findMember2 = memberRepository.findReadOnlyByUsername("member1");// 스냅샷 자체를 만들지 않음
        findMember.setUsername("member2");//Update 쿼리 안날라감

        entityManager.flush();
    }

    @Test
    public void lock(){

        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        entityManager.flush();
        entityManager.clear();

        //when1

        List<Member> result = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void callCustom(){
        List<Member> result = memberRepository.findMemberCustom();
    }
}