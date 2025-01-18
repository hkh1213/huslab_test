package org.example.category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryBoard {
    private final Map<Integer, Category> categoriesById;
    private final Map<Integer, List<Integer>> parentChildRelationships;
    private int currentId;

    public CategoryBoard() {
        this.categoriesById = new HashMap<>();
        this.parentChildRelationships = new HashMap<>();
        this.currentId = 1; // 초기 ID 설정
    }

    /**
     * 카테고리 추가
     * @param categoryName 카테고리 이름
     * @param parentIdx 부모 카테고리 ID (없을 경우 -1)
     */
    public void addCategory(String categoryName, int parentIdx) {
        int categoryId = generateId();
        Category category = new Category(categoryId, categoryName);
        category.setParentIdx(parentIdx);

        categoriesById.put(categoryId, category);

        if (parentIdx != -1) {
            if (!parentChildRelationships.containsKey(parentIdx)) {
                parentChildRelationships.put(parentIdx, new ArrayList<>());
            }
            parentChildRelationships.get(parentIdx).add(categoryId);
        }
    }

    private int generateId() {
        return currentId++;
    }

    /**
     * 카테고리 구조를 JSON 문자열로 반환
     * @return JSON 문자열
     */
    public String getCategoryStructureAsJson() {
        List<Category> allCategories = categoriesById.values().stream()
                .flatMap(this::flattenCategories)
                .toList();

        JSONArray jsonArray = new JSONArray();
        for (Category category : allCategories) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", category.getId());
            jsonObject.put("name", category.getName());
            jsonObject.put("childIds", category.getChildIds());
            jsonObject.put("parentIdx", category.getParentIdx());
            if (category.getBoardNumber() != null) {
                jsonObject.put("boardNumber", category.getBoardNumber());
            }
            jsonArray.put(jsonObject);
        }

        return jsonArray.toString();
    }

    private Stream<Category> flattenCategories(Category category) {
        List<Category> result = new ArrayList<>();
        result.add(category);
        for (int childId : category.getChildIds()) {
            result.addAll(flattenCategories(categoriesById.get(childId)).toList());
        }
        return result.stream();
    }

    /**
     * 이름으로 카테고리 검색
     *
     * @param categoryName 카테고리 이름
     * @return 검색된 카테고리 객체 목록
     */
    public List<Category> findCategoryByName(String categoryName) {
        return categoriesById.values().stream()
                .filter(category -> category.getName().equals(categoryName))
                .collect(Collectors.toList());
    }

    /**
     * ID로 카테고리 검색
     * @param categoryId 카테고리 ID
     * @return 검색된 카테고리 객체
     */
    public Category findCategoryById(int categoryId) {
        return categoriesById.get(categoryId);
    }

    /**
     * 부모 카테고리 ID와 하위 카테고리 ID 목록 반환
     * @return 부모-하위 카테고리 관계 맵
     */
    public Map<Integer, List<Integer>> getParentChildRelationships() {
        return parentChildRelationships;
    }

    /**
     * 기존 그룹 카테고리에 새로운 멤버 추가
     * @param groupCategory 그룹 카테고리 객체
     * @param memberName 새로운 멤버 이름
     * @param boardNumber 지정할 게시판 번호
     */
    public void addCategoryToGroup(Category groupCategory, String memberName, int boardNumber) {
        Category memberCategory = findOrCreateCategory(groupCategory, memberName);
        memberCategory.setBoardNumber(boardNumber);
    }

    /**
     * 상위 카테고리가 기존에 존재하지 않을 때 신규 카테고리 추가
     * @param parent 상위 카테고리
     * @param categoryName 신규 카테고리명
     * @return 신규 카테고리
     */
    private Category findOrCreateCategory(Category parent, String categoryName) {
        for (int childId : parent.getChildIds()) {
            Category child = categoriesById.get(childId);
            if (child.getName().equals(categoryName)) {
                return child;
            }
        }

        int categoryId = generateId();
        Category newCategory = new Category(categoryId, categoryName);
        newCategory.setParentIdx(parent.getId());
        categoriesById.put(categoryId, newCategory);
        parent.addChildId(categoryId);

        return newCategory;
    }

    public static void main(String[] args) {
        CategoryBoard board = new CategoryBoard();

        // Adding categories dynamically
        board.addCategory("남자", -1); // 성별
        board.addCategory("여자", -1);
        board.addCategory("한국", 1); // 남자 > 한국
        board.addCategory("미국", 1); // 남자 > 미국
        board.addCategory("한국", 2); // 여자 > 한국
        board.addCategory("미국", 2); // 여자 > 미국

        board.addCategory("방탄", 3); // 남자 > 한국 > 방탄
        board.addCategory("엑소", 3); // 남자 > 한국 > 엑소
        board.addCategory("블랙핑크", 5); // 여자 > 한국 > 블랙핑크
        board.addCategory("에스파", 5); // 여자 > 한국 > 에스파

        Category 방탄 = board.findCategoryByName("방탄").getFirst();
        board.addCategoryToGroup(방탄, "공지사항", 1);
        board.addCategoryToGroup(방탄, "뷔", 2);
        board.addCategoryToGroup(방탄, "진", 3);

        Category 엑소 = board.findCategoryByName("엑소").getFirst();
        board.addCategoryToGroup(엑소, "공지사항", 1);
        board.addCategoryToGroup(엑소, "첸", 2);
        board.addCategoryToGroup(엑소, "백현", 3);

        Category 블랙핑크 = board.findCategoryByName("블랙핑크").getFirst();
        board.addCategoryToGroup(블랙핑크, "공지사항", 1);
        board.addCategoryToGroup(블랙핑크, "로제", 2);
        board.addCategoryToGroup(블랙핑크, "제니", 3);

        Category 에스파 = board.findCategoryByName("에스파").getFirst();
        board.addCategoryToGroup(에스파, "공지사항", 1);
        board.addCategoryToGroup(에스파, "윈터", 2);
        board.addCategoryToGroup(에스파, "카리나", 3);

        System.out.println("Category Board JSON Structure:");
        System.out.println(board.getCategoryStructureAsJson());
        saveCategoryToJson(board.getCategoryStructureAsJson());
    }
    
    public static void saveCategoryToJson(String jsonData) {
        String projectDir = System.getProperty("user.dir");

        File file = new File(projectDir, "category.json");

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonData);
            System.out.println("category.json 파일에 JSON 데이터가 저장되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
