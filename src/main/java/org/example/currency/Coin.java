package org.example.currency;

import java.util.ArrayList;
import java.util.List;

public class Coin {

    public static int countMethods(int sum, int[] coins) {
        // method_arr[i]는 합이 i인 경우의 가지수
        int[] method_arr = new int[sum + 1];

        // sum == 0 일 때
        method_arr[0] = 1;

        // 동전으로 만들 수 있는 경우의 수 합산
        for (int coin : coins) {
            for (int i = coin; i <= sum; i++) {
                method_arr[i] += method_arr[i - coin];
            }
        }

        return method_arr[sum];
    }

    // 실제 조합을 반환하는 메서드 (추가)
    public static List<List<Integer>> getCombinations(int sum, int[] coins) {
        List<List<Integer>>[] combinations = new List[sum + 1];

        for (int i = 0; i <= sum; i++) {
            combinations[i] = new ArrayList<>();
        }
        combinations[0].add(new ArrayList<>());

        // 각 동전에 대해 가능한 조합 추가
        for (int coin : coins) {
            for (int i = coin; i <= sum; i++) {
                for (List<Integer> combination : combinations[i - coin]) {
                    List<Integer> newCombination = new ArrayList<>(combination);
                    newCombination.add(coin);
                    combinations[i].add(newCombination);
                }
            }
        }

        return combinations[sum];
    }

    public static void main(String[] args) {
        int sum = 10;
        int[] coins = {2, 5, 3, 6};

        System.out.println("총 경우의 수: " + countMethods(sum, coins));

        List<List<Integer>> combinations = getCombinations(sum, coins);
        System.out.println("모든 경우의 조합:");
        for (List<Integer> combination : combinations) {
            System.out.println(combination);
        }
    }
}
