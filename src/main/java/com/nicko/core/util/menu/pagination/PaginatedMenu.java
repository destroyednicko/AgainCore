package com.nicko.core.util.menu.pagination;

import lombok.Getter;
import org.bukkit.entity.Player;
import com.nicko.core.util.menu.Button;
import com.nicko.core.util.menu.Menu;

import java.util.HashMap;
import java.util.Map;

public abstract class PaginatedMenu extends Menu {

    @Getter
    public int page = 1;

    {
        setUpdateAfterClick(false);
    }

    @Override
    public String getTitle(Player player) {
        return getPrePaginatedTitle(player);
    }

    /**
     * Altera o número da página.
     *
     * @param player o jogador visualizando o inventário.
     * @param mod    delta que irá modificar a página.
     */
    public final void modPage(Player player, int mod) {
        page += mod;
        getButtons().clear();
        openMenu(player);
    }

    /**
     * Obtem a página do jogador.
     *
     * @param player o jogador visualizando o inventário
     * @return A página.
     */
    public final int getPages(Player player) {
        int buttonAmount = getAllPagesButtons(player).size();

        if (buttonAmount == 0) {
            return 1;
        }

        return (int) Math.ceil(buttonAmount / (double) getMaxItemsPerPage(player));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        int minIndex = (int) ((double) (page - 1) * getMaxItemsPerPage(player));
        int maxIndex = (int) ((double) (page) * getMaxItemsPerPage(player));
        int topIndex = 0;

        HashMap<Integer, Button> buttons = new HashMap<>();

        for (Map.Entry<Integer, Button> entry : getAllPagesButtons(player).entrySet()) {
            int ind = entry.getKey();

            if (ind >= minIndex && ind < maxIndex) {
                ind -= (int) ((double) (getMaxItemsPerPage(player)) * (page - 1)) - 9;
                buttons.put(ind, entry.getValue());

                if (ind > topIndex) {
                    topIndex = ind;
                }
            }
        }

        buttons.put(0, new PageButton(-1, this));
        buttons.put(8, new PageButton(1, this));

        for (int i = 1; i < 8; i++) {
            buttons.put(i, getPlaceholderButton());
        }

        Map<Integer, Button> global = getGlobalButtons(player);

        if (global != null) {
            for (Map.Entry<Integer, Button> gent : global.entrySet()) {
                buttons.put(gent.getKey(), gent.getValue());
            }
        }

        return buttons;
    }

    public int getMaxItemsPerPage(Player player) {
        return 18;
    }

    /**
     * @param player o jogador visualizando o inventário.
     * @return o mapa do botão que obtem os itens de todos os inventários.
     */
    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    /**
     * @param player o jogador visualizando o inventário.
     * @return o título do inventário pós-alteração.
     */
    public abstract String getPrePaginatedTitle(Player player);

    /**
     * @param player o jogador visualizando o inventário.
     * @return o mapa do botão que será paginado e espalhado pelas páginas.
     */
    public abstract Map<Integer, Button> getAllPagesButtons(Player player);

}
