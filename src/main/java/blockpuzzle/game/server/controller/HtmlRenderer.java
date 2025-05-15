package blockpuzzle.game.server.controller;

import org.springframework.stereotype.Component;
import blockpuzzle.game.game.field.Field;
import blockpuzzle.game.game.field.FieldBlock;
import blockpuzzle.game.game.field.block.Block;

@Component("renderer")
public class HtmlRenderer {

    /*cell*/
    public String cell(Field field, int r, int c, Integer selectedIdx) {

        FieldBlock fb = field.getBlock(r, c);

        // blocks render
        if (fb != null && fb.getStone() != null) {
            int clr = fb.getStone().getColor();                    // 1..6
            return "<span class='stone c" + clr + "'>■</span>";
        }

        // free cells render
        String hl = (selectedIdx != null) ? " highlight" : "";
        return "<a class='cell" + hl + "' " +
                "href='/bpuzzle?command=place&row=" + r + "&col=" + c + "'>·</a>";
    }

    /* ───────────── ASCII-preview ───────────── */
    public String block(Block b) {

        if (b == null)
            return "<i>USED</i>";

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE,
                maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (int[] p : b.getShape()) {
            minX = Math.min(minX, p[0]);  maxX = Math.max(maxX, p[0]);
            minY = Math.min(minY, p[1]);  maxY = Math.max(maxY, p[1]);
        }

        int clr = b.getColor();

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