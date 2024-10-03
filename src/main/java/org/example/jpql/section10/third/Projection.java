package org.example.jpql.section10.third;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.example.jpql.section10.Address;
import org.example.jpql.section10.Member;
import org.example.jpql.section10.Team;

import java.util.List;

/**
 * 3. Projection[프로젝션]
 * 프로젝션 : `select`절에서 조회할 대상을 지정하는 것을 말한다. <- 엔티티 프로젝션 | 임베디드 타입 프로젝션 | 스칼라 타입 프로젝션
 * 엔티티 프로젝션 : "select m from Member m" | "select m.team from Member m"
 * 임베디드 타입 프로젝션 : "select o.address from Order o"
 * 스칼라 타입 프로젝션 : "select m.userName, m.age from Member m"
 * 프로젝션 여러 값 조회 : Query | Object[] | new
 */
public class Projection {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUserName("이름입니당!");
            member.setAge(18);

            em.persist(member);

            em.flush();
            em.clear();

            //엔티티 프로젝션
            em.createQuery("select m from Member m", Member.class);
            em.createQuery("select t from Member m join Team t", Team.class);

            //임베디드 프로젝션
            em.createQuery("select o.address from Order o", Address.class);

            //스칼라 프로젝션  & 프로젝션 - 여러 값 조회 - `Query`반환 <- 반환에 실제 담긴 값은 `Object[]`이다.
            List resultList1 = em.createQuery("select distinct m.userName, m.age from Member m")
                    .getResultList();

            Object obj = resultList1.get(0);

            Object[] result1 = (Object[]) obj;

            System.out.println("userName1 : " + result1[0]);
            System.out.println("age1 : " + result1[1]);

            //프로젝션 - 여러 값 조회 - Object[]
            List<Object[]> resultList2 = em.createQuery("select m.userName, m.age from Member m", Object[].class)
                    .getResultList();

            Object[] result2 = resultList2.get(0);

            System.out.println("userName2 : " + result2[0]);
            System.out.println("age2 : " + result2[1]);

            //프로젝션 - 여러 값 조회 - new
            List<MemberDto> resultList3 =
                    em.createQuery(
                                    "select new org.example.jpql.section10.third.MemberDto(m.userName, m.age) from Member m",
                                    MemberDto.class
                            )
                            .getResultList();

            MemberDto result3 = resultList3.get(0);

            System.out.println("userName3 : " + result3.getUserName());
            System.out.println("age3 : " + result3.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
