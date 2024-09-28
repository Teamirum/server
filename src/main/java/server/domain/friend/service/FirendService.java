package server.domain.friend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.friend.repository.FriendRepository;
import server.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class FirendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;



}
