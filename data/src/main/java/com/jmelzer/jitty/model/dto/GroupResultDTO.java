package com.jmelzer.jitty.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 14.08.2016.
 * For display results in the liveview
 */
public class GroupResultDTO {
    String groupName;
    List<GroupResultEntryDTO> entries = new ArrayList<>();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<GroupResultEntryDTO> getEntries() {
        return entries;
    }

    public void setEntries(List<GroupResultEntryDTO> entries) {
        this.entries = entries;
    }

}
