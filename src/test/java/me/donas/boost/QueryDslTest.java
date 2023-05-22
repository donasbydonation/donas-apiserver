package me.donas.boost;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import me.donas.boost.domain.schedule.domain.CreatorInfo;
import me.donas.boost.domain.schedule.domain.Platform;
import me.donas.boost.domain.schedule.domain.PlatformProvider;
import me.donas.boost.domain.schedule.domain.QCreatorInfo;
import me.donas.boost.domain.schedule.domain.QPlatform;
import me.donas.boost.domain.schedule.domain.QSchedule;
import me.donas.boost.domain.schedule.domain.Schedule;
import me.donas.boost.domain.schedule.dto.QScheduleQueryResponse;
import me.donas.boost.domain.schedule.dto.ScheduleQueryResponse;

@SpringBootTest
@Transactional
public class QueryDslTest {

	@Autowired
	EntityManager em;

	JPAQueryFactory queryFactory;

	@BeforeEach
	public void before() {
		queryFactory = new JPAQueryFactory(em);
		Random random = new Random(42);

		for (int i = 0; i < 10; i++) {
			CreatorInfo creatorInfo = new CreatorInfo("name" + i, "profile" + i, new ArrayList<>());
			em.persist(creatorInfo);
			int num = random.nextInt(1, 4);
			List<Platform> platforms = new ArrayList<>();
			for (PlatformProvider provider : PlatformProvider.values()) {
				if (num == 0)
					break;
				platforms.add(new Platform(creatorInfo, provider, "broadcastLink" + i + " " + provider));
				num--;
			}
			for (Platform platform : platforms) {
				em.persist(platform);
			}
			Schedule schedule = new Schedule(creatorInfo, "title", "bannerImage" + i, "description",
				LocalDateTime.of(2023, 5, 1, 13, 0).plusHours(i));
			em.persist(schedule);
		}

	}

	@Test
	void queryTest() {
		QSchedule schedule = QSchedule.schedule;
		QPlatform platform = QPlatform.platform;
		QCreatorInfo creatorInfo = QCreatorInfo.creatorInfo;
		LocalDateTime start = LocalDateTime.of(2023, 5, 1, 13, 0);
		LocalDateTime end = LocalDateTime.of(2023, 5, 1, 20, 0);

		List<ScheduleQueryResponse> result = queryFactory.select(
				new QScheduleQueryResponse(
					creatorInfo.name,
					creatorInfo.profileImage,
					platform.provider,
					platform.broadcastLink,
					schedule.id,
					schedule.title,
					schedule.bannerImage,
					schedule.description,
					schedule.scheduledTime
				)
			)
			.from(schedule)
			.leftJoin(creatorInfo).on(schedule.creatorInfo.id.eq(creatorInfo.id))
			.leftJoin(platform).on(schedule.creatorInfo.id.eq(platform.creatorInfo.id))
			.where(schedule.scheduledTime.between(start, end))
			.orderBy(schedule.scheduledTime.asc())
			.fetch();

		for (ScheduleQueryResponse scheduleQueryResponse : result) {
			System.out.println("scheduleQueryResponse.getTitle() = " + scheduleQueryResponse.getTitle());
		}

	}
}
