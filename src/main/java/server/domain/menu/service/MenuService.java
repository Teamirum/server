package server.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.market.domain.Market;
import server.domain.market.repository.MarketRepository;
import server.domain.member.mapper.MemberMapper;
import server.domain.member.repository.MemberRepository;
import server.domain.menu.domain.Menu;
import server.domain.menu.dto.MenuRequestDto;
import server.domain.menu.dto.MenuResponseDto;
import server.domain.menu.model.MenuType;
import server.domain.menu.repository.MenuRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MarketRepository marketRepository;
    private final MemberRepository memberRepository;

    public MenuResponseDto.MenuTaskSuccessResponseDto registerMarket(MenuRequestDto.CreateMenuRequestDto requestDto, String memberId) {
        Long marketIdx = getMarketIdx(memberId);
        if (menuRepository.existsByMarketIdxAndName(marketIdx, requestDto.getName())) {
            throw new ErrorHandler(ErrorStatus.MENU_ALREADY_EXIST);
        }

        Menu menu = Menu.builder()
                .marketIdx(marketIdx)
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .menuType(MenuType.fromString(requestDto.getMenuType()))
                .build();
        menuRepository.save(menu);

        Menu savedMenu = menuRepository.findByMarketIdxAndName(marketIdx, requestDto.getName()).orElseThrow(() -> new ErrorHandler(ErrorStatus.MENU_SAVE_FAIL));

        return MenuResponseDto.MenuTaskSuccessResponseDto.builder()
                .idx(savedMenu.getIdx())
                .isSuccess(true)
                .build();
    }

    private Long getMarketIdx(String memberId) {
        return marketRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MARKET_NOT_FOUND)).getIdx();
    }

}
