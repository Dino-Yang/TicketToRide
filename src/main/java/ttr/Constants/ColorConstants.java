package ttr.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ColorConstants {
    public static final String COLOR_RED = "red";
    public static final String COLOR_BLUE = "blue";
    public static final String COLOR_GREEN = "green";
    public static final String COLOR_YELLOW = "yellow";
    public static final String COLOR_WHITE = "white";
    public static final String COLOR_BLACK = "black";
    public static final String COLOR_RAINBOW = "loco";
    public static final String COLOR_BROWN = "brown";
    public static final String COLOR_PURPLE = "purple";
    public static final String COLOR_GRAY = "gray";

    public static final String CODE_RED = "0xff0000ff";
    public static final String CODE_BLUE = "0x0000ffff";
    public static final String CODE_GREEN = "0x32cd32ff";
    public static final String CODE_YELLOW = "0xffff00ff";
    public static final String CODE_WHITE = "0xffffffff";
    public static final String CODE_BLACK = "0x000000ff";
    public static final String CODE_BROWN = "0xffa500ff";
    public static final String CODE_PURPLE = "0xffc0cbff";
    public static final String CODE_RAINBOW = "0x800080ff";
    public static final String CODE_GRAY = "0x808080ff";

    public static ArrayList<String> getColors() {
        return new ArrayList<>(Arrays.asList(COLOR_RAINBOW, COLOR_BLACK, COLOR_BLUE, COLOR_BROWN, COLOR_GREEN,
                COLOR_PURPLE, COLOR_YELLOW, COLOR_WHITE, COLOR_RED));
    }

    public static ArrayList<String[]> getColorCodes() {
        ArrayList<String[]> cl = new ArrayList<>();
        String[] red = new String[]{COLOR_RED, CODE_RED};
        String[] blue = new String[]{COLOR_BLUE, CODE_BLUE};
        String[] green = new String[]{COLOR_GREEN, CODE_GREEN};
        String[] yellow = new String[]{COLOR_YELLOW, CODE_YELLOW};
        String[] white = new String[]{COLOR_WHITE, CODE_WHITE};
        String[] black = new String[]{COLOR_BLACK, CODE_BLACK};
        String[] brown = new String[]{COLOR_BROWN, CODE_BROWN};
        String[] purple = new String[]{COLOR_PURPLE, CODE_PURPLE};
        String[] loco = new String[]{COLOR_RAINBOW, CODE_RAINBOW};
        String[] gray = new String[]{COLOR_GRAY, CODE_GRAY};

        Collections.addAll(cl, red, blue, green, yellow, white, black, brown, purple, loco, gray);
        return cl;
    }
}
