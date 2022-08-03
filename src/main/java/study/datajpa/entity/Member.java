package study.datajpa.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id","username","age"})
@Getter
@Setter
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username= :username"
)
public class Member extends JpaBaseEntity{

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY) //member만 조회시 team은 가짜객체를 참조하고 실제 team의 데이터를 사용하는 시점에 sql이 날라감 -> 지연로딩
    @JoinColumn(name="team_id")
    private Team team;


    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username=username;
        this.age=age;
        if(team !=null){
            changeTeam(team);
        }

    }

    public void changeTeam(Team team){
        this.team=team;
        team.getMembers().add(this);

    }
}
