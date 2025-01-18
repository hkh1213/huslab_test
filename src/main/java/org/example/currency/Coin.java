package org.example.currency;

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

    public static void main(String[] args) {
        int sum = 10;
        int[] coins = {2, 5, 3, 6};
        
        System.out.println(countMethods(sum, coins));
    }
}
