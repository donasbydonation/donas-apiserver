package me.donas.boost;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import me.donas.boost.domain.schedule.application.ScheduleServiceV2;
import me.donas.boost.domain.schedule.domain.CreatorInfo;
import me.donas.boost.domain.schedule.domain.Platform;
import me.donas.boost.domain.schedule.domain.PlatformProvider;
import me.donas.boost.domain.schedule.domain.Schedule;
import me.donas.boost.domain.schedule.dto.ScheduleResultResponses;
import me.donas.boost.domain.schedule.repository.CreatorInfoRepository;
import me.donas.boost.domain.schedule.repository.ScheduleQueryRepository;

@SpringBootTest
@Transactional
public class ScheduleRecommendPageAlgorithmTest {
	@Autowired
	EntityManager em;

	@Autowired
	ScheduleQueryRepository scheduleQueryRepository;

	@Autowired
	CreatorInfoRepository creatorInfoRepository;

	@Autowired
	ScheduleServiceV2 scheduleService;

	@BeforeEach
	void init() {
		for (int i = 0; i < 10; i++) {
			String creatorName = "creatorName" + i;
			String profileImage = "creatorProfileImage" + i;
			CreatorInfo creatorInfo = new CreatorInfo(creatorName, profileImage, new ArrayList<>());
			em.persist(creatorInfo);

			for (PlatformProvider provider : PlatformProvider.values()) {
				if (PlatformProvider.TOTAL.equals(provider))
					continue;
				Platform platform = new Platform(creatorInfo, provider, "broadcastLink" + i + " " + provider);
				em.persist(platform);
			}
			for (int j = 0; j < 10; j++) {
				Schedule schedule = new Schedule(creatorInfo, "title", "bannerImage" + i, "description",
					LocalDateTime.of(2023, 5, 1, 13, 0).plusHours(i * 10 + j));
				System.out.println("ScheduleRecommendPageAlgorithmTest.init" + schedule.getScheduledTime());
				em.persist(schedule);
			}
			em.flush();
			em.clear();
		}
	}

	@Test
	void test() {
		int day = 0;
		ZonedDateTime now1 = ZonedDateTime.of(2023, 5, 2, 4, 0, 0, 0, ZoneOffset.of("+09:00"));
		ZonedDateTime now2 = ZonedDateTime.of(2023, 5, 2, 15, 1, 0, 0, ZoneOffset.of("+09:00"));
		ZonedDateTime now3 = ZonedDateTime.of(2023, 5, 2, 19, 0, 0, 0, ZoneOffset.of("+09:00"));
		ZonedDateTime now4 = ZonedDateTime.of(2023, 5, 2, 23, 1, 0, 0, ZoneOffset.of("+09:00"));

		ScheduleResultResponses scheduleResultResponses = scheduleService
			.readSchedules(now4, day, PlatformProvider.TWITCH, -1, 5);
		System.out.println();
	}
}