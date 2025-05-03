package sk.tuke.kpi.kp.blockpuzzle.gamestudio.server.controller;

import org.springframework.stereotype.Component;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.game.field.Field;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.game.field.FieldBlock;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.game.field.block.Block;

/**
 * Генерирует готовые HTML‑фрагменты для Thymeleaf (через utext).
 */
@Component("renderer")
public class HtmlRenderer {

    /* ───────────── ОДНА КЛЕТКА ПОЛЯ ───────────── */
    public String cell(Field field, int r, int c, Integer selectedIdx) {

        FieldBlock fb = field.getBlock(r, c);

        /* если клетка занята – отрисовываем «камень» нужным цветом  */
        if (fb != null && fb.getStone() != null) {
            int clr = fb.getStone().getColor();                    // 1..6
            return "<span class='stone c" + clr + "'>■</span>";
        }

        /* свободная клетка  */
        String hl = (selectedIdx != null) ? " highlight" : "";
        return "<a class='cell" + hl + "' " +
                "href='/bpuzzle?command=place&row=" + r + "&col=" + c + "'>·</a>";
    }

    /* ───────────── ASCII‑ПРЕВЬЮ БЛОКА ───────────── */
    public String block(Block b) {

        if (b == null)
            return "<i>USED</i>";

        /* вычисляем габариты */
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE,
                maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (int[] p : b.getShape()) {
            minX = Math.min(minX, p[0]);  maxX = Math.max(maxX, p[0]);
            minY = Math.min(minY, p[1]);  maxY = Math.max(maxY, p[1]);
        }

        int clr = b.getColor();   // цвет того же блока

        StringBuilder sb = new StringBuilder("<pre class='block'>");
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                final int fx = x, fy = y;
                boolean filled = b.getShape().stream()
                        .anyMatch(p -> p[0] == fx && p[1] == fy);

                if (filled)
                    sb.append("<span class='stone c")
                            .append(clr)
                            .append("'>■</span>");
                else
                    sb.append("·");
            }
            sb.append('\n');
        }
        return sb.append("</pre>").toString();
    }
}