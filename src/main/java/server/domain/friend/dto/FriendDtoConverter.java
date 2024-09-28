//package server.domain.friend.dto;
//
//import server.domain.friend.domain.Friend;
//
//import java.util.List;
//
//public class FriendDtoConverter {
//
//    public static FriendResponseDto.FriendInfoResponseDto convertToFriendTaskSuccessResponseDto(Friend friend) {
////        return FriendResponseDto.FriendInfoResponseDto.builder()
////                .idx(friend.getIdx())
////                .friendMemberId(friend.getFriendMemberId())
////                .createdAt(friend.getCreatedAt().toString())
////                .isFavorite(friend.isFavorite())
////                .build();
////    }
//
////    public static FriendResponseDto.FriendListResponseDto convertToFriendListResponseDto(List<Friend> friend) {
////        if (friend == null) {
////            return FriendResponseDto.FriendListResponseDto.builder()
////                    .cnt(0)
////                    .isSuccess(false)
////                    .build();
////        }
////        return FriendResponseDto.FriendListResponseDto.builder()
////                .cnt(friend.size())
////                .friendList(friend.stream().map(FriendDtoConverter::convertToFriendTaskSuccessResponseDto).toList())
////                .isSuccess(true)
////                .build();
////    }
////}
