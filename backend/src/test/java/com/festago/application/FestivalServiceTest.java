package com.festago.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.festago.domain.Festival;
import com.festago.domain.FestivalRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.dto.FestivalDetailResponse;
import com.festago.dto.FestivalDetailStageResponse;
import com.festago.dto.FestivalResponse;
import com.festago.dto.FestivalsResponse;
import com.festago.exception.NotFoundException;
import com.festago.support.FestivalFixture;
import com.festago.support.StageFixture;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalServiceTest {

    @Mock
    FestivalRepository festivalRepository;

    @Mock
    StageRepository stageRepository;

    @InjectMocks
    FestivalService festivalService;

    @Test
    void 모든_축제_조회() {
        // given
        Festival festival1 = FestivalFixture.festival().id(1L).build();
        Festival festival2 = FestivalFixture.festival().id(2L).build();
        given(festivalRepository.findAll()).willReturn(List.of(festival1, festival2));

        // when
        FestivalsResponse response = festivalService.findAll();

        // then
        List<Long> festivalIds = response.festivals().stream().map(FestivalResponse::id).toList();

        assertThat(festivalIds).containsExactly(1L, 2L);
    }

    @Nested
    class 축제_상세_조회 {

        @Test
        void 축제가_없다면_예외() {
            // given
            Long festivalId = 1L;
            given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> festivalService.findDetail(festivalId)).isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 축제입니다.");
        }

        @Test
        void 무대_시작시간순으로_정렬() {
            // given
            Long festivalId = 1L;
            Festival festival = FestivalFixture.festival().id(festivalId).build();
            LocalDateTime now = LocalDateTime.now();
            Stage stage1 = StageFixture.stage().id(1L).startTime(now).festival(festival).build();
            Stage stage2 = StageFixture.stage().id(2L).startTime(now.plusDays(1)).festival(festival).build();

            given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
            given(stageRepository.findAllDetailByFestivalId(festival.getId())).willReturn(List.of(stage2, stage1));

            // when
            FestivalDetailResponse response = festivalService.findDetail(festivalId);

            // then
            List<Long> stageIds = response.stages().stream().map(FestivalDetailStageResponse::id).toList();
            assertThat(stageIds).containsExactly(1L, 2L);
        }
    }
}