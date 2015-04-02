import java.util.InputMismatchException;
/*
 * Author: Rich P 
 * 
 * 
 * Calculate the total cost of an item including tax based on whether it is considered a necessary or luxury item. 
 * The tax rate for basic necessities is 1%, the tax rate for luxury items is 9%. For simplicity of implementation, 
 * all transactions are measured in cents (pennies).
 * 
 * Input: 1) Number of Items.
 *        2) Item Price.
 *        3) Input Luxury or Neccesity Item.
 * 
 * */
import java.util.Scanner;

public class TaxPerson {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		try{
			System.out.println("Enter the number of items you want to buy?");
			int noOfItems = scanner.nextInt();
			if(noOfItems <= 0)
				System.out.println("Error!!! Invalid Input...");
			else{
				int[] items = new int[noOfItems];
				float[] itemsCo = new float[noOfItems];
				int ch;
				for(int i = 0; i < noOfItems; i++){
					System.out.println("Enter the cost of Item  "+(i+1)+" in cents : ");
					items[i] = scanner.nextInt();
					int flag = 0;
					while(flag == 0){
						System.out.println("Enter 1 - For luxury item");
						System.out.println("Enter 2 - For Necessity item");
						System.out.println("Enter if this item is luxury or a necessity");
						ch = scanner.nextInt();
						if(ch == 1){
							itemsCo[i] = (float) ((items[i] * (0.09)) + items[i]);
							flag = 1;
						}else if( ch == 2){
							itemsCo[i] = (float) ((items[i] * (0.01)) + items[i]);
							flag = 1;
						}else{

							System.out.println("Invalid input! Please enter either 1 or 2");
						}						
					}
				}
				float tot = (float) 0.0;
				for(int i = 0 ; i < noOfItems; i++){
					System.out.println("The cost of item no. "+(i+1)+" is: " + itemsCo[i]);
					tot = tot + itemsCo[i];
				}
				System.out.println("The total cost of the items is: " + tot);
			}
		}catch(Exception e){
			System.out.println("Error! The input should be an integer. E.g. 1, 2, 10, 100..");
		}finally{
			scanner.close();
		}
	}

}
