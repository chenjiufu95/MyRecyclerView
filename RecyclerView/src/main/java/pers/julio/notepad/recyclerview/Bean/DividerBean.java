package pers.julio.notepad.recyclerview.Bean;

import androidx.annotation.ColorInt;

public class DividerBean {
    public final int SIZE;
    public final int COLOR;
    public final int START;
    public final int END;

    public DividerBean(int size, @ColorInt int color, int start, int end) {
        SIZE = size;
        COLOR = color;
        START = start;
        END = end;
    }
}
