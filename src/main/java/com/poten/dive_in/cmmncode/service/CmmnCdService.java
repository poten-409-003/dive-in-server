package com.poten.dive_in.cmmncode.service;

import com.poten.dive_in.cmmncode.entity.CommonCode;
import com.poten.dive_in.cmmncode.repository.CmmnCdRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CmmnCdService {

    private final CmmnCdRepository cmmnCdRepository;

    @Transactional
    public CommonCode getCommonCode(String codeName) {
        return cmmnCdRepository.findByCodeName(codeName).orElseThrow(() -> new EntityNotFoundException("해당 코드가 존재하지 않습니다."));
    }

//    public List<String> getLevelsByLevelCd(String levelCd) {
//        return cmmnCdRepository.findLevelsByLevelCd(levelCd);
//    }

}
