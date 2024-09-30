package server.domain.menu.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.domain.menu.model.MenuType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    private Long idx;

    private Long marketIdx;

    private String name;

    private int price;

    private MenuType menuType;
}
