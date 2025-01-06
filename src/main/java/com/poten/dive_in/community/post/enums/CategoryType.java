package com.poten.dive_in.community.post.enums;

public enum CategoryType {
    COMPETITION("수영대회"),
    POOL("수영장"),
    GOODS("수영물품"),
    COMMUNICATION("소통해요");

    private final String koreanName;

    CategoryType(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }

    public static String findKoreanNameByInput(String input) {
        for (CategoryType type : CategoryType.values()) {
            if (type.name().equalsIgnoreCase(input)) {
                return type.getKoreanName();
            }
        }
        return null;
    }
}



