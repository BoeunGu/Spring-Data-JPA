package study.datajpa.repository;

import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
//직접구현한 기능을 쓰고 싶을 때 , 인터페이스를 먼저 선언
    List<Member> findMemberCustom();
}
