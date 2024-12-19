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

    // 입력값에 해당하는 enum 항목을 찾는 메서드
    public static String findKoreanNameByInput(String input) {
        for (CategoryType type : CategoryType.values()) {
            if (type.name().equalsIgnoreCase(input)) {
                return type.getKoreanName();
            }
        }
        return null; // 일치하는 항목이 없을 경우 null 반환
    }
}



