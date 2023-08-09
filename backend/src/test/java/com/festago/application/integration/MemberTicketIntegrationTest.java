package com.festago.application.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.application.MemberTicketService;
import com.festago.domain.Festival;
import com.festago.domain.FestivalRepository;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.domain.TicketRepository;
import com.festago.dto.MemberTicketResponse;
import com.festago.dto.MemberTicketsResponse;
import com.festago.support.FestivalFixture;
import com.festago.support.MemberFixture;
import com.festago.support.MemberTicketFixture;
import com.festago.support.StageFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberTicketIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    MemberTicketService memberTicketService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberTicketRepository memberTicketRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Test
    void 예매한_티켓_조회시_Pageable_적용() {
        // given
        Member member = memberRepository.save(MemberFixture.member().build());
        Festival festival = festivalRepository.save(FestivalFixture.festival().build());
        Stage stage = stageRepository.save(StageFixture.stage().festival(festival).build());
        for (int i = 0; i < 20; i++) {
            memberTicketRepository.save(MemberTicketFixture.memberTicket()
                .stage(stage)
                .owner(member)
                .build()
            );
        }

        // when
        MemberTicketsResponse actual = memberTicketService.findAll(member.getId(), PageRequest.of(0, 10));

        // then
        assertThat(actual.memberTickets()).hasSize(10);
    }

    @Test
    void name() {
        // given
        Member member = memberRepository.save(MemberFixture.member().build());
        Festival festival = festivalRepository.save(FestivalFixture.festival().build());
        Stage stage = stageRepository.save(StageFixture.stage().festival(festival).build());
        MemberTicket ticket1 = MemberTicketFixture.memberTicket()
            .owner(member)
            .stage(stage)
            .entryTime(LocalDateTime.now().plusHours(1))
            .build();
        MemberTicket ticket2 = MemberTicketFixture.memberTicket()
            .owner(member)
            .stage(stage)
            .entryTime(LocalDateTime.now().plusHours(2))
            .build();
        MemberTicket ticket3 = MemberTicketFixture.memberTicket()
            .owner(member)
            .stage(stage)
            .entryTime(LocalDateTime.now().minusHours(2))
            .build();
        MemberTicket ticket4 = MemberTicketFixture.memberTicket()
            .owner(member)
            .stage(stage)
            .entryTime(LocalDateTime.now().minusHours(1))
            .build();
        memberTicketRepository.saveAll(List.of(ticket1, ticket2, ticket3, ticket4));

        // when
        MemberTicketsResponse actual = memberTicketService.findCurrent(member.getId(), PageRequest.of(0, 100));

        // then
        List<Long> memberTicketIds = actual.memberTickets().stream()
            .map(MemberTicketResponse::id)
            .toList();
        assertThat(memberTicketIds).containsExactly(
            ticket4.getId(),
            ticket3.getId(),
            ticket1.getId(),
            ticket2.getId()
        );
    }
}