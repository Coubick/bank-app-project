package org.example.DTO;

import lombok.Data;

import java.util.List;

@Data
public class FriendsWithAccountsDTO {
    private String friendName;
    private List<Integer> accountIds;

    public FriendsWithAccountsDTO(String friendName, List<Integer> accountIds) {
        this.friendName = friendName;
        this.accountIds = accountIds;
    }
}