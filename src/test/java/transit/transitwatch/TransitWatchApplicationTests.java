package transit.transitwatch;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import transit.transitwatch.entity.QHello;

@SpringBootTest
@Transactional
class TransitWatchApplicationTests {

	@Autowired
	EntityManager em;
	/*
	* querydsl 동작 테스트
	* */
	@Test
	void contextLoads() {
		Hello hello = new Hello();
		em.persist(hello);
		JPAQueryFactory query = new JPAQueryFactory(em);
		QHello qHello = QHello.hello; //
		Hello result = query
				.selectFrom(qHello)
				.fetchOne();
		Assertions.assertThat(result).isEqualTo(hello);
		Assertions.assertThat(result.getId()).isEqualTo(hello.getId());
	}
}
