package org.example.jpql.section10.second;

import jakarta.persistence.*;
import org.example.jpql.section10.Member;

import java.util.List;

/**
 * 2. 기본 문법과 쿼리 API
 * JPQL 기본 문법 : select m from Member m where 1=1 <- 이런식으로 작성
 * 영속성 컨텍스트 flush : `영속성 컨텍스트`가 자동으로 `플러시` 되는 때 <- 커밋 | 쿼리
 * TypeQuery : `반환 타입`이 명확할 때 사용한다.
 * Query : `반환 타입`이 명확하지 않을 때 사용한다.
 * em.createQuery(...).getSingleResult() : 반환되는 값이 확실하게 하나만 있을 때 사용한다. <- 0개 혹은 2개 이상이면 예외 발생.
 * em.createQuery(...).getResultList() : 반환 값이 없더라고 깡통 List가 반환된다 <- NullPointerException 피할 수 있다.
 * em.createQuery(..where m.age = :age).setParameter("age", member.getAge()) <- `:age`에 member.getAge() 파라미터를 바인딩 한다.
 */
public class JpqlSyntax {

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

            //플러시 : 커밋 | 쿼리 <- 둘 중 하나가 실행되면 플러시 된다.
            System.out.println("=======영속성 컨텍스트가 관리하에 쿼리문이 나가면 여기서 플러시 된다.=========");
            //별칠은 필수다 : Member `m` <- m이 별칭
            TypedQuery<Member> typeQueryMember = em.createQuery("select m from Member m", Member.class);
            System.out.println("typeQueryMember : " + typeQueryMember);
            List<Member> resultList = typeQueryMember.getResultList();

            Query queryMember = em.createQuery("select m.userName, m.age from Member m");
            System.out.println("queryMember : " + queryMember);
            Object queryResultMember = queryMember.getSingleResult();

            Member jpqlFindMember = em.createQuery("select m from Member m where m.age = :age", Member.class)
                    .setParameter("age", member.getAge())
                    .getSingleResult();
            System.out.println("jpqlFindMember : " + jpqlFindMember.getUserName());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
