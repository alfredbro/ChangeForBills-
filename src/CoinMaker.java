import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;


@FunctionalInterface
interface CoinVerifyInterface {

    Float verifyCoins();
}

@FunctionalInterface
interface CoinMakerInterface {

    void convertToCoins(Float billAmount);
}


public class CoinMaker {

	private Map<Float, Integer> coinsMap = null;
	
	CoinMaker() {
		this.coinsMap = new LinkedHashMap<Float, Integer>(); 
	}
	
	public static void main(String[] args) {

		CoinMaker coinMaker = new CoinMaker();
		coinMaker.configureCoins();


		System.out.println("Configuration Complete...");
		System.out.println("Machine contains coins with total value of: " + 
			coinMaker.ref.verifyCoins());
		
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			System.out.println("");
			System.out.println("Enter amount that you wish to convert to change: ");
	
			Integer billInteger = 0;
			try {
				billInteger = Integer.valueOf(scanner.nextLine());
			} catch(NumberFormatException nfe) {
				System.out.println("Invalid amount. " + 
					"Amount must be an Integer (only bills)");
				continue;
			}
			
			/* We want the billAmount to be a Float, as it propagates
			 * through the logic. This is so change can be subtracted.
			 */
			Float billAmount = Float.valueOf(billInteger);
			
			try {
				coinMaker.ref2.convertToCoins(billAmount);
			} catch(IllegalStateException ex) {
				// exception already logged
				continue;
			}
		
			System.out.println("COIN CHANGE PROVIDED");
			
			Integer totalCoinsLeft = coinMaker.coinsMap.values().stream()
				.reduce(0, Integer::sum);
			
			if(totalCoinsLeft == 0) {
				System.out.println("");
				System.out.println("Machine is out of Coins");
				System.out.println("");
				System.out.println("");
				break;
			}
		}
	}
	
	private void configureCoins() {

		// TODO: handle float type better (don't use 'f')
		this.coinsMap.put(0.25f, 100);
		this.coinsMap.put(0.10f, 100);
		this.coinsMap.put(0.05f, 100);
		this.coinsMap.put(0.01f, 100);		
	}
	
	private CoinVerifyInterface ref = () -> {

 		Float valueAllCoins = 0.0f;
 		for(Entry<Float, Integer> coinEntry : this.coinsMap.entrySet()) {
 			
 			System.out.println("there are [" + 
 			coinEntry.getValue() +
 			"] coins of size [" + 
 			coinEntry.getKey() + "]");
 			
 			valueAllCoins += coinEntry.getKey() * coinEntry.getValue();
 		}

 		return valueAllCoins;
    };
     
 	private CoinMakerInterface ref2 = (billAmount) -> {
 		
		System.out.println("");
		System.out.println("");
		System.out.println("billAmount = " + billAmount);
		System.out.println("");

		/* Check whether machine has enough coins.
		 * TODO: This aggregator could also be a lambda.
		 */
		Float totalCoinValueInMachine = 0.0f;
		for(Entry<Float, Integer> coinEntry : coinsMap.entrySet()) {
			
			totalCoinValueInMachine += 
				coinEntry.getKey() * coinEntry.getValue();
		}
		if(billAmount > totalCoinValueInMachine) {
			System.out.println("ERROR: Machine does not have enough coins " +
					"to make change for this amount.");
			throw new IllegalStateException();
		}
		
		for(Entry<Float, Integer> coinEntry : coinsMap.entrySet()) {
			
			Float coinValue = coinEntry.getKey() *
							coinEntry.getValue();
			
			if(billAmount >= coinValue) {
			
				billAmount -= coinValue;
				
				System.out.println("Used All [" + 
				coinEntry.getValue() + 
				"] coins, with total value [" +
				coinValue +
				"]");
				
				coinEntry.setValue(0);
				
			} else {
				Integer coinsUsed = 
					(int) (billAmount / coinEntry.getKey());
				
				billAmount -= (int) (coinsUsed * coinEntry.getKey());
				
				System.out.println("Used [" +
				coinsUsed + 
				"] coins of denomination [" + 
				coinEntry.getKey() + 
				"], with total value [" +
				coinsUsed * coinEntry.getKey() +
				"]");
				
				coinEntry.setValue((int) (coinEntry.getValue() - coinsUsed));
			}

			System.out.println("there are now [" + 
			coinEntry.getValue() +
			"] coins of size [" + 
			coinEntry.getKey() + "]");
		}
 	};
}
