import java.util.Arrays;
import java.util.Comparator;

public class test {
    public static void main(String[] args) {
        int[][] arr= {{1,2,4},{2,1,3},{3,6,8}};
        Arrays.sort(arr, Comparator.comparingInt(o -> o[2]));
        printout(arr);

    }
    public static void printout(int[][] arr){
        for(int i=0;i<arr.length;i++){
            for(int j=0;j<arr[i].length;j++){
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }
    }
}

