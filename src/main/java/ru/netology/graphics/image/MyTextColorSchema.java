package ru.netology.graphics.image;

public final class MyTextColorSchema implements TextColorSchema {
    private static final char[] SYM = {'#', '$', '@', '%', '*', '+', '-', '\''};
//    private static final char[] SYM = {'▇', '●', '◉', '◍', '◎', '○', '☉', '◌', '-'};
    private static final int INTERVAL = (int) Math.ceil(255.0 / SYM.length);

    @Override
    public char convert(int color) {
        return SYM[color / INTERVAL];
    }
}
