package server.domain.businessCard.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.businessCard.domain.BusinessCard;
import server.domain.businessCard.mapper.BusinessCardMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BusinessCardRepository {

    private final BusinessCardMapper businessCardMapper;

    public List<BusinessCard> findAllBusinessCardByMemberIdx(Long memberIdx) {
        return businessCardMapper.findAllByMemberIdx(memberIdx);
    }

   public Optional<BusinessCard> findByMemberIdx(Long memberIdx) {
        BusinessCard businessCard = businessCardMapper.findByMemberIdx(memberIdx);
        if (businessCard != null) {
            return Optional.of(businessCard);
        }
        return Optional.empty();
    }

    public Optional<BusinessCard> findByBusinessCardIdx(Long BusinessCardIdx) {
        BusinessCard businessCard = businessCardMapper.findByBusinessCardIdx(BusinessCardIdx);
        if (businessCard != null) {
            return Optional.of(businessCard);
        }
        return Optional.empty();
    }

    public boolean existsBusinessCardByMemberIdx(Long memberIdx) {
        BusinessCard businessCard = businessCardMapper.findByMemberIdx(memberIdx);
        return businessCard != null;
    }

    public void save(BusinessCard businessCard) {
        businessCardMapper.save(businessCard);
        System.out.println("BusinessCard ID: " + businessCard.getIdx()); // 저장 후 idx 값 확인
    }

    public void delete(Long idx) {
        businessCardMapper.delete(idx);
    }

    public BusinessCard updateBusinessCard(BusinessCard businessCard) {
        businessCardMapper.updateBusinessCard(businessCard);
        return businessCard;
    }




}