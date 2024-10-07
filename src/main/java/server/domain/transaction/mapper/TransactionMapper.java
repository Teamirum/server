package server.domain.transaction.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.transaction.domain.Transaction;

import java.util.List;
import java.util.Map;

@Mapper
public interface TransactionMapper {

    // 거래 내역 저장
    void save(Transaction transaction);

    // 특정 거래 내역 조회 (거래 ID로 조회)
    Transaction findByTransactionIdx(Long transactionIdx);

    // 특정 카드의 모든 거래 내역 조회 (카드 ID로 조회)
    List<Transaction> findTransactionsByCreditIdx(Long creditIdx);

    // 특정 회원의 모든 거래 내역 조회 (회원 ID로 조회)
    List<Transaction> findAllByMemberIdx(Long memberIdx);

    Transaction findByIdxAndMemberIdx(Map<String, Object> map);

    void delete(Long idx);

    // 거래 내역 업데이트
    void updateTransaction(Transaction transaction);
}
