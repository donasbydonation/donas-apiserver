package me.donas.boost.domain.schedule.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.schedule.application.SearchBetweenTime;
import me.donas.boost.domain.schedule.domain.PlatformProvider;
import me.donas.boost.domain.schedule.domain.QCreatorInfo;
import me.donas.boost.domain.schedule.domain.QPlatform;
import me.donas.boost.domain.schedule.domain.QSchedule;
import me.donas.boost.domain.schedule.dto.QScheduleQueryResponse;
import me.donas.boost.domain.schedule.dto.ScheduleQueryResponse;

@Repository
@RequiredArgsConstructor
public class ScheduleQueryRepository {
	private final JPAQueryFactory queryFactory;

	public List<ScheduleQueryResponse> findAllByScheduledTimeBetweenAndPlatformProvider(int day,
		SearchBetweenTime searchBetweenTime, PlatformProvider provider) {
		QSchedule schedule = QSchedule.schedule;
		QCreatorInfo creatorInfo = QCreatorInfo.creatorInfo;
		QPlatform platform = QPlatform.platform;
		LocalDateTime start = searchBetweenTime.start();
		LocalDateTime end = searchBetweenTime.end();

		return queryFactory.select(new QScheduleQueryResponse(
				creatorInfo.name,
				creatorInfo.profileImage,
				platform.provider,
				platform.broadcastLink,
				schedule.id,
				schedule.title,
				schedule.bannerImage,
				schedule.description,
				schedule.scheduledTime
			))
			.from(schedule)
			.leftJoin(creatorInfo).on(schedule.creatorInfo.id.eq(creatorInfo.id))
			.leftJoin(platform).on(schedule.creatorInfo.id.eq(platform.creatorInfo.id))
			.where(
				scheduledTimeBetween(day, start, end),
				platformProviderEq(provider)
			)
			.orderBy(schedule.scheduledTime.asc())
			.fetch();
	}

	private BooleanExpression scheduledTimeBetween(int day, LocalDateTime start, LocalDateTime end) {
		return QSchedule.schedule.scheduledTime.between(start.plusDays(day), end.plusDays(day));
	}

	private BooleanExpression platformProviderEq(PlatformProvider provider) {
		return (PlatformProvider.TOTAL.equals(provider)) ? null : QPlatform.platform.provider.eq(provider);
	}
}
