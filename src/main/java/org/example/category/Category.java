package org.example.category;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id;
    private String name;
    private List<Integer> childIds;
    private Integer parentIdx;
    private Integer boardNumber;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        this.childIds = new ArrayList<>();
        this.parentIdx = null; // 초기값 null로 설정
        this.boardNumber = null; // 초기값 null로 설정
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getChildIds() {
        return childIds;
    }

    public void addChildId(int childId) {
        childIds.add(childId);
    }

    public Integer getParentIdx() {
        return parentIdx;
    }

    public void setParentIdx(Integer parentIdx) {
        this.parentIdx = parentIdx;
    }

    public Integer getBoardNumber() {
        return boardNumber;
    }

    public void setBoardNumber(Integer boardNumber) {
        this.boardNumber = boardNumber;
    }
}
