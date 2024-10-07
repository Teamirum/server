package server.domain.menu.dto;

import server.domain.menu.domain.Menu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuDtoConverter {

    public static MenuResponseDto.AllMenuInfoResponseDto convertToAllMenuInfoResponseDto(List<Menu> menu) {
        List<MenuResponseDto.MenuInfoResponseDto> menuInfoResponseDtoList = menu.stream()
                .map(MenuDtoConverter::convertToMenuInfoResponseDto)
                .toList();
        return MenuResponseDto.AllMenuInfoResponseDto.builder()
                .totalCnt(menu.size())
                .menuList(menuInfoResponseDtoList)
                .isSuccess(true)
                .build();
    }

    public static MenuResponseDto.MenuInfoResponseDto convertToMenuInfoResponseDto(Menu menu) {
        return MenuResponseDto.MenuInfoResponseDto.builder()
                .idx(menu.getIdx())
                .marketIdx(menu.getMarketIdx())
                .name(menu.getName())
                .price(menu.getPrice())
                .menuType(menu.getMenuType().name())
                .build();
    }
}
