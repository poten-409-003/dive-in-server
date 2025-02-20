package com.poten.dive_in.lesson.service;

import java.util.ArrayList;
import java.util.List;

public class LessonUtils {
    public static String getLevelsByCode(String levelCd) {
        List<String> levels = new ArrayList<>();

        String master = "01000";
        String advanced = "00100";
        String intermediate = "00010";
        String beginner = "00001";

        if ((Integer.parseInt(levelCd, 2) & Integer.parseInt(master, 2)) != 0) {
            levels.add("마스터즈");
        }
        if ((Integer.parseInt(levelCd, 2) & Integer.parseInt(advanced, 2)) != 0) {
            levels.add("상급");
        }
        if ((Integer.parseInt(levelCd, 2) & Integer.parseInt(intermediate, 2)) != 0) {
            levels.add("중급");
        }
        if ((Integer.parseInt(levelCd, 2) & Integer.parseInt(beginner, 2)) != 0) {
            levels.add("초급");
        }

        return String.join(", ", levels);
    }
}
